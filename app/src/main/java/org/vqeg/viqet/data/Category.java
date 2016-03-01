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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Category implements Serializable {

	// Name
	private String name;

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	// Photo List
	private List<Photo> photoList;

	public List<Photo> getPhotoList()
	{
		return this.photoList;
	}

    public List<Photo> getCompletedPhotoList()
    {
        List<Photo> completedList = new ArrayList<>(this.photoList.size());
        for(Photo photo : this.photoList)
        {
            if(photo.getState() == Photo.State.ANALYSIS_COMPLETE) {
                completedList.add(photo);
            }
        }
        return completedList;

    }

	public Category(List<Photo> photoList)
	{
		this.photoList = photoList;
	}
	

	@Override
	public String toString()
	{
		return "Category: " + this.name + " Count:" + this.photoList.size();
	}

	// Feature Details
	public List<FeatureDetail> FeatureDetails()
	{
        if(photoList == null) return null;

        Map<String,List<Double>> featureValuesMap = new HashMap<String,List<Double>>();
        Map<String,String>featureDisplayPrefMap = new HashMap<String, String>();

        //For each photo extract features and put in featureValuesMap
        for(Photo photo : this.photoList){
            if(photo.getState() == Photo.State.ANALYSIS_COMPLETE && photo.getPhotoDetailMap() != null) {

                for(PhotoDetail photoDetail : photo.getPhotoDetailMap().values()){

                    String parameterName = photoDetail.getParameterName();
                    String displayPref = photoDetail.getDisplayPreference();

                    //If this is the first time you see this photoDetail name,
                    // create a new entry in featureValuesMap and featureDisplayPrefMap
                    if(!featureValuesMap.containsKey(parameterName)){
                        List<Double> featureValues = new ArrayList<Double>(this.photoList.size());
                        featureValuesMap.put(parameterName,featureValues);
                        featureDisplayPrefMap.put(parameterName, displayPref);
                    }

                    //Add the photoDetail to the corresponding featureValue list
                    List<Double> featureValues = featureValuesMap.get(parameterName);
                    featureValues.add(photoDetail.getValue());
                }
            }
        }

        // Aggregate the feature details and return
        int numberOfFeatures = featureValuesMap.size();
        List<FeatureDetail> featureDetailList = new ArrayList<FeatureDetail>(numberOfFeatures);

        for(Map.Entry<String,List<Double>> entry : featureValuesMap.entrySet())
        {

            //featureDetail.setDisplayPreference();
            String parameterName = entry.getKey();
            String displayPref = featureDisplayPrefMap.get(parameterName);
            List<Double> values = entry.getValue();
            if(values.size() > 0) {
                featureDetailList.add(AggregateValue(parameterName, displayPref, values));
            }
            else
            {
                FeatureDetail featureDetail = new FeatureDetail();
                featureDetail.setParameterName(parameterName);
                featureDetail.setValue(0.0);
                featureDetail.setStandardError(0.0);
                featureDetail.setConfidenceInterval95(0.0);
                featureDetail.setDisplayPreference(displayPref);
                featureDetailList.add(featureDetail);
            }
        }

		return featureDetailList;
	}
	
	private FeatureDetail AggregateValue(String parameterName, String displayPreference, List<Double> valueList)
    {
        //Formulas used from http://www.sjsu.edu/faculty/gerstman/StatPrimer/estimation.pdf
        int n = valueList.size();

        //Calculate Mean
        double sum = 0.0;
        for (double value : valueList) 
        {
        	sum += value;
        }
        double mean = sum / n;

        //Calculate Standard Deviation
        double sumSquaredDifferences = 0.0;
        
        for (double value : valueList) 
        {
        	double difference = value - mean;
            sumSquaredDifferences += difference * difference;
		}

        double standardDeviation = Math.sqrt(sumSquaredDifferences / n);

        //Calculate standard error
        double standardErrorOfMean = standardDeviation / Math.sqrt(n);

        //Calculate error for 95% confidence
        double confidenceInterval = standardErrorOfMean * TStat(n);

        FeatureDetail aggregateValue = new FeatureDetail();
        aggregateValue.setParameterName(parameterName);
        aggregateValue.setValue(mean); 
        aggregateValue.setStandardDeviation(standardDeviation);
        aggregateValue.setStandardError(standardErrorOfMean);
        aggregateValue.setConfidenceInterval95(confidenceInterval);
        aggregateValue.setDisplayPreference(displayPreference);
        return aggregateValue;
    }
	
	private double TStat(int n)
    {
        //t Table values for 95% confidence interval from http://www.sjsu.edu/faculty/gerstman/StatPrimer/t-table.pdf

        //df -> Degrees of freedom
        int df = n - 1;

        return
        df == 0 ? 0 :
        df == 1 ? 12.71 :
        df == 2 ? 4.303 :
        df == 3 ? 3.182 :
        df == 4 ? 2.776 :
        df == 5 ? 2.571 :
        df == 6 ? 2.447 :
        df == 7 ? 2.365 :
        df == 8 ? 2.306 :
        df == 9 ? 2.262 :
        df == 10 ? 2.228 :
        df == 11 ? 2.201 :
        df == 12 ? 2.179 :
        df == 13 ? 2.160 :
        df == 14 ? 2.145 :
        df == 15 ? 2.131 :
        df == 16 ? 2.120 :
        df == 17 ? 2.110 :
        df == 18 ? 2.101 :
        df == 19 ? 2.093 :
        df == 20 ? 2.086 :
        df == 21 ? 2.080 :
        df == 22 ? 2.074 :
        df == 23 ? 2.069 :
        df == 24 ? 2.064 :
        df == 25 ? 2.060 :
        df == 26 ? 2.056 :
        df == 27 ? 2.052 :
        df == 28 ? 2.048 :
        df == 29 ? 2.045 :
        df < 40 ? 2.042 :
        df < 60 ? 2.021 :
        df < 80 ? 2.000 :
        df < 100 ? 1.990 :
        df < 1000 ? 1.984 :
        1.962;
    }

}

