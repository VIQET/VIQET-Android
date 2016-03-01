/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.cloud;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.vqeg.viqet.cloud.Json.MethodologyResponse;
import org.vqeg.viqet.cloud.Json.PhotoResponse;
import org.vqeg.viqet.cloud.Json.SASResponse;
import org.vqeg.viqet.cloud.Json.VersionResponse;
import org.vqeg.viqet.utilities.SystemInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CloudCommunicator
{
    private static String webService = "VQRestfulService.svc";

    private static String analyzePictureAPI = "AnalyzePictureREST";
    private static String versionAPI = "FetchVersionInfo";
    private static String methodologyAPI = "FetchMethodologyInfo";
    private static String uploadSasAPI = "GetBlobPhotoNameREST";

    private static int connectTimeout = 10000;
    private static int readTimeout = 10000;

    public static PhotoResponse FetchPhotoDetails(String blobName, String containerName, String category, Context context)
    {
        final String fetchURL = getServerName(context) + "/" + webService + "/" + analyzePictureAPI;
        PhotoResponse photoResponse = null;

        if (SystemInfo.isNetworkPresent(context))
        {
            HttpURLConnection connection = null;
            try
            {
                String charset = StandardCharsets.UTF_8.name();
                String query = String.format("%s=%s&%s=%s&%s=%s&%s=%s",
                        "blobFileName", URLEncoder.encode(blobName, charset),
                        "containerName", URLEncoder.encode(containerName, charset),
                        "category", URLEncoder.encode(category, charset),
                        "MOSmodel", URLEncoder.encode(getMOSModelType(context), charset));

                URL url = new URL(fetchURL + "?" + query);
                Log.i("BlobResultURL",url.toString());
                connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(connectTimeout);
                connection.setReadTimeout(readTimeout);

                connection.setUseCaches(false);
                connection.setDoInput(true); // true if we want to read server's response
                connection.setDoOutput(false); // false indicates this is a GET request

                // check for HTTP 200 OK
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String response = fetchResponseFromConnection(connection.getInputStream());
                    photoResponse =  ResponseParser.ParsePhotoResponse(response);
                }
                else
                {
                    Log.e(CloudCommunicator.class.getSimpleName(), "Server error while fetching photo details: " + connection.getResponseCode() + " " +connection.getResponseMessage());
                }
            }
            catch(Exception exception){
                Log.e(CloudCommunicator.class.getSimpleName(), "Fetch Photo Details Failed: " + exception.toString());}
            finally{connection.disconnect();}
        }
        else {Log.e(CloudCommunicator.class.getSimpleName(), "No Internet Connection. Fetch Photo Details Failed.");}

        return photoResponse;
    }

    public static VersionResponse FetchVersion(Context context)
    {
        final String fetchURL = getServerName(context) + "/" + webService + "/" + versionAPI;

        if (SystemInfo.isNetworkPresent(context))
        {
            HttpURLConnection connection = null;
            try
            {
                URL url = new URL(fetchURL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                connection.setConnectTimeout(connectTimeout);
                connection.setReadTimeout(readTimeout);

                // check for HTTP 200 OK
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String response = fetchResponseFromConnection(connection.getInputStream());
                    return ResponseParser.ParseVersionResponse(response);
                }
                else
                {
                    Log.e(CloudCommunicator.class.getSimpleName(), "Server error while fetching version: " + connection.getResponseCode() + " " +connection.getResponseMessage()+" "+fetchURL);
                }
            }
            catch (Exception exception) {Log.e(CloudCommunicator.class.getSimpleName(), "Fetch Version Failed: " + exception.toString());}
            finally{connection.disconnect();}
        }
        else {Log.e(CloudCommunicator.class.getSimpleName(), "No Internet Connection. Fetch Version Failed.");}

        return null;
    }

    public static MethodologyResponse FetchMethodology(Context context)
    {
        final String fetchURL = getServerName(context) + "/" + webService + "/" + methodologyAPI;

        if (SystemInfo.isNetworkPresent(context))
        {
            HttpURLConnection connection = null;
            try
            {
                String charset = StandardCharsets.UTF_8.name();
                String query = String.format("%s=%s",
                        "s", URLEncoder.encode("1_2", charset));
                URL url = new URL(fetchURL+"?"+query);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                connection.setConnectTimeout(connectTimeout);
                connection.setReadTimeout(readTimeout);
                // check for HTTP 200 OK
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String response = fetchResponseFromConnection(connection.getInputStream());
                    return ResponseParser.ParseMethodologyResponse(response);
                }
                else
                {
                    Log.e(CloudCommunicator.class.getSimpleName(), "Server error while fetching version: " + connection.getResponseCode() + " " +connection.getResponseMessage());
                }
            }
            catch (Exception exception) {Log.e(CloudCommunicator.class.getSimpleName(), "Fetch Version Failed: " + exception.toString());}
            finally{connection.disconnect();}
        }
        else {Log.e(CloudCommunicator.class.getSimpleName(), "No Internet Connection. Fetch Version Failed.");}

        return null;
    }

    public static SASResponse FetchUploadSAS(Context context)
    {
        final String fetchURL = getServerName(context) + "/" + webService + "/" + uploadSasAPI;

        if (SystemInfo.isNetworkPresent(context))
        {
            HttpURLConnection connection = null;
            try
            {
                String charset = StandardCharsets.UTF_8.name();
                String query = String.format("%s=%s",
                "serverName", URLEncoder.encode("production", charset));

                URL url = new URL(fetchURL + "?" + query);
                connection = (HttpURLConnection) url.openConnection();

                connection.setConnectTimeout(connectTimeout);
                connection.setReadTimeout(readTimeout);

                connection.setUseCaches(false);
                connection.setDoInput(true); // true if we want to read server's response
                connection.setDoOutput(false); // false indicates this is a GET request

                // check for HTTP 200 OK
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    String response = fetchResponseFromConnection(connection.getInputStream());
                    return ResponseParser.ParseSASResponse(response);
                }
                else
                {
                    Log.e(CloudCommunicator.class.getSimpleName(), "Server error while fetching upload SAS: " + connection.getResponseCode() + " " +connection.getResponseMessage()+" "+url);
                }
            }
            catch (Exception exception) {Log.e(CloudCommunicator.class.getSimpleName(), "Fetch upload SAS Failed: " + exception.toString());}
            finally{
                if(connection!=null){
                    connection.disconnect();
                }}
        }
        else
        {Log.e(CloudCommunicator.class.getSimpleName(), "No Internet Connection. Fetch Upload SAS Failed.");}

        return null;
    }

    private static String getServerName(Context context)
    {
        //Get server name
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String serverDefaultValue = context.getResources().getString(org.vqeg.viqet.R.string.productionServerValue);
        return sharedPref.getString("serverType", serverDefaultValue);
    }

    private static String getMOSModelType(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String mosModelDefaultValue = context.getResources().getString(org.vqeg.viqet.R.string.CategoryMosModelValue);//TODO
        return sharedPref.getString("mosModelType", mosModelDefaultValue);
    }

    private static String fetchResponseFromConnection(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String response = "";
        String line = "";
        do
        {
            response += line;
            line = reader.readLine();
        }
        while(line != null);
        return response;
    }
}
