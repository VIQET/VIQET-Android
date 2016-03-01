/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.utilities;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileHelper 
{
    private static String appDirectoryPath = Environment.getExternalStorageDirectory() + File.separator + "Android" + File.separator + "data" + File.separator + "org.vqeg.viqet";

    public static final String photoDirectoryName =  "Photos";
    public static final String testPhotoDirectoryName = "Tests";
    public static final String methodologyDirectoryName = "Methodology";

    public static final String photoPrefix = "IMG_";
    public static final String visualizationPrefix = "VIZ_";
    public static final String testPhotoPrefix = "TEST_";
    public static final String methodologyPhotoPrefix = "EXAMPLE_";

    public static File createUniqueFile(String filenamePrefix, String directoryName)
	{
		try
        {
            //Create directory if it doesn't exist
            File directory = new File(appDirectoryPath + File.separator + directoryName);
            directory.mkdirs();

            //Find a unique name
            int id = -1;
            //String filenamePrefix = "IMG_";
            File imageFile = null;
            do
            {
                id++;
                imageFile = new File(directory, filenamePrefix + String.format("%03d", id) + ".jpg");
            }
            while (imageFile.exists());

            //Create the file
            if (imageFile.createNewFile() && imageFile.exists())
            {
                return imageFile;
            }
        }
        catch(IOException exception)
        {
            Log.e(FileHelper.class.getName(),"Could not create file" + exception.toString());
        }
		return null;
	}

    //Just returns the file object. The calling function will check file.exists() to determine if the file exists or not
    public static File getFile(String filename, String directoryPath)
    {
        File externalStorageDirectory = new File(appDirectoryPath + File.separator  + directoryPath);
        externalStorageDirectory.mkdirs();
        File file  = new File(externalStorageDirectory + File.separator + filename);
        return file;
    }
	
	public static void copy(File src, File dst) throws IOException
	{
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) 
	    {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
    }

    private final static String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

    public static boolean checkIfImage(File file)
    {
        for (String extension : okFileExtensions)
        {
            if (file.getName().toLowerCase().endsWith(extension))
            {
                return true;
            }
        }
        return false;
    }
}
