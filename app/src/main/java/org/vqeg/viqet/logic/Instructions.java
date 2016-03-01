/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.logic;

import android.content.Context;

import org.vqeg.viqet.data.InputCategoryInfo;
import org.vqeg.viqet.data.ResultStore;
import org.vqeg.viqet.data.ExamplePhoto;

import java.util.List;

public class Instructions 
{
	private InputCategoryInfo categoryInfo;
	private int photoIndex;
	
	public Instructions(String categoryName, int resultIndex, int photoIndex, Context context)
	{
		this.photoIndex = photoIndex;
		this.categoryInfo = ResultStore.GetResultStore().getResults().get(resultIndex).getRemoteInfo().getInputCategory(categoryName);
	}
	
	public int getPhotoIndex()
	{
		return this.photoIndex;
	}
	
	public String getTitle()
	{
		return this.categoryInfo.getName();
	}
	
	public String getDescription()
	{
		return this.categoryInfo.getDescription();
	}
	
	public List<ExamplePhoto> getExamplePhotos()
	{
		return this.categoryInfo.getExamplePhotoList();
	}
}
