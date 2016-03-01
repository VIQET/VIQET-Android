/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

public class HelpFragment extends Fragment {
    private ImageView imageView;
    private int currentPage = 0;
    private Button next, previous;

    public HelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(org.vqeg.viqet.R.layout.fragment_help, container, false);
        WebView helpDoc = (WebView) rootView.findViewById(org.vqeg.viqet.R.id.webView);
        helpDoc.loadUrl("file:///android_asset/help.html");
//        helpDoc.setInitialScale(1);
        helpDoc.getSettings().setLoadWithOverviewMode(true);
        helpDoc.getSettings().setUseWideViewPort(true);

        return rootView;
    }

}
