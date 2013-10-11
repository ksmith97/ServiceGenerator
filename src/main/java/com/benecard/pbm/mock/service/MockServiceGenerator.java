/**
 * 
 */
package com.benecard.pbm.mock.service;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.type.PrimitiveType;
import japa.parser.ast.type.ReferenceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benecard.pbm.mock.helper.BuilderUtil;
import com.benecard.pbm.mock.helper.ImportResolver;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

/**
 * @author ksmith_cntr
 * 
 */
public final class MockServiceGenerator
{
    private static final String classTemplate;
    private static final Logger logger = LoggerFactory.getLogger( MockServiceGenerator.class );
    private static final String methodTemplate;

    static
    {
        try
        {
            classTemplate = BuilderUtil.loadResourceAsString( "/ServiceClassTemplate" );
            methodTemplate = BuilderUtil.loadResourceAsString( "/MethodTemplate" );
        }
        catch( final IOException e )
        {
            MockServiceGenerator.logger.error( "Faild to load Templates!", e );
            throw new Error( "Failed to load templates.", e );
        }
    }

    public static void parseFile( final Reader in, final Writer out )
    throws IOException, ParseException
    {
        final CompilationUnit cu = JavaParser.parse( in );

        out.write( new MockServiceGenerator( cu ).generate() );
        out.flush();
    }

    private final CompilationUnit compUnit;

    public MockServiceGenerator( final CompilationUnit cu )
    {
        this.compUnit = cu;
    }

    public final String generate()
    {
        final String className = BuilderUtil.getClassName( this.compUnit );
        final Collection<MethodDeclaration> methods = BuilderUtil.getMethods( this.compUnit );

        return MockServiceGenerator.classTemplate
        .replace( "<className>", className )
        .replace( "<date>", new SimpleDateFormat( "MM/dd/yyyy" ).format( new Date() ) )
        .replace( "<localEjbRef>", "I" + className + "Local" )
        .replace( "<package>", this.compUnit.getPackage().toString().trim() )
        .replace( "<methods>", Joiner.on( "\n" ).join( this.getMethods( methods ) ) )
        .replace( "<imports>", Joiner.on( "\n" ).join(
            ImportResolver.resolveImports( this.compUnit.getImports(), BuilderUtil.getMethodTypes( methods ) ) ) );
    }

    private String generateMethod(final MethodDeclaration method)
    {
        return MockServiceGenerator.methodTemplate
        .replace( "<methodName>", method.getName() )
        .replace( "<returnType>", method.getType().toString() )
        .replace( "<params>",
            method.getParameters() != null ? Joiner.on( ", " ).join( method.getParameters() ) : "" )
            .replace( "<return>", this.generateMethodReturn( method.getType() ) )
            .replace( "<exceptions>", method.getThrows() != null ? Joiner.on( "," ).join( method.getThrows() ) : "" );
    }

    private String generateMethodReturn( final Type type )
    {
        if ( type instanceof VoidType )
        {
            return "";
        }
        else if ( type instanceof ReferenceType )
        {
            return "null";
        }
        else if( type instanceof PrimitiveType )
        {
            final PrimitiveType primitive = (PrimitiveType) type;
            switch(primitive.getType())
            {
                case Boolean:
                    return "true";
                case Byte:
                    return "1";
                case Char:
                    return "'A'";
                case Double:
                    return "1.0d";
                case Float:
                    return "1.0f";
                case Int:
                    return "1";
                case Long:
                    return "1L";
                case Short:
                    return "1";
                default:
                    return "";
            }
        }

        throw new Error("Could not establish the proper return type for method. Type: " + type);
    }

    private Collection<String> getMethods( final Collection<MethodDeclaration> existingMethods )
    {
        final Collection<String> methodStrs = Lists.newArrayListWithCapacity( existingMethods.size() );

        for( final MethodDeclaration m : existingMethods )
        {
            methodStrs.add( this.generateMethod( m ) );
        }
        return methodStrs;
    }
}
