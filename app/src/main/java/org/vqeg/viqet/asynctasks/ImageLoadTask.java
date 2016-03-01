/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.utilities.PhotoDecoder;

/**
 * Created by rkalidin on 11/16/2015.
 */

// ASYNC TASK TO AVOID CHOKING UP UI THREAD
public class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

    private ImageView imgView;
    private String filePath;

    public ImageLoadTask(ImageView imgView, String filePath){
        this.imgView=imgView;
        this.filePath=filePath;
    }

    @Override
    protected void onPreExecute() {
        Log.i("ImageLoadTask", "Loading image...");
    }

    protected Bitmap doInBackground(String... param) {
        try {
            Bitmap b = PhotoDecoder.getThumbnail(filePath, 100, 100);
            addBitmapToMemoryCache(filePath,b);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Bitmap ret) {
        if (ret != null) {
            imgView.setImageBitmap(ret);
        } else {
            Log.e("ImageLoadTask", "Failed to load " + filePath + " image");
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            MainActivity.mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return MainActivity.mMemoryCache.get(key);
    }
}