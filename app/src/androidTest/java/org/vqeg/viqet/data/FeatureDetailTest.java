package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

public class FeatureDetailTest extends AndroidTestCase {
	
	public void testFilename()
	{
		FeatureDetail featureDetail = new FeatureDetail();
		
		featureDetail.setParameterName("ParameterName");
		assertEquals("ParameterName", featureDetail.getParameterName());
		
		featureDetail.setParameterName("Parameter Name");
		assertEquals("Parameter Name", featureDetail.getParameterName());
	}
	
	public void testDisplayPreference()
	{
		FeatureDetail featureDetail = new FeatureDetail();
		
		featureDetail.setDisplayPreference("DisplayPreference");
		assertEquals("DisplayPreference", featureDetail.getDisplayPreference());
		
		featureDetail.setDisplayPreference("Display Preference");
		assertEquals("Display Preference", featureDetail.getDisplayPreference());
	}
	
	public void testValue()
	{
		FeatureDetail featureDetail = new FeatureDetail();
		
		featureDetail.setValue(1.0);
		assertEquals(1.0, featureDetail.getValue());

		featureDetail.setValue(1);
		assertEquals(1.0, featureDetail.getValue());
		
		featureDetail.setValue(-1.0);
		assertEquals(-1.0, featureDetail.getValue());
	}
	
	public void testStandardDeviation()
	{
		FeatureDetail featureDetail = new FeatureDetail();
		featureDetail.setStandardDeviation(10.01);
		assertEquals(10.01,featureDetail.getStandardDeviation());
	}
	
	public void testStandardError()
	{
		FeatureDetail featureDetail = new FeatureDetail();
		featureDetail.setStandardError(10.01);
		assertEquals(10.01,featureDetail.getStandardError());
	}
	
	public void testConfidenceInterval()
	{
		FeatureDetail featureDetail = new FeatureDetail();
		featureDetail.setConfidenceInterval95(10.01);
		assertEquals(10.01,featureDetail.getConfidenceInterval95());
	}

}
