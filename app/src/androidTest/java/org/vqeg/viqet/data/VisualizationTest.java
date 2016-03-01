package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

public class VisualizationTest extends AndroidTestCase {
	
	public void testFilename()
	{
		Visualization visualization = new Visualization();
		
		visualization.setVisualizationName("visualizationName");
		assertEquals("visualizationName", visualization.getVisualizationName());
		
		visualization.setVisualizationName("visualization Name");
		assertEquals("visualization Name", visualization.getVisualizationName());
	}
	
	public void testFilePath()
	{
		Visualization visualization = new Visualization();
		
		visualization.setFilePath("testfilepath");
		assertEquals("testfilepath", visualization.getFilePath());
		
		visualization.setFilePath("/testfilepath");
		assertEquals("/testfilepath", visualization.getFilePath());
		
		visualization.setFilePath("/test file path");
		assertEquals("/test file path", visualization.getFilePath());
		
		visualization.setFilePath("/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path");
		assertEquals("/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path/test/file/path", visualization.getFilePath());
	}
}
