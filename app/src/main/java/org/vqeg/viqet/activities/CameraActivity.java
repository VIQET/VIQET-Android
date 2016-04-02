/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import org.vqeg.viqet.utilities.FileHelper;

import java.io.File;

public class CameraActivity extends Activity
{
	static final int PHOTO_FROM_CAMERA=1;
	static final String TAG_NAME = "Camera Activity";
	private File photoFile = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		launchCamera();
	}
	
	private void launchCamera() 
	{
		int cameraId = 0;
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
		{
			Log.e(TAG_NAME, "No camera on this device");
		} 
		else 
		{
			cameraId = findBackFacingCamera();
			if (cameraId < 0) 
			{
				Log.e(TAG_NAME, "No back facing camera on this device");
			} 
			else 
			{
				dispatchTakePictureIntent();	
			}
		} 	
	}

	private void dispatchTakePictureIntent() 
	{
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) 
		{
			// Create the File where the photo should go
			photoFile = FileHelper.createUniqueFile(FileHelper.photoPrefix, FileHelper.photoDirectoryName);
			
			// Continue only if the File was successfully created
			if (photoFile != null) 
			{
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, PHOTO_FROM_CAMERA);
			}
		}
	}
	
	private int findBackFacingCamera() 
	{
 		int cameraId = -1;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i++) 
		{
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_BACK)
			{
				Log.d(TAG_NAME, "Camera found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
	{
		if (resultCode == RESULT_OK && requestCode == PHOTO_FROM_CAMERA) 
		{
			Intent returnIntent = new Intent();
			returnIntent.putExtra("FILE_NAME", this.photoFile.getName());
			returnIntent.putExtra("FILE_PATH", this.photoFile.getAbsolutePath());
			setResult(resultCode,returnIntent);
		}
		else
		{
			setResult(resultCode);	
		}
		finish();
	}

}
