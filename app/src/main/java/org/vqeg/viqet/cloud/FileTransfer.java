/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.cloud;

import android.content.Context;
import android.util.Log;

import org.vqeg.viqet.utilities.FileHelper;
import org.vqeg.viqet.utilities.SystemInfo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileTransfer
{
    public static boolean UploadFile(String filepath, String SasURL, Context context)
    {
        if (SystemInfo.isNetworkPresent(context))
        {
            final File file = new File(filepath);
            final long fileLength = file.length();

            try {
                URL url = new URL(SasURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("PUT");
                urlConnection.addRequestProperty("x-ms-blob-type", "BlockBlob");
                urlConnection.addRequestProperty("Content-Type", "image/jpeg");
                urlConnection.setRequestProperty("Content-Length", Long.toString(fileLength));
                // Write image data to server
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

                int bytesRead = 0;
                byte[] byteBuffer = new byte[1024];
                FileInputStream inputStream = new FileInputStream(file);
                while ((bytesRead = inputStream.read(byteBuffer)) != -1)
                {
                    wr.write(byteBuffer, 0, bytesRead);
                }
                wr.flush();
                wr.close();
                inputStream.close();
                int response = urlConnection.getResponseCode();
                //If we successfully uploaded, return true
                if (response == HttpURLConnection.HTTP_CREATED)
                {
                    return true;
                }
                else
                {
                    Log.i(FileTransfer.class.getSimpleName(), "Upload Failed. Server Response: " + urlConnection.getResponseMessage());
                }
            }
            catch (Exception e)
            {
                Log.i(FileTransfer.class.getSimpleName(), "Exception while uploading photo" + e.toString());
            }
        }
        else
        {
            Log.i(FileTransfer.class.getSimpleName(), "Upload Failed (No Network) - " + filepath);
        }
        return false;
    }

    public static String Download(String filenamePrefix, String directoryName, String sasURL, Context context)
    {
        if (SystemInfo.isNetworkPresent(context))
        {
            HttpURLConnection connection = null;
            try
            {
                sasURL = sasURL.replaceAll(" ", "%20");
                URL url = new URL(sasURL);

                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // check for HTTP 200 OK
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    File file = FileHelper.createUniqueFile(filenamePrefix, directoryName);
                    if(file != null) {
                        //Download the file in chunks
                        OutputStream outputStream = new FileOutputStream(file);
                        InputStream ipStream = connection.getInputStream();
                        byte[] byteBuffer = new byte[1024];
                        int bytesRead = 0;
                        while ((bytesRead = ipStream.read(byteBuffer)) != -1) {
                            outputStream.write(byteBuffer, 0, bytesRead);
                        }
                        ipStream.close();
                        outputStream.flush();
                        outputStream.close();

                        return file.getAbsolutePath();
                    }
                    else{
                        Log.i(FileTransfer.class.getSimpleName(), "Error while creating file photo");
                    }

                }
                else
                {
                    Log.i(FileTransfer.class.getSimpleName(), "Server error while downloading photo: " + connection.getResponseCode() + " " +connection.getResponseMessage());
                }

            }
            catch (Exception e)
            {
                Log.i(FileTransfer.class.getSimpleName(), "Exception while downloading photo: " + sasURL + e.toString());
            }
            finally
            {
                if(connection!=null) {
                    connection.disconnect();
                }
            }
        }
        else
        {
            Log.i(FileTransfer.class.getSimpleName(), "Download Failed (No Network)");
        }
        return null;
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