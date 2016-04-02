/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.vqeg.viqet.R;

import java.io.File;


public class PhotoPreviewActivity extends ActionBarActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_preview);
		//Fetch photo from intent
		Intent intent = getIntent();
		String title = intent.getStringExtra("TITLE");
		String filePath = intent.getStringExtra("FILE_PATH");
		//Set title
		getSupportActionBar().setTitle(title);
	    //Set photo to webview
	    File file = new File(filePath);
	    String url = file.toURI().toString();
	    WebView webView = (WebView) findViewById(R.id.webView);
	    webView.loadUrl(url);
	    webView.setBackgroundColor(Color.BLACK);
	    WebSettings settings = webView.getSettings();
	    settings.setBuiltInZoomControls(true);
	    settings.setDisplayZoomControls(false);
	    settings.setUseWideViewPort(true);
	    settings.setLoadWithOverviewMode(true);
	}
}