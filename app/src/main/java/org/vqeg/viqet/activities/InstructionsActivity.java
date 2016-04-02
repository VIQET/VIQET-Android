/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.data.ExamplePhoto;
import org.vqeg.viqet.logic.Instructions;

public class InstructionsActivity extends ActionBarActivity
{
	private Instructions instructions;
	static final int PHOTO_FROM_CAMERA=1;
	static final int PHOTO_FROM_GALLERY=2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.instructions);

		if(getResources().getBoolean(R.bool.portrait_only)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}

		getSupportActionBar().setTitle(getResources().getString(R.string.instructionsTitle));

		// Get data from intent
		int resultIndex = getIntent().getIntExtra("RESULT_INDEX", 0);
		int photoIndex = getIntent().getIntExtra("PHOTO_INDEX", 0);
		String category = getIntent().getStringExtra("CATEGORY");
		if(category != null)
		{
			this.instructions = new Instructions(category, resultIndex, photoIndex, this.getApplicationContext());
		}
		
		//Title
		TextView titleTextView = (TextView) findViewById(R.id.categoryNameHeading);
		titleTextView.setText(this.instructions.getTitle());
		
		//Description
		TextView descriptionTextView = (TextView) findViewById(R.id.categoryDescription);
		descriptionTextView.setText(this.instructions.getDescription());
		
		//Example Photos
		LinearLayout examplePhotosLinearLayout = (LinearLayout) findViewById(R.id.examplePhotos);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (final ExamplePhoto examplePhoto : this.instructions.getExamplePhotos())
		{
			View examplePhotoView = inflater.inflate(R.layout.instructions_example_photo_item, examplePhotosLinearLayout, false);
			
			//Set the image for the example photo
			ImageView examplePhotoImage = (ImageView) examplePhotoView.findViewById(R.id.examplePhoto);
			examplePhotoImage.setImageURI(Uri.parse(examplePhoto.getFileName()));
			examplePhotosLinearLayout.addView(examplePhotoView);
		}
		
		// Camera Button
		Button launchCameraButton = (Button) findViewById(R.id.launchCamera);
		launchCameraButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(InstructionsActivity.this, CameraActivity.class);
				startActivityForResult(intent, PHOTO_FROM_CAMERA);
			}
		});
		
		// Gallery Button
		Button launchGalleryButton = (Button) findViewById(R.id.launchGallery);
		launchGalleryButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{	
				Intent intent = new Intent();
				intent.setClass(InstructionsActivity.this, GalleryActivity.class);
				startActivityForResult(intent, PHOTO_FROM_GALLERY);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
	{
		if(requestCode == PHOTO_FROM_CAMERA || requestCode == PHOTO_FROM_GALLERY)
		{
			if(resultCode == RESULT_OK) 
			{ 
				String filename = imageReturnedIntent.getStringExtra("FILE_NAME");
				String filepath = imageReturnedIntent.getStringExtra("FILE_PATH");
				returnResult(filename, filepath, this.instructions.getPhotoIndex());
			}
		}
	}
	
	private void returnResult(String filename, String filepath, int photoIndex)
	{
		Intent returnIntent = new Intent();
		returnIntent.putExtra("PHOTO_INDEX", photoIndex);
		returnIntent.putExtra("FILE_NAME", filename);
		returnIntent.putExtra("FILE_PATH", filepath);
		setResult(RESULT_OK,returnIntent);
		finish();
	}
}
