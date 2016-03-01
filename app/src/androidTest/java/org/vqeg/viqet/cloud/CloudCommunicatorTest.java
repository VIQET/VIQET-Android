package org.vqeg.viqet.cloud;

import android.test.AndroidTestCase;

import org.vqeg.viqet.cloud.Json.ExamplePhotoResponse;
import org.vqeg.viqet.cloud.Json.InputCategoryResponse;
import org.vqeg.viqet.cloud.Json.MethodologyResponse;
import org.vqeg.viqet.cloud.Json.OutputCategoryResponse;
import org.vqeg.viqet.cloud.Json.PhotoDetailResponse;
import org.vqeg.viqet.cloud.Json.PhotoResponse;
import org.vqeg.viqet.cloud.Json.VersionResponse;
import org.vqeg.viqet.cloud.Json.VisualizationImagesResponse;

import java.util.List;

public class CloudCommunicatorTest extends AndroidTestCase
{
    private String cloudVersion = "3_114";
    private int featureCount = 6;
    private int visualizationCount = 2;

    public void testFetchVersion()
    {
        VersionResponse versionResponse = CloudCommunicator.FetchVersion(this.getContext());
        assertNotNull(versionResponse);
        assertEquals(cloudVersion, versionResponse.getVersionString());
    }

    public void testFetchPhotoDetailsSuccess()
    {
        //Blob that exists
        String blobName = "d5207e8b-ab66-4aa2-a662-c06a38734671";
        String containerName = "viqet-staging";
        String inputCategory= "Landscape";

        PhotoResponse photoResponse = CloudCommunicator.FetchPhotoDetails(blobName, containerName, inputCategory, this.getContext());
        assertNotNull(photoResponse);
        assertEquals(PhotoResponse.ErrorCodes.Success, photoResponse.getErrorCode());

        //Check that right cloud version is being used
        assertEquals(cloudVersion,photoResponse.getVersionString());

        //Check features returned
        List<PhotoDetailResponse> photoDetailResponse = photoResponse.getPhotoDetails();
        assertNotNull(photoDetailResponse);
        assertEquals(featureCount, photoDetailResponse.size());

        //Check visualizations returned
        List<VisualizationImagesResponse> visualizationImagesResponse = photoResponse.getVisualizationImages();
        assertNotNull(visualizationImagesResponse);
        assertEquals(visualizationCount, visualizationImagesResponse.size());
    }

    //Add test case for blob not found
    public void testFetchPhotoDetailsMissingBlob()
    {
        //Blob that exists
        String blobName = "missingBlob";
        String containerName = "viqet-staging";
        String inputCategory= "Landscape";

        PhotoResponse photoResponse = CloudCommunicator.FetchPhotoDetails(blobName, containerName, inputCategory, this.getContext());
        assertNotNull(photoResponse);
        assertEquals(PhotoResponse.ErrorCodes.MissingBlob, photoResponse.getErrorCode());
    }

    public void testFetchMethodology()
    {
        MethodologyResponse methodologyResponse = CloudCommunicator.FetchMethodology(this.getContext());
        assertNotNull(methodologyResponse);

        //Check Version
        assertNotNull(methodologyResponse.getVersion());

        //Check each input category
        InputCategoryResponse[] inputCategoryResponseArray = methodologyResponse.getInputCategoryResponseArray();
        assertNotNull(inputCategoryResponseArray);
        for(InputCategoryResponse inputCategoryResponse : inputCategoryResponseArray)
        {
            //Check each output Category Name
            String[] outputCategoryResponseArray = inputCategoryResponse.getOutputCategoryNameArray();
            assertNotNull(outputCategoryResponseArray);
            for(String outputCategoryName : outputCategoryResponseArray)
            {
                assertNotNull(outputCategoryName);
            }

            //Check description
            assertNotNull(inputCategoryResponse.getDescription());

            //Check examplePhotoList
            ExamplePhotoResponse[] examplePhotoResponseArray = inputCategoryResponse.getExamplePhotoResponseArray();
            assertNotNull(examplePhotoResponseArray);
            for(ExamplePhotoResponse examplePhoto : examplePhotoResponseArray)
            {
                assertNotNull(examplePhoto.isAcceptable());
                assertNotNull(examplePhoto.getBlobFileName());
                assertNotNull(examplePhoto.getDescription());
                assertNotNull(examplePhoto.getName());
            }
        }

        //Check each output category
        OutputCategoryResponse[] outputCategoryResponseArray = methodologyResponse.getOutputCategoryResponseArray();
        assertNotNull(outputCategoryResponseArray);
        for(OutputCategoryResponse outputCategoryResponse : outputCategoryResponseArray)
        {
            assertNotNull(outputCategoryResponse.getDescription());
            assertNotNull(outputCategoryResponse.getName());
        }
    }
}
