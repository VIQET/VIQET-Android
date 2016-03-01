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
public class PhotoDetail implements Serializable
{
	//Parameter Name
	private String parameterName;

	public String getParameterName()
	{
		return this.parameterName;
	}

	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}
	
	//Value
	private double value;

	public double getValue() 
	{
		return this.value;
	}

	public void setValue(double value) 
	{
		this.value = value;
	}
	
	//Display Preference
	private String displayPreference;

	public String getDisplayPreference()
	{
		return this.displayPreference;
	}

	public void setDisplayPreference(String displayPreference)
	{
		this.displayPreference = displayPreference;
	}

}
