package org.vqeg.viqet.logic;

import android.test.AndroidTestCase;

import org.vqeg.viqet.data.Result;

public class ResultBrowserTest extends AndroidTestCase {
	
	public void testAddDeleteResult()
	{
		ResultBrowser resultBrowser = new ResultBrowser();
        int initialSize = resultBrowser.getResults().size();

		Result result = resultBrowser.addResult("Test1");
		assertEquals(initialSize +1, resultBrowser.getResults().size());
		
		resultBrowser.deleteResult(result);
		assertEquals(initialSize, resultBrowser.getResults().size());
	}
	
	public void testAddDeleteMultipleResult()
	{

		ResultBrowser resultBrowser = new ResultBrowser();
        int initialSize = resultBrowser.getResults().size();

		Result result1 = resultBrowser.addResult("Test1");
		assertEquals(initialSize + 1, resultBrowser.getResults().size());
		
		Result result2 = resultBrowser.addResult("Test2");
		assertEquals(initialSize + 2, resultBrowser.getResults().size());
		
		Result result3 = resultBrowser.addResult("Test3");
		assertEquals(initialSize + 3, resultBrowser.getResults().size());
		
		resultBrowser.deleteResult(result1);
		assertEquals(initialSize + 2, resultBrowser.getResults().size());
		
		resultBrowser.deleteResult(result2);
		assertEquals(initialSize + 1, resultBrowser.getResults().size());
		
		//test if the non existing result is deleted
		resultBrowser.deleteResult(result2);
		assertEquals(initialSize + 1, resultBrowser.getResults().size());
		
		resultBrowser.deleteResult(result3);
		assertEquals(initialSize, resultBrowser.getResults().size());
		
		//test if the non existing result is deleted
		resultBrowser.deleteResult(result3);
		assertEquals(initialSize, resultBrowser.getResults().size());
	}

}
