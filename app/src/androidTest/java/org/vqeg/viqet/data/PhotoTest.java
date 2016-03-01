package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

public class PhotoTest extends AndroidTestCase {
	
	public void testFilename()
	{
		Photo photo = new Photo();
		
		photo.setFilename("filename");
		assertEquals("filename", photo.getFilename());
		
		photo.setFilename("file name");
		assertEquals("file name", photo.getFilename());
		
		photo.setFilename("filename.jpg");
		assertEquals("filename.jpg", photo.getFilename());
	}
	
	public void testFilePath()
	{
		Photo photo = new Photo();
		
		photo.setFilePath("testfilepath");
		assertEquals("testfilepath", photo.getFilePath());
		
		photo.setFilePath("/testfilepath");
		assertEquals("/testfilepath", photo.getFilePath());
		
		photo.setFilePath("/test file path");
		assertEquals("/test file path", photo.getFilePath());
		
		photo.setFilePath("/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path");
		assertEquals("/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path", photo.getFilePath());
	}
	
	public void testInputCategory()
	{
		Photo photo = new Photo();
		photo.setInputCategory("input category");
		assertEquals("input category", photo.getInputCategory());
	}
	
	public void testOutputCategory()
	{
		Photo photo = new Photo();
		photo.setOutputCategory("output category1");
		photo.setOutputCategory("output category2");
		
		assertTrue(photo.getOutputCategories().contains("output category1"));
		assertTrue(photo.getOutputCategories().contains("output category2"));
	}
	
	public void testDuplicateOutputCategories()
	{
		Photo photo = new Photo();
		photo.setOutputCategory("output category1");
		photo.setOutputCategory("output category1");
		
		assertEquals(1, photo.getOutputCategories().size());
	}
	
	public void testPhotoDetails()
	{
		Photo photo = new Photo();
		
		PhotoDetail photoDetail = new PhotoDetail();
		photoDetail.setParameterName("Noise");
		photoDetail.setValue(100.01);
		photoDetail.setDisplayPreference("MainPage");
		photo.addPhotoDetail("Noise", photoDetail);
		
		PhotoDetail returnedPhotoDetail = photo.getPhotoDetail("Noise");
		assertEquals("Noise", returnedPhotoDetail.getParameterName());
		assertEquals(100.01, returnedPhotoDetail.getValue());
		assertEquals("MainPage", returnedPhotoDetail.getDisplayPreference());
		
	}
	
	public void testVisualization()
	{
		Photo photo = new Photo();
		
		Visualization visualization = new Visualization();
		visualization.setVisualizationName("Sharpness Heatmap");
		visualization.setFilePath("File Path");
		
		photo.addVisualization(visualization);
		
		Visualization returnedVisualization = photo.getVisualizationList().get(0);
		assertEquals("Sharpness Heatmap", returnedVisualization.getVisualizationName());
		assertEquals("File Path", returnedVisualization.getFilePath());
	}
}
