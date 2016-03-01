/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

import android.util.Log;
import org.vqeg.viqet.utilities.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RemoteInfoProvider 
{
	private static final String filename = "RemoteInfo.vqt";
	private static final String TAG = "RemoteInfoProvider";
	public static String BROADCAST_ACTION = "org.vqeg.broadcast.remoteInfoReceived";

	private static RemoteInfo remoteInfo;
	public static RemoteInfo getRemoteInfo()
	{
		if (remoteInfo == null)
		{
            File file = FileHelper.getFile(filename, FileHelper.methodologyDirectoryName);
            if(file.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    try {
                        remoteInfo = (RemoteInfo) objectInputStream.readObject();
                    } catch (ClassNotFoundException e) {
                        Log.e(TAG, "Class Not Found Exception thrown by getRemoteInfo() ");
                    }
                    objectInputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e(TAG, "File Not Found Exception thrown by getRemoteInfo() ");
                } catch (IOException e) {
                    Log.e(TAG, "IO Exception thrown by getRemoteInfo() ");
                }
            }
		}
		return remoteInfo;
	}
	public static RemoteInfo getCopyRemoteInfo() {
	return remoteInfo;
	}
	public static void setRemoteInfo(RemoteInfo remoteInfoObject)
	{
		remoteInfo = remoteInfoObject;

        //Persist the remote info
        File file = FileHelper.getFile(filename, FileHelper.methodologyDirectoryName);
		try 
		{
 			FileOutputStream fileOutputStream = new FileOutputStream(file);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(remoteInfo);
			objectOutputStream.close(); 
		} 
		catch (IOException e)
		{
            Log.e(TAG, "IO Exception thrown by setRemoteInfo()" + e.toString());
		}	
	}
}


