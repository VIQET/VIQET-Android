/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.alarms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.vqeg.viqet.services.PhotoInspectorService;


public class PhotoInspectorAlarmReceiver extends BroadcastReceiver
{
	@Override
    public void onReceive(Context context, Intent intent)
    {   
		Log.i(this.getClass().getSimpleName(), "Alarm fired");
		Intent startServiceIntent = new Intent();
		startServiceIntent.setClass(context, PhotoInspectorService.class);
		startServiceIntent.setAction(PhotoInspectorService.INSPECT_ALL_PENDING_PHOTOS_ACTION);
		context.startService(startServiceIntent);
    }
}