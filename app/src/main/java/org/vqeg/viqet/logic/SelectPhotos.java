/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.vqeg.viqet.data.Category;
import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.data.Result;
import org.vqeg.viqet.data.ResultStore;
import org.vqeg.viqet.services.PhotoInspectorService;

import java.util.List;

public class SelectPhotos 
{
	private Result result;
	private int resultIndex;
	
	private static final String TAG = "Select Photos";

	public SelectPhotos(int resultIndex)
	{
		this.resultIndex = resultIndex;
		this.result = ResultStore.GetResultStore().getResults().get(resultIndex);
	}
	
	public List<Category> getCategories(){
		return result.getInputCategoryList();
	}
	
	public void InspectPhoto(Context context, int photoIndex)
	{
		//Set the photo as being processed
		try
		{		
			Intent startServiceIntent = new Intent();
			startServiceIntent.setClass(context, PhotoInspectorService.class);
			startServiceIntent.setAction(PhotoInspectorService.INSPECT_PHOTO_ACTION);
			startServiceIntent.putExtra(PhotoInspectorService.RESULT_INDEX, resultIndex);
			startServiceIntent.putExtra(PhotoInspectorService.PHOTO_INDEX, photoIndex);
			
			context.startService(startServiceIntent);
		}
		catch(Exception e){}
	}
	
	public void reInspectPhotos(Context context)
	{
		try
		{
			Intent startServiceIntent = new Intent();
			startServiceIntent.setClass(context, PhotoInspectorService.class);
			startServiceIntent.setAction(PhotoInspectorService.RE_INSPECT_PHOTO_ACTION);
			startServiceIntent.putExtra(PhotoInspectorService.RESULT_INDEX, resultIndex);
			
			context.startService(startServiceIntent);
		}
		catch(Exception e){}
	}
	
	public void updatePhoto(int photoIndex, String filename, String filepath)
	{
		try
		{
			Photo photo = result.getPhotoList().get(photoIndex);
			photo.clear();
			photo.setFilename(filename);
	    	photo.setFilePath(filepath);
	    	photo.setState(Photo.State.UNANALYZED);
	    	ResultStore.GetResultStore().persist();
		}
		catch(Exception e)
		{
			Log.e(TAG, "Error while updating photo", e);
		}
	}
	
	public int getPhotoIndex(Photo photo)
	{
		return this.result.getPhotoList().indexOf(photo);
	}
	
	public int getResultIndex()
	{
		return this.resultIndex;
	}
	
	public Result getResult()
	{	
		return this.result;
	}
	
	public void deletePhoto(Photo photo)
	{
		photo.clear();
		ResultStore.GetResultStore().persist();
	}
	
	public boolean isResultReady(Context context)
	{
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean allowIncompleteResult = sharedPref.getBoolean("allowIncompleteResult", false);
		
		List<Photo> photoList = getResult().getPhotoList();
		
		//if the first item is not complete, dont allow the results to be calculated
		if(photoList.get(0).getState() != Photo.State.ANALYSIS_COMPLETE)
		{
			return false;
		}
		
		//Check if all photo analysis are complete.
		for(Photo photo : photoList)
		{
			Photo.State state = photo.getState();

			if(state != Photo.State.ANALYSIS_COMPLETE)
			{
				//We make an exception for empty photos if incomplete results are allowed
				if(allowIncompleteResult)
				{
					if(state != Photo.State.NO_PHOTO)
					{
						return false;
					}
					else
					{
						//Do Nothing (If all pass, true will be returned)
					}
				}
				else
				{
					return false;
				}
			}
		}
		return true;
	}

}


