/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.asynctasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import org.vqeg.viqet.R;
import org.vqeg.viqet.cloud.CloudCommunicator;
import org.vqeg.viqet.cloud.Json.VersionResponse;
import org.vqeg.viqet.data.RemoteInfo;
import org.vqeg.viqet.data.RemoteInfoProvider;
import org.vqeg.viqet.data.Version;
import org.vqeg.viqet.fragments.HomeFragment;

/**
 * Created by rkalidin on 2/26/2016.
 */
public class VersionUpdateTask extends AsyncTask<Void,Void,Void>{

    private Activity activity;
    private ProgressDialog progress;
    private  boolean versionChange = false;

    public VersionUpdateTask(Activity activity)
    {
        progress = new ProgressDialog(activity);
        progress.setMessage("Checking for version updates");
        progress.setCancelable(false);
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        progress.dismiss();
        if(versionChange){
            Fragment fragment = new HomeFragment();
            FragmentManager fragmentManager = activity.getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "Home Fragment").commit();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle(R.string.version_change_title).setMessage(R.string.text_dialog_version_change);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    private void updateVersion()
    {
        VersionResponse versionResponse = CloudCommunicator.FetchVersion(activity.getApplicationContext());
        Version newVersion = new Version();
        if(versionResponse!= null){
            newVersion.setVersionFromString(versionResponse.getVersionString());
            RemoteInfo remoteInfo =  RemoteInfoProvider.getRemoteInfo();
            if(remoteInfo!= null){
                if(!(newVersion.getAlgoVersion() == remoteInfo.getVersion().getAlgoVersion()) ||
                        !(newVersion.getMethodologyVersion() == remoteInfo.getVersion().getMethodologyVersion()) || !(newVersion.getReleaseVersion() == remoteInfo.getVersion().getReleaseVersion()))
                {
                    remoteInfo.setVersion(newVersion);
                    versionChange =true;
                }
            }
        }
    }
    @Override
    protected Void doInBackground(Void... params) {
        updateVersion();
        return null;
    }
}
