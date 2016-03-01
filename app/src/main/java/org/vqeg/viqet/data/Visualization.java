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
public class Visualization implements Serializable
{
	
	//Visualization Name
	private String visualizationName;
	public String getVisualizationName()
	{
		return this.visualizationName;
	}
	public void setVisualizationName(String visualizationName)
	{
		this.visualizationName = visualizationName;
	}
	
	//File Path
	private String filePath;
	public String getFilePath()
	{
		return this.filePath;
	}
	public void setFilePath(String filePath)
	{
		this.filePath = filePath;
	}
	

}
