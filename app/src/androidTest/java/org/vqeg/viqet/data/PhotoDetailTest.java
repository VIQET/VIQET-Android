package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

public class PhotoDetailTest extends AndroidTestCase {

	public void testFilename()
	{
		PhotoDetail photoDetail = new PhotoDetail();
		
		photoDetail.setParameterName("ParameterName");
		assertEquals("ParameterName", photoDetail.getParameterName());
		
		photoDetail.setParameterName("Parameter Name");
		assertEquals("Parameter Name", photoDetail.getParameterName());
	}
	
	public void testDisplayPreference()
	{
		PhotoDetail photoDetail = new PhotoDetail();
		
		photoDetail.setDisplayPreference("DisplayPreference");
		assertEquals("DisplayPreference", photoDetail.getDisplayPreference());
		
		photoDetail.setDisplayPreference("Display Preference");
		assertEquals("Display Preference", photoDetail.getDisplayPreference());
	}
	
	public void testValue()
	{
		PhotoDetail photoDetail = new PhotoDetail();
		
		photoDetail.setValue(1.0);
		assertEquals(1.0, photoDetail.getValue());

		photoDetail.setValue(1);
		assertEquals(1.0, photoDetail.getValue());
		
		photoDetail.setValue(-1.0);
		assertEquals(-1.0, photoDetail.getValue());
	}
}
