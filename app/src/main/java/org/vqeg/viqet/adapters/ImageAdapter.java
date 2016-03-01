/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.asynctasks.ImageLoadTask;
import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.logic.PhotoDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Kalidindi on 7/30/15.
 */

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<Photo> photoList;
    private int resultIndex;
    public ImageAdapter(Context c, List<Photo> photoList, int resultIndex) {
        mContext = c;
        this.photoList=photoList;
        this.resultIndex=resultIndex;
    }

    public int getCount() {
        return photoList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        final Photo photoItem= photoList.get(position);
        ViewHolderImageAdapter viewHolder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(org.vqeg.viqet.R.layout.select_photos_photo_item, parent, false);

            viewHolder = new ViewHolderImageAdapter();

            //Set ImageLoadTask - If the photo isn't set use the placeholder image from the resources
            viewHolder.photoImageView = (ImageView) convertView.findViewById(org.vqeg.viqet.R.id.photoImage);
            viewHolder.statusText = (TextView) convertView.findViewById(org.vqeg.viqet.R.id.tv_status_upload_image);
            viewHolder.image_name = (TextView) convertView.findViewById(org.vqeg.viqet.R.id.image_name);
            viewHolder.mosRatingBar = (RatingBar) convertView.findViewById(org.vqeg.viqet.R.id.singlePhotoMOSRatingBar);
            viewHolder.busyRing = (ProgressBar) convertView.findViewById(org.vqeg.viqet.R.id.status_ring);
            viewHolder.statusImage =(ImageView) convertView.findViewById(org.vqeg.viqet.R.id.tick);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderImageAdapter)convertView.getTag();
        }
        Bitmap b= null;
        if(!(photoItem.getFilePath()==null)){
            if(MainActivity.mMemoryCache!=null){
                b= getBitmapFromMemCache(photoItem.getFilePath());

            }
            if(b==null){
                new ImageLoadTask(viewHolder.photoImageView, photoItem.getFilePath()).execute();
            }else{
                viewHolder.photoImageView.setImageBitmap(b);
            }
        }
        viewHolder.mosRatingBar.setVisibility(View.INVISIBLE);
        viewHolder.image_name.setVisibility(View.INVISIBLE);
        switch(photoItem.getState())
        {
            case UNANALYZED:
                viewHolder.statusText.setText("");
                break;
            case NO_PHOTO:
                viewHolder.statusImage.setVisibility(View.INVISIBLE);
                viewHolder.busyRing.setVisibility(View.INVISIBLE);
                viewHolder.statusText.setText("Please add photo");
                viewHolder.statusText.setTextColor(Color.WHITE);
                break;
            case UPLOAD_STARTED:
                viewHolder.busyRing.setVisibility(View.VISIBLE);
                viewHolder.statusText.setText(org.vqeg.viqet.R.string.uploading);
                viewHolder.statusText.setTextColor(Color.GREEN);
                break;
            case ANALYSIS_STARTED:
                viewHolder.statusText.setText(org.vqeg.viqet.R.string.checking);
                viewHolder.statusText.setTextColor(Color.GREEN);
                break;
            case UPLOAD_FAILED:
                viewHolder.statusText.setText("Upload Failed");
                viewHolder.statusText.setTextColor(Color.RED);
                break;
            case ANALYSIS_FAILED:
                ConnectivityManager cm =
                        (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if(isConnected){
                    viewHolder.statusText.setText("Analysis Failed");
                    viewHolder.statusText.setTextColor(Color.RED);
                }
                else{
                    viewHolder.statusText.setText("No network connection");
                    viewHolder.statusText.setTextColor(Color.RED);
                }
                break;
            case ANALYSIS_INPROGRESS:
                viewHolder.statusText.setText(org.vqeg.viqet.R.string.processing);
                viewHolder.statusText.setTextColor(Color.GREEN);
                viewHolder.statusText.setVisibility(TextView.VISIBLE);
                break;
            case ANALYSIS_COMPLETE:
                viewHolder.statusImage.setImageResource(org.vqeg.viqet.R.drawable.acceptable_example);
                viewHolder.statusImage.setVisibility(View.VISIBLE);
                viewHolder.busyRing.setVisibility(View.INVISIBLE);
                PhotoDetails photoDetails =new PhotoDetails(photoItem);
                List<PhotoDetail> photoDetailList = new ArrayList<>(photoDetails.getPhoto().getPhotoDetailMap().values());
                double photo_mos=0;
                for(PhotoDetail featureDetail : photoDetailList){
                    if(featureDetail.getParameterName().equals("MOS")){
                        photo_mos=featureDetail.getValue();
                        break;
                    }
                }
                viewHolder.mosRatingBar.setVisibility(View.VISIBLE);
                viewHolder.mosRatingBar.setRating((float)photo_mos);
                viewHolder.image_name.setVisibility(View.VISIBLE);
                viewHolder.image_name.setText(""+photoItem.getFilename());
                viewHolder.statusText.setVisibility(TextView.INVISIBLE);
                break;
            case UPLOAD_COMPLETE:
                viewHolder.statusText.setText("Upload Complete");
                viewHolder.statusText.setTextColor(Color.GREEN);
                break;
        }
        return convertView;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return MainActivity.mMemoryCache.get(key);
    }
}