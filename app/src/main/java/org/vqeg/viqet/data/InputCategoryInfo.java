/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class InputCategoryInfo implements Serializable
{
	//Name
	private String name;
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}

	//Description
	private String description;
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}

	//Example Photos
	private List<ExamplePhoto> examplePhotoList;
	
	public List<ExamplePhoto> getExamplePhotoList()
	{
		return examplePhotoList;
	}
	public void addExamplePhoto(ExamplePhoto examplePhoto) 
	{
		if(this.examplePhotoList == null)
		{
			this.examplePhotoList = new ArrayList<ExamplePhoto>();
		}
		this.examplePhotoList.add(examplePhoto);
	}

	//Required number of photos
	private int requiredPhotoCount;
	public int getRequiredPhotoCount() 
	{
		return requiredPhotoCount;
	}
	public void setRequiredPhotoCount(int requiredPhotoCount) 
	{
		this.requiredPhotoCount = requiredPhotoCount;
	}

	//Output Categories
	private List<String> outputCategories;
	public List<String> getOutputCategories()
	{
		return outputCategories;
	}
	public void setOutputCategories(List<String> outputCategories)
	{
		this.outputCategories = outputCategories;
	}


}
