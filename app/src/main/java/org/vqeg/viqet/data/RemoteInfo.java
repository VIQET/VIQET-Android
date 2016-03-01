/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Set;

@SuppressWarnings("serial")
public class RemoteInfo implements Serializable
{
	//Version Number
	private Version version;
	public Version getVersion() 
	{
		return version;
	}
	public void setVersion(Version version) 
	{
		this.version = version;
	}

	//Input Categories
	private LinkedHashMap<String,InputCategoryInfo> inputCategories;
	public Set<String> getInputCategories()
	{
		return inputCategories.keySet();
	}
	public InputCategoryInfo getInputCategory(String categoryName)
	{
		return inputCategories.get(categoryName);
	}
	
	public void addInputCategories(InputCategoryInfo inputCategory) 
	{
		if(this.inputCategories == null)
		{
			this.inputCategories = new LinkedHashMap<String,InputCategoryInfo>();
		}
		this.inputCategories.put(inputCategory.getName(), inputCategory);
	}

	//Output Categories
	private LinkedHashMap<String,OutputCategoryInfo> outputCategories;
	public Set<String> getOutputCategories()
	{
		return outputCategories.keySet();
	}
	public OutputCategoryInfo getOutputCategory(String categoryName)
	{
		return outputCategories.get(categoryName);
	}
	public void addOutputCategories(OutputCategoryInfo outputCategory)
	{
		if(this.outputCategories == null)
		{
			this.outputCategories = new LinkedHashMap<String,OutputCategoryInfo>();
		}
		this.outputCategories.put(outputCategory.getName(), outputCategory);
	}
}
