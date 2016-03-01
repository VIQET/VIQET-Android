/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.vqeg.viqet.utilities.PhotoDecoder;

import java.util.ArrayList;

/**
 * Created by rkalidin on 8/18/2015.
 */

public class ImageSliderPhotoDetailAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;

    // constructor
    public ImageSliderPhotoDetailAdapter(Activity activity,ArrayList<String> imagePaths) {
        this._activity = activity;
        this._imagePaths = imagePaths;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        LayoutInflater inflater = (LayoutInflater) _activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(org.vqeg.viqet.R.layout.fragment_photo_view_pager_help, container,
                false);
        imgDisplay = (ImageView) viewLayout.findViewById(org.vqeg.viqet.R.id.img_photo_visualization);
        String path=_imagePaths.get(position);
        Bitmap bitmap = PhotoDecoder.getThumbnail(path, 100, 100);
        imgDisplay.setImageBitmap(bitmap);
        container.addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}