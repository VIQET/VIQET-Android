package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

public class ResultTest extends AndroidTestCase {
	
private Result result;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.result = new Result("testName", RemoteInfoProvider.getRemoteInfo());

        //Add photo 1
		Photo photo1 = this.result.getPhotoList().get(0);
        photo1.setState(Photo.State.ANALYSIS_COMPLETE);
//        photo1.setOutputCategory("Outdoor Day");
//        photo1.setOutputCategory("Outdoor Night");
		
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

		//Add Photo 2
		Photo photo2 = this.result.getPhotoList().get(1);
        photo2.setState(Photo.State.ANALYSIS_COMPLETE);
//        photo2.setOutputCategory("Indoor");
		
		PhotoDetail photoDetail3 = new PhotoDetail();
		photoDetail3.setParameterName("Noise");
		photoDetail3.setValue(100.01);
		photoDetail3.setDisplayPreference("MainPage");
		photo2.addPhotoDetail("Noise", photoDetail3);
		
		photoDetail3 = new PhotoDetail();
		photoDetail3.setParameterName("Sharpness");
		photoDetail3.setValue(202.01);
		photoDetail3.setDisplayPreference("MainPage");
		photo2.addPhotoDetail("Sharpness", photoDetail3);

	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		this.result = null;
	};
	
	public void testName()
	{
		this.result.setName("Test Name");
		assertEquals("Test Name", this.result.getName());
	}
	
	public void testPhotoList()
	{
		assertEquals(20,this.result.getPhotoList().size());
	}
	
	public void testInputCategoryList()
	{
		assertEquals(4,this.result.getInputCategoryList().size());
	}
	
	public void testOutputCategoryList()
	{
		//Check size
		assertEquals(3,this.result.getOutputCategoryList().size());
	}
}
