/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;


import java.io.Serializable;

@SuppressWarnings("serial")
public class ExamplePhoto implements Serializable
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
	
	//Download Manager ID
	private long downloadManagerID;
	public long getDownloadManagerID()
	{
		return this.downloadManagerID;
	}
	public void setDownloadManagerID(long id)
	{
		this.downloadManagerID = id;
	}

	//File Name
	private String filename;
	public String getFileName()
	{
		return filename;
	}
	public void setFileName(String filename)
	{
		this.filename = filename;
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
	
	//Acceptable - Indicates if it is a good (To do) or bad (Do not do) example
	private boolean acceptable;
	public boolean isAcceptable() 
	{
		return acceptable;
	}
	public void setAcceptable(boolean acceptable) 
	{
		this.acceptable = acceptable;
	}
}
