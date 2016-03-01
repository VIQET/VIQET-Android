/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import org.vqeg.viqet.alarms.PhotoInspectorAlarm;
import org.vqeg.viqet.cloud.CloudCommunicator;
import org.vqeg.viqet.cloud.Json.ExamplePhotoResponse;
import org.vqeg.viqet.cloud.FileTransfer;
import org.vqeg.viqet.cloud.Json.InputCategoryResponse;
import org.vqeg.viqet.cloud.Json.MethodologyResponse;
import org.vqeg.viqet.cloud.Json.OutputCategoryResponse;
import org.vqeg.viqet.cloud.Json.PhotoDetailResponse;
import org.vqeg.viqet.cloud.Json.PhotoResponse;
import org.vqeg.viqet.cloud.Json.SASResponse;
import org.vqeg.viqet.cloud.Json.VersionResponse;
import org.vqeg.viqet.cloud.Json.VisualizationImagesResponse;
import org.vqeg.viqet.data.ExamplePhoto;
import org.vqeg.viqet.data.InputCategoryInfo;
import org.vqeg.viqet.data.OutputCategoryInfo;
import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.data.RemoteInfo;
import org.vqeg.viqet.data.RemoteInfoProvider;
import org.vqeg.viqet.data.Result;
import org.vqeg.viqet.data.ResultStore;
import org.vqeg.viqet.data.SharedAccessSignature;
import org.vqeg.viqet.data.Version;
import org.vqeg.viqet.data.Visualization;
import org.vqeg.viqet.utilities.FileHelper;

import java.util.ArrayList;
import java.util.List;


public class PhotoInspectorService extends IntentService
{
	private static final String TAG = "PhotoInspectorService";
    public static final String BROADCAST_PHOTO_STATUS_CHANGE = "org.vqeg.viqet.broadcast.photoUpdated";
    public static final String BROADCAST_METHODOLOGY_FETCHED = "org.vqeg.viqet.broadcast.methodologyFetched";
    public static final String INSPECT_PHOTO_ACTION = "INSPECT_PHOTO_ACTION";
	public static final String RE_INSPECT_PHOTO_ACTION = "RE_INSPECT_PHOTO_ACTION";
	public static final String INSPECT_ALL_PENDING_PHOTOS_ACTION = "INSPECT_ALL_PENDING_PHOTOS_ACTION";
    public static final String FETCH_METHODOLOGY_ACTION = "FETCH_METHODOLOGY_ACTION";
    public static final String FETCH_VERSION_MODEL_CHANGED_ACTION = "FETCH_VERSION_MODEL_CHANGED_ACTION";
    public static final String FETCH_VERSION_ACTION = "FETCH_VERSION_ACTION";
    public static final String RESULT_INDEX = "RESULT_INDEX";
	public static final String PHOTO_INDEX = "PHOTO_INDEX";
    public static final String SUCCESS = "SUCCESS";
	
