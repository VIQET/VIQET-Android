/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.data;

import org.vqeg.viqet.data.Photo.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

@SuppressWarnings("serial")
public class Result implements Serializable {

	// Test Name
	private String name;

	public String getName() {
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	private List<Category> inputCat;
    public Result(String name, RemoteInfo remoteInfo)
    {
        //Set name
        this.setName(name);

        //Create placeholder photos in each category
        if(remoteInfo != null)
        {
            Set<String> inputCategories = remoteInfo.getInputCategories();
            if(inputCategories != null)
            {
                for(String categoryName : inputCategories)
                {
                    InputCategoryInfo inputCategoryInfo = remoteInfo.getInputCategory(categoryName);
                    if(inputCategoryInfo != null)
                    {
                        for(int photoIndex = 0; photoIndex < inputCategoryInfo.getRequiredPhotoCount(); photoIndex++)
                        {
                            Photo photo = new Photo();
                            photo.setFilename("Add Photo");
                            photo.setInputCategory(categoryName);

                            //Add output Categories
                            for(String outputCategory : RemoteInfoProvider.getRemoteInfo().getInputCategory(categoryName).getOutputCategories()) {
                                photo.setOutputCategory(outputCategory);
                            }

                            this.addPhoto(photo);
                        }
                    }
                }
            }
        }

        //Add the remote info to the result object
        this.setRemoteInfo(remoteInfo);
    }

	//RemoteInfo
	private RemoteInfo remoteInfo;
	public RemoteInfo getRemoteInfo() 
	{
		return this.remoteInfo;
	}
	private void setRemoteInfo(RemoteInfo remoteInfo)
	{
		this.remoteInfo = remoteInfo;
	}

	// Photo List
	private List<Photo> photoList;

	public List<Photo> getPhotoList()
	{
		return this.photoList;
	}

	public void addPhoto(Photo photo) 
	{
		if (this.photoList == null) 
		{
			this.photoList = new ArrayList<Photo>();
		}
		this.photoList.add(photo);
		Reset();
	}

	// Input Category List
	private List<Category> inputCategoryList;

	public List<Category> getInputCategoryList()
	{
        return getCategoriesFromPhotos(this.photoList, true);
	}

	// Output Category List
	private List<Category> outputCategoryList;

	public List<Category> getOutputCategoryList()
	{
        return getCategoriesFromPhotos(this.photoList, false);
	}

	// Feature List
	private List<FeatureDetail> overallDetails;

	public List<FeatureDetail> OverallDetails()
	{
			// Calculate aggregated Feature Details
			List<Category> outputCategoryList = getOutputCategoryList();
			if (outputCategoryList != null && outputCategoryList.size() > 0)
			{
                //Find the lcm of number of values in each category
                int lcm = 1;
                for(Category category : outputCategoryList)
                {
                    if(category.getCompletedPhotoList().size() > 0 )
                      lcm = LCM(lcm, category.getCompletedPhotoList().size());
                }
				//Extract each parameter of the photo into the categoryDetailMap
                Map<String,List<FeatureDetail>> CategoryDetailMap = new HashMap<String, List<FeatureDetail>>();
				for(Category category : outputCategoryList)
				{
                    if(category.getCompletedPhotoList().size() > 0) {
                        int copyCount = lcm / category.getCompletedPhotoList().size();
                        List<FeatureDetail> categoryFeatureDetails = category.FeatureDetails();
                        List<FeatureDetail> deviceDetailList = new ArrayList<FeatureDetail>(category.FeatureDetails());
                        for (int parameterIndex = 0; parameterIndex < deviceDetailList.size(); parameterIndex++) {
                            String parameterName = deviceDetailList.get(parameterIndex).getParameterName();
                            //Get the list for the current parameter. If no list is present, create one
                            List<FeatureDetail> listToAddCurrentParameter = CategoryDetailMap.get(parameterName);
                            if (listToAddCurrentParameter == null) {
                                listToAddCurrentParameter = new ArrayList<FeatureDetail>();
                                CategoryDetailMap.put(parameterName, listToAddCurrentParameter);
                            }
                            //Add the value of the parameter to the corresponding list (to be aggregated later)
                            //Add the value copyCount times
                            for (int i = 0; i < copyCount; i++) {
                                listToAddCurrentParameter.add(deviceDetailList.get(parameterIndex));
                            }
                        }
                    }
				}
				//Create list of aggregated parameters
				this.overallDetails = new ArrayList<FeatureDetail>();
				for(String parameterName : CategoryDetailMap.keySet())
				{
					this.overallDetails.add(AggregateValue(CategoryDetailMap.get(parameterName)));
				}
			}
		return this.overallDetails;
	}

	//Group photos by category
	private List<Category> getCategoriesFromPhotos(List<Photo> photos, boolean isInputCategory)
	{
		List<Category> categoryList = null;
		if(photos!=null && photos.size() > 0)
		{
			categoryList = new ArrayList<Category>();

			// Iterate through all the photos and group them by Category - Add them to HashMap
			LinkedHashMap<String, List<Photo>> categoryPhotoLists = new LinkedHashMap<String, List<Photo>>();
			for (Photo photo : photos)
			{
				List<String> categoriesForThisPhoto = null;

				if(isInputCategory && photo.getInputCategory() != null)
				{
					String inputCategory = photo.getInputCategory();
					if(inputCategory != null)
					{
						categoriesForThisPhoto = new ArrayList<String>();
						categoriesForThisPhoto.add(inputCategory);
					}
				}
				else
				{
					categoriesForThisPhoto = photo.getOutputCategories();
				}

				if(categoriesForThisPhoto !=null)
				{
					for(String categoryForThisPhoto:categoriesForThisPhoto)
					{
						// The first time a category is encountered, we create a new
						// ArrayList to save all the photos from that category
						if (!categoryPhotoLists.containsKey(categoryForThisPhoto))
						{
							categoryPhotoLists.put(categoryForThisPhoto, new ArrayList<Photo>());
						}

						//Fetch the right Photo list based on category and add the photo to it
						List<Photo> categoryPhotoList = categoryPhotoLists.get(categoryForThisPhoto);
						categoryPhotoList.add(photo);
					}
				}
			}

			// Convert Hashmap of Photos to ArrayList of categories
			for(Entry<String,List<Photo>> entry: categoryPhotoLists.entrySet())
			{
				Category category = new Category(entry.getValue());
				category.setName(entry.getKey());
				categoryList.add(category);
			}
		}
		return categoryList;
	}

	// Whenever photos are added or deleted, reset the categories and detail lists
	private void Reset() {
		this.inputCategoryList = null;
		this.outputCategoryList = null;
		this.overallDetails = null;
	}

	private FeatureDetail AggregateValue(List<FeatureDetail> featureDetailList)
	{
		if (featureDetailList!=null && featureDetailList.size() > 0)
		{
			//Calculate mean
			double sum = 0;
			for(FeatureDetail featureDetail : featureDetailList)
			{
				sum += featureDetail.getValue();
			}
			double mean = sum/featureDetailList.size();

			//Calculate Standard Deviation
			double sumSquaredDifferences = 0.0;
            for(FeatureDetail featureDetail : featureDetailList)
            {
                double difference = featureDetail.getValue() - mean;
                sumSquaredDifferences += difference * difference;
            }
			double standardDeviation = Math.sqrt(sumSquaredDifferences / featureDetailList.size());
			//Calculate standard error
			double standardErrorOfMean = standardDeviation / Math.sqrt(featureDetailList.size());
			FeatureDetail aggregateValue = new FeatureDetail();
			//We use the first element for the Parameter name and display preference
			aggregateValue.setParameterName(featureDetailList.get(0).getParameterName());
			aggregateValue.setDisplayPreference(featureDetailList.get(0).getDisplayPreference());
			aggregateValue.setValue(mean);
			aggregateValue.setStandardDeviation(standardDeviation);
			aggregateValue.setStandardError(standardErrorOfMean);
			aggregateValue.setConfidenceInterval95(1.96 * standardDeviation);

			return aggregateValue;
		}
		else
			return null;
	}

	// Take the mean and standard deviation and generate many numbers
	private List<Double> BootStrap(double mean, double standardError, int bootstrapCount)
	{
		Random random = new Random();
		List<Double> bootstrappedValues = new ArrayList<Double>(bootstrapCount);

		for (int index = 0; index < bootstrapCount; index++)
		{
			double generatedSample = mean + standardError * random.nextGaussian();
			bootstrappedValues.add(generatedSample);
		}

		return bootstrappedValues;
	}


	/// Take a list of Feature Details, bootstrap and return a List of averages
	private List<Double> GenerateAverageList(List<FeatureDetail> featureDetailList, int bootstrapCount)
	{
		List<Double> averageList = new ArrayList<Double>(featureDetailList.size() * bootstrapCount);
		for(FeatureDetail featureDetail : featureDetailList)
		{
			averageList.addAll(BootStrap(featureDetail.getValue(), featureDetail.getStandardError(), bootstrapCount));
		}
		return averageList;
	}

	public boolean isCloudVersionConsistent()
	{
		Version cloudVersion = null;
		for(Photo photo : this.photoList)
		{
			if(photo.getState() == State.ANALYSIS_COMPLETE)
			{
				//Use the cloud version of the first completed photo to check the others
				if(cloudVersion == null)
				{
					cloudVersion = photo.getCloudVersion();
				}
				else
				{
					Version photoCloudVersion = photo.getCloudVersion();
					if(!(cloudVersion.getMethodologyVersion() == photoCloudVersion.getMethodologyVersion() && cloudVersion.getAlgoVersion() == photoCloudVersion.getAlgoVersion()))
					{
						return false;
					}
				}
			}
		}
		return true;
	}

    //http://rosettacode.org/wiki/Greatest_common_divisor
    private int GCD(int a, int b)
    {
        int t;

        //Ensure B > A
        if(a < b)
        {
            t=b;
            b=a;
            a=t;
        }

        //Find GCD
        while(b != 0)
        {
            t = a % b;
            a = b;
            b = t;
        }

        if (a > 0)
        {
            return a;
        }
        else
        {
            return 1;
        }
    }

    private int LCM(int a, int b)
    {
        return a * b / GCD(a,b);
    }
}
