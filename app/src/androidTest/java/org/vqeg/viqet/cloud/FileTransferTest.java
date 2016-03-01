package org.vqeg.viqet.cloud;

import android.test.AndroidTestCase;

import org.vqeg.viqet.cloud.Json.SASResponse;
import org.vqeg.viqet.utilities.FileHelper;

import java.io.File;

public class FileTransferTest extends AndroidTestCase
{
    public void testUploadFile()
    {
        SASResponse sasResponse = CloudCommunicator.FetchUploadSAS(this.getContext());
        assertNotNull(sasResponse);

        //Create a file
        File file = FileHelper.createUniqueFile(FileHelper.testPhotoPrefix, FileHelper.testPhotoDirectoryName);
        assertEquals(true, FileTransfer.UploadFile(file.getAbsolutePath(), sasResponse.getSASURL(), this.getContext()));
        assertEquals(true,file.delete());
    }

    public void testDownloadFile()
    {
        String sasURL = "http://viqetstorageservice.blob.core.windows.net/viqet-staging/037c134f-f072-4df5-bd7c-e685b58940a1?sv=2014-02-14&sr=c&sig=mRx%2Fb5EVT9sUv9M7bB2cgP76MKqz8JFxWXn6P3DpWMA%3D&st=2015-03-10T07%3A00%3A00Z&se=2016-01-01T08%3A00%3A00Z&sp=r";
        String filePath = FileTransfer.Download(FileHelper.testPhotoPrefix, FileHelper.testPhotoDirectoryName, sasURL, this.getContext());
        assertNotNull(filePath);
        File file = new File(filePath);
        assertEquals(true, file.delete());
    }
}