	public PhotoInspectorService() 
	{
		super(TAG);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {

        int resultIndex, photoIndex;

        switch(intent.getAction())
        {
            case INSPECT_PHOTO_ACTION:
                resultIndex = intent.getIntExtra(RESULT_INDEX, -1);
                photoIndex = intent.getIntExtra(PHOTO_INDEX, -1);
                inspectPhoto(resultIndex, photoIndex);
                break;
            case RE_INSPECT_PHOTO_ACTION:
                resultIndex = intent.getIntExtra(RESULT_INDEX, -1);
                reInspectPhotos(resultIndex);
                break;
            case INSPECT_ALL_PENDING_PHOTOS_ACTION:
                inspectAllPendingPhotos();
                break;
            case FETCH_METHODOLOGY_ACTION:
                fetchVersion();
                break;
            case FETCH_VERSION_ACTION:
                fetchVersion();
                break;
            case FETCH_VERSION_MODEL_CHANGED_ACTION:
                fetchVersionWhenModelChanged();
                break;
        }
	}
	
	private void inspectPhoto(int resultIndex, int photoIndex)
	{
		if( resultIndex >= 0 && photoIndex >=0)
		{
			Photo photo = ResultStore.GetResultStore().getResults().get(resultIndex).getPhotoList().get(photoIndex);

            //If the photo is not uploaded, upload the photo
            if((photo.getState() == Photo.State.UNANALYZED) || (photo.getState() == Photo.State.UPLOAD_FAILED))
			{
                photo.setState(Photo.State.UPLOAD_STARTED);
                broadcastPhotoStatusChange(resultIndex, photoIndex, this.getApplicationContext());

                    //Fetch SAS URL from SERVER
                    SharedAccessSignature sharedAccessSignature = FetchSAS();
                    if(sharedAccessSignature != null)
                    {
                        photo.setContainerName(sharedAccessSignature.getContainerName());
                        photo.setBlobName(sharedAccessSignature.getBlobName());

                        if(FileTransfer.UploadFile(photo.getFilePath(), sharedAccessSignature.getSasURL(), this.getApplicationContext()))
                        {
                            Log.i(this.getClass().getSimpleName(), "Upload Complete - Photo No " + photoIndex);
                            photo.setState(Photo.State.UPLOAD_COMPLETE);
                        }
                        else
                        {
                            Log.i(this.getClass().getSimpleName(), "Upload Failed (While uploading to blob storage) - Photo No " + photoIndex);
                            photo.setState(Photo.State.UPLOAD_FAILED);
                        }
                    }
                    else
                    {
                        Log.i(this.getClass().getSimpleName(), "Upload Failed (No SAS url returned by server) - Photo No " + photoIndex);
                        photo.setState(Photo.State.UPLOAD_FAILED);
                    }

                ResultStore.GetResultStore().persist();
                broadcastPhotoStatusChange(resultIndex, photoIndex, this.getApplicationContext());
            }


            //If the photo has been uploaded, ask the server to analyze the photo
			if((photo.getState() == Photo.State.UPLOAD_COMPLETE)|| (photo.getState() == Photo.State.ANALYSIS_FAILED) || (photo.getState() == Photo.State.ANALYSIS_INPROGRESS))
			{
                photo.setState(Photo.State.ANALYSIS_STARTED);
                broadcastPhotoStatusChange(resultIndex, photoIndex, this.getApplicationContext());

                PhotoResponse photoResponse = CloudCommunicator.FetchPhotoDetails(photo.getBlobName(), photo.getContainerName(), photo.getInputCategory(), this.getApplicationContext());
                if(photoResponse != null)
                {
                    if(photoResponse.getErrorCode() == PhotoResponse.ErrorCodes.Success)
                    {
                        Version cloudVersion = new Version();
                        cloudVersion.setVersionFromString(photoResponse.getVersionString());
                        photo.setCloudVersion(cloudVersion);

                        //Add Photo Details
                        for (PhotoDetailResponse photoDetailResponse : photoResponse.getPhotoDetails()) {
                            PhotoDetail photoDetail = new PhotoDetail();
                            photoDetail.setParameterName(photoDetailResponse.getParameterName());
                            photoDetail.setDisplayPreference(photoDetailResponse.getDisplayPreference());
                            photoDetail.setValue(photoDetailResponse.getValue());

                            photo.addPhotoDetail(photoDetail.getParameterName(), photoDetail);
                        }

                        //Add Visualizations
                        boolean visualizationFailed = false;
                        for (VisualizationImagesResponse visualizationImageResponse : photoResponse.getVisualizationImages()) {

                            //Download Visualization Photos
                            String sasURL = visualizationImageResponse.getVisualization();

                            //String downloadFileName = photo.getFilename() + "_" + visualizationImageResponse.getVisualization();
                            String downloadedFilepath = FileTransfer.Download(FileHelper.visualizationPrefix, FileHelper.photoDirectoryName, sasURL, this.getApplicationContext());

                            if (downloadedFilepath != null)
                            {
                                //Add Visualization ImageLoadTask to Photo
                                Visualization visualization = new Visualization();
                                visualization.setVisualizationName(visualizationImageResponse.getVisualization());
                                visualization.setFilePath(downloadedFilepath);
                                photo.addVisualization(visualization);
                            }
                            else
                            {
                                //flag analysis as failed so the photos are downloaded again (Could instead use a placeholder photo)
                                visualizationFailed = true;
                                Log.e(this.getClass().getSimpleName(),"Visualization file could not be downloaded");
                            }
                        }
                        if(visualizationFailed)
                        {
                            photo.setState(Photo.State.ANALYSIS_FAILED);
                        }
                        else
                        {
                            photo.setState(Photo.State.ANALYSIS_COMPLETE);
                        }

                    }
                    else if (photoResponse.getErrorCode() == PhotoResponse.ErrorCodes.PhotoQueued)
                    {
                        photo.setState(Photo.State.ANALYSIS_INPROGRESS);
                    }
                    else if (photoResponse.getErrorCode() == PhotoResponse.ErrorCodes.MissingBlob)
                    {
                        photo.setState(Photo.State.UPLOAD_FAILED);
                    }
                    else if (photoResponse.getErrorCode() == PhotoResponse.ErrorCodes.OtherError)
                    {
                        photo.setState(Photo.State.ANALYSIS_FAILED);
                    }
                }
                else
                {
                    photo.setState(Photo.State.ANALYSIS_FAILED);
                }

                ResultStore.GetResultStore().persist();
                broadcastPhotoStatusChange(resultIndex, photoIndex, this.getApplicationContext());

			}
		}		
	}
	
	private void inspectAllPendingPhotos()
	{		
		List<Result> resultList = ResultStore.GetResultStore().getResults();
		for(int resultIndex = 0; resultIndex < resultList.size(); resultIndex++)
		{
			List<Photo> photoList = resultList.get(resultIndex).getPhotoList();
			for(int photoIndex = 0; photoIndex < photoList.size(); photoIndex++)
			{
				Photo photo = photoList.get(photoIndex);
				if(isIncompleteState(photo.getState()))
				{
					inspectPhoto(resultIndex, photoIndex);
				}
			}
		}
	}
	
	private void reInspectPhotos(int resultIndex)
	{
		for(Photo photo : ResultStore.GetResultStore().getResults().get(resultIndex).getPhotoList())
		{
			if(photo.getState() != Photo.State.NO_PHOTO )
			{
				photo.setState(Photo.State.UPLOAD_COMPLETE);
			}
		}
        ResultStore.GetResultStore().persist();
		inspectAllPendingPhotos();
	}
	
	private boolean isIncompleteState(Photo.State state)
	{
		if(state == Photo.State.NO_PHOTO || state == Photo.State.ANALYSIS_COMPLETE)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public static void broadcastPhotoStatusChange(int resultIndex, int photoIndex, Context context)
	{
		Intent broadcastIntent = new Intent();
		broadcastIntent.putExtra(RESULT_INDEX, resultIndex);
		broadcastIntent.putExtra(PHOTO_INDEX, photoIndex);
		broadcastIntent.setAction(BROADCAST_PHOTO_STATUS_CHANGE);
		context.sendBroadcast(broadcastIntent);
	}

    public static void broadcastMethodologyFetched(boolean success, Context context)
    {
        Intent broadcastIntent = new Intent();
        broadcastIntent.putExtra(SUCCESS, success);
        broadcastIntent.setAction(BROADCAST_METHODOLOGY_FETCHED);
        context.sendBroadcast(broadcastIntent);
    }

    private SharedAccessSignature FetchSAS()
    {
        SASResponse sasResponse = CloudCommunicator.FetchUploadSAS(this.getApplicationContext());
        if(sasResponse != null)
        {
            SharedAccessSignature sharedAccessSignature = new SharedAccessSignature();
            sharedAccessSignature.setContainerName(sasResponse.getContainerName());
            sharedAccessSignature.setBlobName(sasResponse.getBlobName());
            sharedAccessSignature.setSasURL(sasResponse.getSASURL());
            return sharedAccessSignature;
        }
        else
        {
            return null;
        }
    }

    private void fetchVersion()
    {
        VersionResponse versionResponse = CloudCommunicator.FetchVersion(this.getApplicationContext());
        if(versionResponse != null)
        {
            //fetchVersion should never be called before fetch Methodology.
            //Just in case, we check to make sure fetchMethodology is called
            RemoteInfo remoteInfo = RemoteInfoProvider.getRemoteInfo();
            if(remoteInfo == null)
            {
                fetchMethodology(versionResponse);
                remoteInfo = RemoteInfoProvider.getRemoteInfo();
            }
            Version version = new Version();
            version.setVersionFromString(versionResponse.getVersionString());
            remoteInfo.setVersion(version);
        }
    }

    private void fetchVersionWhenModelChanged()
    {
        VersionResponse versionResponse = CloudCommunicator.FetchVersion(this.getApplicationContext());
        if(versionResponse != null)
        {
            fetchMethodology(versionResponse);
            RemoteInfo remoteInfo = RemoteInfoProvider.getRemoteInfo();
            Version version = new Version();
            version.setVersionFromString(versionResponse.getVersionString());
            remoteInfo.setVersion(version);
        }
    }

    private void fetchMethodology(VersionResponse versionResponse)
    {
        MethodologyResponse methodologyResponse = CloudCommunicator.FetchMethodology(this.getApplicationContext());
        if(methodologyResponse != null)
        {
            RemoteInfo remoteInfo = new RemoteInfo();

            Version version = new Version();
            version.setVersionFromString(versionResponse.getVersionString());
            remoteInfo.setVersion(version);

            InputCategoryResponse[] inputCategoryResponseArray = methodologyResponse.getInputCategoryResponseArray();
            if(inputCategoryResponseArray != null) {

                for (InputCategoryResponse inputCategoryResponse : inputCategoryResponseArray) {

                    //Add Input Categories
                    InputCategoryInfo inputCategoryInfo = new InputCategoryInfo();

                    //Add the input category name
                    inputCategoryInfo.setName(inputCategoryResponse.getName());

                    //Add required photo count
                    inputCategoryInfo.setRequiredPhotoCount(inputCategoryResponse.getRequiredPhotoCount());

                    //Add each output Category Name
                    String[] outputCategoryNameArray = inputCategoryResponse.getOutputCategoryNameArray();
                    if (outputCategoryNameArray != null) ;
                    {
                        List<String> outputCategories = new ArrayList<>();
                        for (String outputCategoryName : outputCategoryNameArray) {
                            outputCategories.add(outputCategoryName);
                        }
                        inputCategoryInfo.setOutputCategories(outputCategories);
                    }

                    //Add description
                    String inputCategoryDescription = inputCategoryResponse.getDescription();
                    inputCategoryInfo.setDescription(inputCategoryDescription);

                    //Add examplePhotoList
                    ExamplePhotoResponse[] examplePhotoResponseArray = inputCategoryResponse.getExamplePhotoResponseArray();
                    if (examplePhotoResponseArray != null) {
                        for (ExamplePhotoResponse examplePhotoResponse : examplePhotoResponseArray) {
                            //Download Example Photo
                            String url = methodologyResponse.getImageFilesPath() + "/" + examplePhotoResponse.getBlobFileName()+".jpg";
                            String downloadedFilepath = FileTransfer.Download(FileHelper.methodologyPhotoPrefix, FileHelper.methodologyDirectoryName, url, this.getApplicationContext());
                            if (downloadedFilepath != null)
                            {
                                ExamplePhoto examplePhoto = new ExamplePhoto();
                                examplePhoto.setAcceptable(examplePhotoResponse.isAcceptable());
                                examplePhoto.setFileName(downloadedFilepath);
                                examplePhoto.setDescription(examplePhotoResponse.getDescription());
                                examplePhoto.setName(examplePhotoResponse.getName());

                                inputCategoryInfo.addExamplePhoto(examplePhoto);
                            }
                            else
                            {
                                Log.e(this.getClass().getSimpleName(),"Example Photo could not be downloaded");
                                broadcastMethodologyFetched(false, this.getApplicationContext());
                                return;
                            }
                        }
                    }

                    remoteInfo.addInputCategories(inputCategoryInfo);
                }

                //Add each output category
                OutputCategoryResponse[] outputCategoryResponseArray = methodologyResponse.getOutputCategoryResponseArray();
                if(outputCategoryResponseArray != null);
                for(OutputCategoryResponse outputCategoryResponse : outputCategoryResponseArray)
                {
                    OutputCategoryInfo outputCategoryInfo = new OutputCategoryInfo();
                    outputCategoryInfo.setName(outputCategoryResponse.getName());
                    outputCategoryInfo.setDescription(outputCategoryResponse.getDescription());
                    remoteInfo.addOutputCategories(outputCategoryInfo);
                }
            }

            //Here is the remote info and broadcast message that the remote info is fetched
            RemoteInfoProvider.setRemoteInfo(remoteInfo);
            broadcastMethodologyFetched(true, this.getApplicationContext());

        }
        else
        {
            //Broadcast info that remote info could not be found
            broadcastMethodologyFetched(false, this.getApplicationContext());
        }
    }

    @Override
	public void onDestroy() 
	{
		boolean incompletePhotosPresent = false;
		
		//Check for incomplete Photos
		for(Result result : ResultStore.GetResultStore().getResults())
		{
			for(Photo photo : result.getPhotoList())
			{
				if(isIncompleteState(photo.getState()))
				{
					incompletePhotosPresent = true;
					break;
				}
			}
			if(incompletePhotosPresent)
			{
				break;
			}
		}
		
		Log.i(TAG, "Check if we need to set alarm");
		//Start Timer if there are incomplete photos
		if(incompletePhotosPresent)
		{
			PhotoInspectorAlarm.setAlarm(this, 15);
		}
		
		Log.i(TAG, "Service Destroyed");
		super.onDestroy();
	}
}

