/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.logic;

import org.vqeg.viqet.data.Category;
import org.vqeg.viqet.data.FeatureDetail;
import org.vqeg.viqet.data.Result;

import java.util.List;

public class ResultView 
{
	private Result result;

	public ResultView(Result result)
	{
		this.result = result;
	}
	
	public List<Category> getCategoryList()
	{
		if(this.result != null)
		{
			return this.result.getOutputCategoryList();
		}
		else
		{
			return null;
		}
	}
	
	public FeatureDetail getOverallMOS()
	{
		List<FeatureDetail> aggregatedDetails = this.result.OverallDetails();
		
		if(aggregatedDetails != null)
		{
			for(FeatureDetail featureDetail : aggregatedDetails)
			{
				if(featureDetail.getParameterName().equalsIgnoreCase("MOS"))
				{
					return featureDetail;
				}
			}
		}
		return null;
	}
	
	public FeatureDetail getCategoryMOS(Category category)
	{
		List<FeatureDetail> featureDetailList = category.FeatureDetails();
		if(featureDetailList !=null && featureDetailList.size() > 0)
		{
			for(FeatureDetail featureDetail : featureDetailList)
			{
				if(featureDetail.getParameterName().equalsIgnoreCase("MOS"))
				{
					return featureDetail;
				}
			}
		}
		return null;
	}
}
