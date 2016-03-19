/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.vqeg.viqet.BuildConfig;
import org.vqeg.viqet.data.RemoteInfo;
import org.vqeg.viqet.data.RemoteInfoProvider;
import org.vqeg.viqet.data.Version;

public class AboutFragment extends Fragment {

    private TextView tv_para1,tv_para2,tv_para3;
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(org.vqeg.viqet.R.layout.fragment_about, container, false);

        SpannableString ss = new SpannableString(getActivity().getResources().getString(org.vqeg.viqet.R.string.about_fragment_first_para));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.vqeg.org"));
                startActivity(browserIntent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 261, 273, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(getActivity().getResources().getColor(org.vqeg.viqet.R.color.about_page_link_color), 261, 273,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new UnderlineSpan(), 261, 273, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_para1 = (TextView) rootView.findViewById(org.vqeg.viqet.R.id.tv_about_first_para);
        tv_para1.setText(ss);
        tv_para1.setMovementMethod(LinkMovementMethod.getInstance());

        SpannableString ss1 = new SpannableString(getActivity().getResources().getString(org.vqeg.viqet.R.string.about_fragment_second_para));
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.eclipse.org/legal/epl-v10.html"));
                startActivity(browserIntent);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss1.setSpan(clickableSpan1, 172, 213, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(getActivity().getResources().getColor(org.vqeg.viqet.R.color.about_page_link_color), 172, 213,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss1.setSpan(new UnderlineSpan(), 172, 213, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_para2 = (TextView) rootView.findViewById(org.vqeg.viqet.R.id.tv_about_second_para);
        tv_para2.setText(ss1);
        tv_para2.setMovementMethod(LinkMovementMethod.getInstance());

        tv_para3= (TextView) rootView.findViewById(org.vqeg.viqet.R.id.tv_about_third_para);
        RemoteInfo remoteInfo = RemoteInfoProvider.getRemoteInfo();
        if(remoteInfo != null) {
            try {
                String versionFromManifest = BuildConfig.VERSION_NAME;//getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
//                String[] versionParts = versionFromManifest.split("\\.");
//                String releaseVersion = versionParts[0];
                String androidAppVersion = versionFromManifest;
                Version version = remoteInfo.getVersion();
                tv_para3.setText("Version: " + version.getReleaseVersion() + "." + version.getMethodologyVersion() + "." + version.getAlgoVersion() + "." + androidAppVersion);
            } catch (Exception e) {
                tv_para3.setText("Version cannot be found");
            }
        }
        return rootView;
    }

}
