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
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import org.vqeg.viqet.utilities.FileHelper;

import java.io.File;
import java.io.IOException;

public class GalleryActivity extends Activity
{
	static final int SELECT_IMAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, SELECT_IMAGE);  
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent)
	{
		if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE && imageReturnedIntent !=null && imageReturnedIntent.getData() != null)
		{
            Uri selectedImage = imageReturnedIntent.getData();
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String galleryFilePath = cursor.getString(columnIndex);
			cursor.close();

			File localStorageFile = null;
			try
			{
				localStorageFile = FileHelper.createUniqueFile(FileHelper.photoPrefix, FileHelper.photoDirectoryName);
				File galleryFile = new File(galleryFilePath);
				FileHelper.copy(galleryFile,localStorageFile);
			}
			catch(IOException exception)
			{
				Log.e("Gallery", exception.getMessage());
			}

			Intent returnIntent = new Intent();
			returnIntent.putExtra("FILE_NAME", localStorageFile.getName());
			returnIntent.putExtra("FILE_PATH", localStorageFile.getAbsolutePath());

			setResult(RESULT_OK,returnIntent);
		}
		else
		{
			setResult(RESULT_CANCELED);
		}

		finish();
	}
}
