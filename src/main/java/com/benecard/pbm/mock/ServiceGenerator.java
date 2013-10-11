/**
 * 
 */
package com.benecard.pbm.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.benecard.pbm.mock.helper.FileHelper;
import com.benecard.pbm.mock.service.MockServiceGenerator;
import com.google.common.base.Predicate;

/**
 * @author ksmith_cntr
 *
 */
public class ServiceGenerator
{
    private static final Logger logger = LoggerFactory.getLogger( ServiceGenerator.class );

    public static void main( final String[] args ) throws Exception
    {
        MockServiceGenerator.parseFile( new BufferedReader( new InputStreamReader( new FileInputStream(
        "C:\\DevelopmentTools\\PostalAddressService.java" ) ) ),
        new BufferedWriter( new OutputStreamWriter( new FileOutputStream(
        "C:\\DevelopmentTools\\PostalAddressServiceMock.java" ) ) ) );

        if ( true )
        {
            return;
        }
        final File targetServiceProject;
        if ( args.length >= 1 )
        {
            targetServiceProject = new File( args[0] );
        }
        else
        {
            targetServiceProject = new File( "." );
        }

        final File targetGenProject;
        if ( args.length >= 2 )
        {
            targetGenProject = new File( args[1] );
        }
        else
        {
            targetGenProject = new File(".");
        }
        new ServiceGenerator( targetServiceProject, targetGenProject ).generate();
    }

    private final File destination;
    private final File target;

    public ServiceGenerator( final File a, final File b )
    {
        this.target = a;
        this.destination = b;
    }

    public void generate()
    {
        if ( !this.destination.isDirectory() )
        {
            throw new IllegalArgumentException( "The destination to put the generated files is not a directory." );
        }

        if ( this.destination.listFiles().length > 0 )
        {
            System.out.println( "The destination for the generated files is not empty." );
        }

        final Collection<File> files = FileHelper.getChildFiles( this.target, new Predicate<File>()
            {
            public boolean apply( final File f )
            {
                return f.getName().endsWith( ".java" ) || f.getName().equals( "ibm-ejb-jar-bnd.xml" );
            }
            } );


    }


}
