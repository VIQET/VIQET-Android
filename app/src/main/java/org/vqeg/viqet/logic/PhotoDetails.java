/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.logic;

import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.data.Visualization;
import org.vqeg.viqet.singlephotodata.SinglePhoto;

import java.util.List;

public class PhotoDetails 
{
	private Photo photo;
	private SinglePhoto singlePhoto;
	private int visualizationCount;
	
	public PhotoDetails(Photo photo)
	{
		this.photo = photo;
		this.visualizationCount = 0;
		
		List<Visualization> visualizationList = photo.getVisualizationList();
		if(visualizationList != null)
		{
			this.visualizationCount = visualizationList.size();
		}
	}
	public PhotoDetails(SinglePhoto singlePhoto)
	{
		this.singlePhoto = singlePhoto;
		this.visualizationCount = 0;

		List<Visualization> visualizationList = singlePhoto.getVisualizationList();
		if(visualizationList != null)
		{
			this.visualizationCount = visualizationList.size();
		}
	}
	public Photo getPhoto()
	{
		return this.photo;
	}
	public SinglePhoto getSinglePhoto()
	{
		return this.singlePhoto;
	}
}
