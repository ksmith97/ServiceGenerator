/**
 * 
 */
package com.benecard.pbm.mock.helper;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

/**
 * @author ksmith_cntr
 *
 */
public class FileHelper
{
    private static final Logger logger = LoggerFactory.getLogger( FileHelper.class );

    public static Collection<File> getChildFiles( final File startFile, final Predicate<File> filter )
    {
        if ( startFile.isFile() )
        {
            if ( filter.apply( startFile ) )
            {
                return Lists.newArrayList( startFile );
            }
            else
            {
                return Arrays.asList( new File[0] );
            }
        }
        else if ( startFile.isDirectory() )
        {
            final Collection<File> files = Lists.newArrayList();
            for( final File file : startFile.listFiles() )
            {
                files.addAll( FileHelper.getChildFiles( file, filter ) );
                if ( file.length() > 1000 )
                {
                    throw new Error(
                    "Found number of files exceeds the reasonable bound of 1000. This probably means the wrong target was specified." );
                }
            }

            return files;
        }
        else
        {
            FileHelper.logger.warn( "Encountered file that did not count as a file or a directory. File: "
            + startFile.getAbsolutePath() );
            return Arrays.asList( new File[0] );
        }

    }
}
