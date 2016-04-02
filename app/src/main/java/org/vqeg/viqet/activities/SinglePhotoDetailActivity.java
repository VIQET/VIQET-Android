package org.vqeg.viqet.activities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.vqeg.viqet.adapters.ImageSliderPhotoDetailAdapter;
import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.data.Visualization;
import org.vqeg.viqet.logic.PhotoDetails;
import org.vqeg.viqet.singlephotodata.SinglePhoto;
import org.vqeg.viqet.utilities.PhotoDecoder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkalidin on 3/26/2016.
 */
public class SinglePhotoDetailActivity extends ActionBarActivity {

    private static final String PHOTO_ID = "PHOTO_ID";
    private static final String RESULT_ID = "RESULT_ID";
    private static final String FLAG = "FLAG";
    private PhotoDetails photoDetails;
    private ViewPager viewPager;
    private ImageView img_scroll_helper;
    private int resultIndex;
    private boolean flag;

    private SinglePhoto photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(org.vqeg.viqet.R.layout.activity_photo_detail);

        if(getResources().getBoolean(org.vqeg.viqet.R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        photo = (SinglePhoto)getIntent().getSerializableExtra(PHOTO_ID);
        resultIndex= getIntent().getIntExtra(RESULT_ID,0);

        initialize();
    }

    private void initialize(){
        this.photoDetails = new PhotoDetails(photo);
        getSupportActionBar().setTitle(""+photoDetails.getSinglePhoto().getFilename());

        ArrayList<String> filePaths= new ArrayList<>();
        filePaths.add(photo.getFilePath());
        for(Visualization visualization: photo.getVisualizationList()){
            String path= visualization.getFilePath();
            if(!filePaths.contains(path)) {
                filePaths.add(path);
            }
        }

        if(!getResources().getBoolean(org.vqeg.viqet.R.bool.screen_big)){
            setupViewPager(filePaths);
        }else{
            ImageView img_original = (ImageView) findViewById(org.vqeg.viqet.R.id.img_original);
            ImageView img_viz1 = (ImageView) findViewById(org.vqeg.viqet.R.id.img_viz1);
            ImageView img_viz2 = (ImageView) findViewById(org.vqeg.viqet.R.id.img_viz2);
            String path=filePaths.get(0);
            Bitmap bitmap = PhotoDecoder.getThumbnail(path, 100, 100);
            img_original.setImageBitmap(bitmap);
            String path1=filePaths.get(1);
            Bitmap bitmap1 = PhotoDecoder.getThumbnail(path1, 100, 100);
            img_viz1.setImageBitmap(bitmap1);
            String path2=filePaths.get(2);
            Bitmap bitmap2 = PhotoDecoder.getThumbnail(path2, 100, 100);
            img_viz2.setImageBitmap(bitmap2);
        }

        TextView photoDetailNameHeader = (TextView) findViewById(org.vqeg.viqet.R.id.label_photoDetailName);
        photoDetailNameHeader.setText(org.vqeg.viqet.R.string.photoFeature);

        TextView photoDetailValueHeader = (TextView) findViewById(org.vqeg.viqet.R.id.label_photoDetailValue);
        photoDetailValueHeader.setText(org.vqeg.viqet.R.string.photoValue);

        //Add Photo Details Values
        List<PhotoDetail> photoDetailList = new ArrayList<PhotoDetail>(this.photoDetails.getSinglePhoto().getPhotoDetailMap().values());
        ListAdapter photoDetailAdapter = new PhotoDetailAdapter(getApplicationContext(), org.vqeg.viqet.R.layout.photo_details_feature_item, photoDetailList);
        ListView photoDetailsListView = (ListView) findViewById(org.vqeg.viqet.R.id.photoDetailsListView);
        photoDetailsListView.setAdapter(photoDetailAdapter);
    }

    private void setupViewPager(ArrayList<String> filePaths) {

        img_scroll_helper = (ImageView) findViewById(org.vqeg.viqet.R.id.scroll_helper);
        viewPager = (ViewPager) findViewById(org.vqeg.viqet.R.id.pager_photo_detail);
        ImageSliderPhotoDetailAdapter adapter = new ImageSliderPhotoDetailAdapter(SinglePhotoDetailActivity.this,filePaths);
        viewPager.setAdapter(adapter);

        // displaying selected image first
        viewPager.setCurrentItem(0);
        img_scroll_helper.setImageResource(org.vqeg.viqet.R.drawable.swipe_one);

        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                switch(position)
                {
                    case 0:
                        img_scroll_helper.setImageResource(org.vqeg.viqet.R.drawable.swipe_one);
                        break;
                    case 1:
                        img_scroll_helper.setImageResource(org.vqeg.viqet.R.drawable.swipe_two);
                        break;
                    case 2:
                        img_scroll_helper.setImageResource(org.vqeg.viqet.R.drawable.swipe_three);
                        break;
                    default:
                        img_scroll_helper.setImageResource(org.vqeg.viqet.R.drawable.swipe_one);
                        break;
                }
            }
        });
    }

    final class PhotoDetailAdapter extends ArrayAdapter<PhotoDetail>
    {
        private final Context context;
        private int resource;
        List<PhotoDetail> photoDetailList;

        public PhotoDetailAdapter(Context context, int resource, List<PhotoDetail> photoDetailList)
        {
            super(context, resource, photoDetailList);
            this.context = context;
            this.resource = resource;
            this.photoDetailList = photoDetailList;

        }

        public View getView(final int photofeatureIndex, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(this.resource, parent,false);
            PhotoDetail featureDetail = this.photoDetailList.get(photofeatureIndex);
            //Set Feature Name
            TextView featureNameTextView = (TextView) view.findViewById(org.vqeg.viqet.R.id.photoDetailName);
            featureNameTextView.setText(featureDetail.getParameterName());
            //Set Feature Value
            TextView featureValueTextView = (TextView) view.findViewById(org.vqeg.viqet.R.id.photoDetailValue);
            featureValueTextView.setText(new DecimalFormat("#0.00").format(featureDetail.getValue()));
            return view;
        }
    }
}
