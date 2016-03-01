/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.fragments;

/**
 * Created by Rohit Kalidindi on 7/30/15.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.vqeg.viqet.BuildConfig;
import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.data.RemoteInfo;
import org.vqeg.viqet.data.RemoteInfoProvider;
import org.vqeg.viqet.data.Version;
import org.vqeg.viqet.logic.ResultBrowser;

/**
 * Home Fragment
 */
public class HomeFragment extends Fragment {

    private ResultBrowser resultBrowser;
    private ImageButton btn_start;
    private TextView tv_version;

    public HomeFragment() {   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(org.vqeg.viqet.R.layout.fragment_home, container, false);

        LinearLayout background=(LinearLayout) rootView.findViewById(org.vqeg.viqet.R.id.home_background_layout);

        MainActivity.mDrawerList.setItemChecked(0, true);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("VIQET");
        MainActivity.setupUI(background, getActivity());

        btn_start =(ImageButton) rootView.findViewById(org.vqeg.viqet.R.id.start_button);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
        tv_version =(TextView) rootView.findViewById(org.vqeg.viqet.R.id.tv_version);
        RemoteInfo remoteInfo = RemoteInfoProvider.getCopyRemoteInfo();
        if(remoteInfo != null) {
            try
            {
                String versionFromManifest = BuildConfig.VERSION_NAME;//getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
                String[] versionParts = versionFromManifest.split("\\.");
                String releaseVersion = versionParts[0];
                String androidAppVersion = versionParts[1];
                Version version = remoteInfo.getVersion();
                tv_version.setText("Preview Release "+releaseVersion + "." + version.getMethodologyVersion() + "." + version.getAlgoVersion() + "." + androidAppVersion);
            } catch (Exception e)
            {
                tv_version.setText("Version cannot be found");

            }
        }
        return rootView;
    }

    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(org.vqeg.viqet.R.string.addResultPopupMessage);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        builder.setView(input);
        builder.setPositiveButton(org.vqeg.viqet.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (!input.getText().toString().equals("")) {
                    resultBrowser = new ResultBrowser();
                    resultBrowser.addResult(input.getText().toString());

                    Fragment fragment = new PhotoCategoriesFragment();
                    Bundle args = new Bundle();
                    args.putInt("RESULT_INDEX", (resultBrowser.getResults().size()-1));
                    args.putInt("STEP_INDEX", 0);
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction().replace(org.vqeg.viqet.R.id.content_frame, fragment).commit();
                }
            }
        });
        builder.setNegativeButton(org.vqeg.viqet.R.string.cancel, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { } });
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}