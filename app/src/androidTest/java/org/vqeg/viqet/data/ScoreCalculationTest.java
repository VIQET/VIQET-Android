package org.vqeg.viqet.data;

import android.test.AndroidTestCase;

public class ScoreCalculationTest extends AndroidTestCase {

    //Input Categories
    private final String landscape = "Landscape";
    private final String wallHanging = "Wall";
    private final String dinnerPlate = "Still Life";
    private final String night = "Night";

    //Output Categories
    private final String outdoorDay = "Outdoor Day";
    private final String outdoorNight = "Outdoor Night";
    private final String indoor = "Indoor";

    //Test parameters
    private final String sharpness = "Sharpness";
    private final String noise = "Noise";
    private final String mos = "MOS";

    private Result result;

    private PhotoDetail createPhotoDetail(String parameter, double value){
        PhotoDetail photoDetail = new PhotoDetail();
        photoDetail.setParameterName(parameter);
        photoDetail.setValue(value);
        photoDetail.setDisplayPreference("MainPage");
        return photoDetail;
    }

    private void populatePhotos(){
    Photo photo = null;
    int photoIndex = 0;

    //wallHanging:

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,40));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,80));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,1.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,45));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,90));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,1.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,50));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,100));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,55));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,110));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,60));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,120));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    //Night

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,20));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,100));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,1.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,21));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,200));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,22));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,300));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,23));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,400));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,24));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,500));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    //Outdoor Day

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,80));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,40));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,90));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,45));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,100));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,50));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,110));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,55));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,120));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,60));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,4.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    //dinnerPlate:

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,80));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,40));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,90));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,45));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,2.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,100));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,50));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,110));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,55));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,3.5));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    photo = this.result.getPhotoList().get(photoIndex++);
    photo.addPhotoDetail(sharpness,createPhotoDetail(sharpness,120));
    photo.addPhotoDetail(noise,createPhotoDetail(noise,60));
    photo.addPhotoDetail(mos,createPhotoDetail(mos,4.0));
    photo.setState(Photo.State.ANALYSIS_COMPLETE);

    }


    @Override
    protected void setUp() throws Exception {

        super.setUp();
        this.result = new Result("Test", RemoteInfoProvider.getRemoteInfo());

        //Get the list of input categories, and fill in the photos
        populatePhotos();
    }

    @Override
    protected void tearDown() throws Exception {
        this.result = null;
        super.tearDown();
    }

    public void testCategoryScore()
    {
        for(Category category : this.result.getOutputCategoryList())
        {
            switch (category.getName())
            {
                case outdoorDay: {
                    for (FeatureDetail featureDetail : category.FeatureDetails()) {
                        switch (featureDetail.getParameterName()) {
                            case mos:
                                assertEquals(featureDetail.getValue(), 3.0);
                                break;
                            case sharpness:
                                assertEquals(featureDetail.getValue(), 100.0);
                                break;
                            case noise:
                                assertEquals(featureDetail.getValue(), 50.0);
                                break;
                        }
                    }
                    break;
                }
                case indoor: {
                    for (FeatureDetail featureDetail : category.FeatureDetails()) {
                        switch (featureDetail.getParameterName()) {
                            case mos:
                                assertEquals(featureDetail.getValue(), 2.5);
                                break;
                            case sharpness:
                                assertEquals(featureDetail.getValue(), 75.0);
                                break;
                            case noise:
                                assertEquals(featureDetail.getValue(), 75.0);
                                break;
                        }
                    }
                    break;
                }
                case outdoorNight: {
                    for (FeatureDetail featureDetail : category.FeatureDetails()) {
                        switch (featureDetail.getParameterName()) {
                            case mos:
                                assertEquals(featureDetail.getValue(), 2.5);
                                break;
                            case sharpness:
                                assertEquals(featureDetail.getValue(), 22.0);
                                break;
                            case noise:
                                assertEquals(featureDetail.getValue(), 300.0);
                                break;
                        }
                    }
                    break;
                }
            }
        }

    }

    public void testOverallScore(){
        for(FeatureDetail featureDetail :this.result.OverallDetails()){
            switch (featureDetail.getParameterName()) {
                case mos:
                    assertEquals(featureDetail.getValue(), 2.7);
                    break;
                case sharpness:
                    assertEquals(featureDetail.getValue(), 63.8);
                    break;
                case noise:
                    assertEquals(featureDetail.getValue(), 155.0);
                    break;
            }
        }

    }
}
