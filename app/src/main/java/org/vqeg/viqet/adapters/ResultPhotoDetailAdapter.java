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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.asynctasks.ImageLoadTask;
import org.vqeg.viqet.data.Category;
import org.vqeg.viqet.data.FeatureDetail;
import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.logic.PhotoDetails;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkalidin on 11/16/2015.
 */
public class ResultPhotoDetailAdapter extends BaseExpandableListAdapter
{
    private Context mContext;
    private List<Category> photoList;

    public ResultPhotoDetailAdapter(Context c, List<Category> photoList, int resultIndex)
    {
        this.photoList=photoList;
        this.mContext=c;
    }

    @Override
    public Object getChild(int arg0, int arg1)
    {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int position, boolean isLastChild, View convertView, ViewGroup parent)
    {

        final Photo photoItem= photoList.get(groupPosition).getPhotoList().get(position);
        ViewHolderImageAdapter viewHolder;

            LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.result_photo_detail_child_item, parent, false);

            viewHolder = new ViewHolderImageAdapter();

            //Set ImageLoadTask - If the photo isn't set use the placeholder image from the resources
            viewHolder.photoImageView = (ImageView) convertView.findViewById(R.id.childPhotoImage);
            viewHolder.statusText = (TextView) convertView.findViewById(R.id.photoMOSScore);
            viewHolder.image_name = (TextView) convertView.findViewById(R.id.child_image_name);

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
        PhotoDetails photoDetails =new PhotoDetails(photoItem);
        List<PhotoDetail> photoDetailList = new ArrayList<>(photoDetails.getPhoto().getPhotoDetailMap().values());
        double photo_mos=0;
        for(PhotoDetail featureDetail : photoDetailList){
            if(featureDetail.getParameterName().equals("MOS")){
                photo_mos=featureDetail.getValue();
                break;
            }
        }
        DecimalFormat df = new DecimalFormat("#0.0");
        viewHolder.image_name.setText(""+photoItem.getFilename());
        viewHolder.statusText.setText("MOS Score: "+df.format(photo_mos));

        return convertView;
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return MainActivity.mMemoryCache.get(key);
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return photoList.get(groupPosition).getPhotoList().size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public int getGroupCount()
    {
        return photoList.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.result_view_category_item, parent, false);

        Category category = photoList.get(groupPosition);

        FeatureDetail mosFeature = null;
        List<FeatureDetail> features= category.FeatureDetails();
        for(int i= 0; i<features.size();i++){
            if(features.get(i).getParameterName().equalsIgnoreCase("MOS")){
                mosFeature = features.get(i);
                break;
            }
        }

        if(mosFeature == null){
            ImageView group_icon = (ImageView) view.findViewById(R.id.group_icon);
            int imageResourceId = isExpanded ? R.drawable.forward_arrow
                    : R.drawable.down_arrow;
            group_icon.setImageResource(imageResourceId);

            //Set Feature Value
            TextView CategoryMOStextView = (TextView) view.findViewById(R.id.categoryMOS);
            CategoryMOStextView.setText("MOS Not Found");

            TextView CategoryMOSVariabilitytextView = (TextView) view.findViewById(R.id.categoryMOSVariability);
            CategoryMOSVariabilitytextView.setText("Variability: Not Found");
        }
        else {
            ImageView group_icon = (ImageView) view.findViewById(R.id.group_icon);
            int imageResourceId = isExpanded ? R.drawable.forward_arrow
                    : R.drawable.down_arrow;
            group_icon.setImageResource(imageResourceId);

            DecimalFormat df = new DecimalFormat("#0.0");
            //Set Feature Value
            TextView CategoryMOStextView = (TextView) view.findViewById(R.id.categoryMOS);
            CategoryMOStextView.setText(df.format(mosFeature.getValue()));

            TextView CategoryMOSVariabilitytextView = (TextView) view.findViewById(R.id.categoryMOSVariability);
            CategoryMOSVariabilitytextView.setText("Variability: "+df.format(mosFeature.getStandardError()));
        }
        //Set Category Name
        TextView categoryNameTextView = (TextView) view.findViewById(R.id.categoryName);
        categoryNameTextView.setText(category.getName());

        return view;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1)
    {
        return true;
    }

}
