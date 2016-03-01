package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

public class CategoryTest extends AndroidTestCase {
private Category category;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Photo photo1 = new Photo();
        photo1.setOutputCategory("Outdoor Day");
        photo1.setState(Photo.State.ANALYSIS_COMPLETE);
		
		PhotoDetail photoDetail1 = new PhotoDetail();
		photoDetail1.setParameterName("Noise");
		photoDetail1.setValue(100.01);
		photoDetail1.setDisplayPreference("MainPage");
		photo1.addPhotoDetail("Noise", photoDetail1);
		
		PhotoDetail photoDetail2 = new PhotoDetail();
		photoDetail2.setParameterName("Sharpness");
		photoDetail2.setValue(200.01);
		photoDetail2.setDisplayPreference("MainPage");
		photo1.addPhotoDetail("Sharpness", photoDetail2);
		
		Photo photo2 = new Photo();
        photo2.setOutputCategory("Outdoor Day");
        photo1.setState(Photo.State.ANALYSIS_COMPLETE);
		
		PhotoDetail photoDetail3 = new PhotoDetail();
		photoDetail3.setParameterName("Noise");
		photoDetail3.setValue(100.01);
		photoDetail3.setDisplayPreference("MainPage");
		photo2.addPhotoDetail("Noise", photoDetail3);
		
		PhotoDetail photoDetail4 = new PhotoDetail();
		photoDetail4.setParameterName("Sharpness");
		photoDetail4.setValue(200.01);
		photoDetail4.setDisplayPreference("MainPage");
		photo2.addPhotoDetail("Sharpness", photoDetail4);
		
		ArrayList<Photo> photoList = new ArrayList<Photo>();
		photoList.add(photo1);
		photoList.add(photo2);
		
		this.category = new Category(photoList);
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.category = null;
	};
	
	public void testName()
	{
		this.category.setName("Daylight");
		assertEquals("Daylight",category.getName());
	}
	
	public void testPhotoList()
	{
		assertEquals(2,this.category.getPhotoList().size());
	}
	
	public void testFeatureDetails()
	{
		List<FeatureDetail> featureDetails = this.category.FeatureDetails();
		
		assertEquals(2,featureDetails.size());
		
		assertEquals("Noise",featureDetails.get(0).getParameterName());
		assertEquals(100.01,featureDetails.get(0).getValue());
		assertEquals(0.0,featureDetails.get(0).getStandardDeviation());
		assertEquals(0.0,featureDetails.get(0).getStandardError());
		assertEquals("MainPage",featureDetails.get(0).getDisplayPreference());
		
		assertEquals("Sharpness",featureDetails.get(1).getParameterName());
		assertEquals(200.01,featureDetails.get(1).getValue());
		assertEquals(0.0,featureDetails.get(1).getStandardDeviation());
		assertEquals(0.0,featureDetails.get(1).getStandardError());
		assertEquals("MainPage",featureDetails.get(1).getDisplayPreference());
	}

}
