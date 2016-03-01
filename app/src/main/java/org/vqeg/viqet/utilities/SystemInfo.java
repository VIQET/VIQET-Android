/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.utilities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class SystemInfo 
{
	public static int getNetworkFlags(Context context)
	{
		//Check settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean mobilePermission = sharedPref.getBoolean("useMobile", true);
		boolean wiFiPermission = sharedPref.getBoolean("useWiFi", true);
		
		int flags = 0;
		if(mobilePermission)
		{
			flags = flags | DownloadManager.Request.NETWORK_MOBILE;
		}
		
		if(wiFiPermission)
		{
			flags = flags | DownloadManager.Request.NETWORK_WIFI;
		}

		return flags;
	}
	
	public static boolean isNetworkPresent(Context context)
	{
		//Check settings
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean mobilePermission = sharedPref.getBoolean("useMobile", true);
		boolean wiFiPermission = sharedPref.getBoolean("useWiFi", true);
		
		if(mobilePermission && isNetworkTypePresent(context, ConnectivityManager.TYPE_MOBILE))
		{
			return true;
		}
		else if(wiFiPermission && isNetworkTypePresent(context, ConnectivityManager.TYPE_WIFI))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private static boolean isNetworkTypePresent(Context context, int networkType)
	{
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if(activeNetwork != null && activeNetwork.isConnected())
		{
			NetworkInfo[] netInfo = cm.getAllNetworkInfo();
			for (NetworkInfo ni : netInfo)
			{
				if(ni.getType() == networkType && ni.isConnected())
			    {
					return true;
			    }
			}
		}
		return false;
	}
}

