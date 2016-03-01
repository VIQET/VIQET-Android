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
public class FeatureDetail extends PhotoDetail implements Serializable
{

	// Standard Deviation
	private double standardDeviation;

	public double getStandardDeviation() 
	{
		return this.standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) 
	{
		this.standardDeviation = standardDeviation;
	}

	// Standard Error
	private double standardError;

	public double getStandardError() 
	{
		return this.standardError;
	}

	public void setStandardError(double standardError) 
	{
		this.standardError = standardError;
	}

	// Confidence Interval
	private double confidenceInterval95;

	public double getConfidenceInterval95() 
	{
		return this.confidenceInterval95;
	}

	public void setConfidenceInterval95(double confidenceInterval95) 
	{
		this.confidenceInterval95 = confidenceInterval95;
	}
}
