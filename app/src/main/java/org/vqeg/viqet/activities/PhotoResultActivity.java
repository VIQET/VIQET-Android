/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.adapters.ResultPhotoDetailAdapter;
import org.vqeg.viqet.data.Category;
import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.logic.ResultBrowser;
import org.vqeg.viqet.logic.SelectPhotos;

import java.util.List;

public class PhotoResultActivity extends ActionBarActivity {
    private SelectPhotos selectPhotos;
    private List<Category> categoryList;
    private int resultIndex;
    private ExpandableListView elv_result_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_result);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        
        resultIndex = getIntent().getIntExtra("RESULT_INDEX", 0);

        selectPhotos = new SelectPhotos(resultIndex);
        categoryList = selectPhotos.getCategories();

        ResultBrowser resultBrowser= new ResultBrowser();
        String title= resultBrowser.getResults().get(resultIndex).getName();
        getSupportActionBar().setTitle(title);

        elv_result_photo = (ExpandableListView) findViewById(R.id.elv_results_photo_detail);
        ResultPhotoDetailAdapter adapter =new ResultPhotoDetailAdapter(PhotoResultActivity.this,categoryList,resultIndex);
        elv_result_photo.setAdapter(adapter);

        elv_result_photo.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                final Photo photo =categoryList.get(groupPosition).getPhotoList().get(childPosition);
                if(photo.getPhotoDetailMap() != null)
                {
                    Intent intent = new Intent();
                    intent.setClass(PhotoResultActivity.this, PhotoDetailActivity.class);
                    intent.putExtra("PHOTO_ID", photo);
                    intent.putExtra("RESULT_ID", resultIndex);
                    intent.putExtra("STEP_ID", groupPosition);
                    startActivity(intent);
                    return  true;
                }
                return false;
            }
        });
    }

}
