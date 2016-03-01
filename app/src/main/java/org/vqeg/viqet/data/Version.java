/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

import android.util.Log;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Version implements Serializable
{
	private static String TAG = "Version Object";
	
	//Major Version
	private int methodologyVersion;
	public int getMethodologyVersion()
	{
		return this.methodologyVersion;
	}
    public void setMethodologyVersion(String methodologyVersion)
    {
        this.methodologyVersion = Integer.parseInt(methodologyVersion);
    }

	//Minor Version
	private int algoVersion;
	public int getAlgoVersion()
	{
		return this.algoVersion;
	}

    public void setVersionFromString(String versionString)
	{
		if(versionString != null)
		{

			String[] versionParts = versionString.split("_");
			if(versionParts.length < 2)
			{
				Log.e(TAG, "returned version is invalid");
			}
			else
			{
				this.methodologyVersion = Integer.parseInt(versionParts[0]);
				this.algoVersion = Integer.parseInt(versionParts[1]);
			}
		}
	}
}
