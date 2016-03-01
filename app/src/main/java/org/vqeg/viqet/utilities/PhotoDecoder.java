/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PhotoDecoder 
{
	public static Bitmap getImageBitmap(String filePath)
	{
		return BitmapFactory.decodeFile(filePath);
	}
	
	public static Bitmap getThumbnail(String filePath, int width, int height)
	{
		//Fetch size info from file
		final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, width, height);
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    
	    return BitmapFactory.decodeFile(filePath, options);
	}

	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
	{
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;

	    if (height > reqHeight || width > reqWidth) 
	    {
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) 
	        {
	            inSampleSize *= 2;
	        }
	    }

	    return inSampleSize;
	}
	
}
