package org.vqeg.viqet.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.activities.CameraActivity;
import org.vqeg.viqet.activities.GalleryActivity;
import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.activities.PhotoPreviewActivity;
import org.vqeg.viqet.activities.SinglePhotoDetailActivity;
import org.vqeg.viqet.asynctasks.ImageLoadTask;
import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.logic.PhotoDetails;
import org.vqeg.viqet.services.PhotoInspectorService;
import org.vqeg.viqet.singlephotodata.SinglePhoto;
import org.vqeg.viqet.singlephotologic.SinglePhotoResultBrowser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadSinglePhotoFragment extends Fragment {
    static final int PHOTO_FROM_CAMERA=1;
    static final int PHOTO_FROM_GALLERY=2;

    private SinglePhoto photo;
    private ImageButton details,evaluatePhoto,uploadImage;
    private ImageView image;
    private ProgressBar busyRing;
    private RadioGroup radioGroup;
    private RelativeLayout rel_layout_upload,rel_layout_upload_State,rel_layout_result_State, rel_layout_eval;
    private TextView tv_status,image_name,singleOverallMOS;
    private int resultIndex;
    private RatingBar mosRatingBar;
    private boolean isPresent;
    private String filepath;
    private String filename;

    private SinglePhotoResultBrowser resultBrowser;
    public UploadSinglePhotoFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_upload_single_photo, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Photo Quality");
        uploadImage =(ImageButton) rootView.findViewById(R.id.upload_single_photo);
        evaluatePhoto=(ImageButton) rootView.findViewById(R.id.evaluate_single_photo);
        details = (ImageButton) rootView.findViewById(R.id.single_details);
        image =(ImageView) rootView.findViewById(R.id.single_photo_display);
        radioGroup =(RadioGroup)rootView.findViewById(R.id.radioGroup);
        rel_layout_upload = (RelativeLayout)rootView.findViewById(R.id.rel_layout_single_upload_and_eval);
        rel_layout_upload_State = (RelativeLayout)rootView.findViewById(R.id.rel_layout_upload_State);
        rel_layout_result_State=(RelativeLayout) rootView.findViewById(R.id.rel_layout_result_State);
        rel_layout_eval=(RelativeLayout) rootView.findViewById(R.id.rel_layout_single_eval);
        tv_status=(TextView)rootView.findViewById(R.id.tv_status);
        image_name=(TextView)rootView.findViewById(R.id.test_single_name);
        singleOverallMOS=(TextView)rootView.findViewById(R.id.singleOverallMOS);
        busyRing= (ProgressBar)rootView.findViewById(R.id.prog);
        mosRatingBar=(RatingBar)rootView.findViewById(R.id.mosSingleImageRatingBar);

        MainActivity.lastClicked=5;
        resultBrowser = new SinglePhotoResultBrowser();
        Bundle bundle = getArguments();
        resultIndex = bundle.getInt("RESULT_INDEX", -1);
        if(resultIndex==-1) {
            photo = new SinglePhoto();
            photo.setState(SinglePhoto.State.NO_PHOTO);
            isPresent=false;
        }
        else{
            photo = resultBrowser.getResults().get(resultIndex).getPhoto();
            image.setImageURI(Uri.parse(photo.getFilePath()));
            isPresent=true;
        }
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.select_upload_method);
                builder.setPositiveButton(R.string.select_gallery, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), GalleryActivity.class);
                        startActivityForResult(intent, PHOTO_FROM_GALLERY);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton(R.string.select_camera, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), CameraActivity.class);
                        startActivityForResult(intent, PHOTO_FROM_CAMERA);
                        dialog.dismiss();
                    }
                });
            }
        });

        evaluatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluatePhoto();
            }
        });

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SinglePhotoDetailActivity.class);
                intent.putExtra("PHOTO_ID", photo);
                intent.putExtra("RESULT_ID", resultIndex);
                startActivity(intent);
            }
        });
        checkState(photo);

        image.setClickable(true);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photo.getState()!= SinglePhoto.State.NO_PHOTO){
                    if(photo.getFilePath() != null)
                    {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), PhotoPreviewActivity.class);
                        intent.putExtra("TITLE", photo.getFilename());
                        intent.putExtra("FILE_PATH", photo.getFilePath());
                        startActivity(intent);
                    }
                }
            }
        });

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
           @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (isPresent) {
                                Fragment fragment = new SingleResultsListFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .commit();
                            }else{
                                Fragment fragment = new HomeFragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.content_frame, fragment)
                                        .commit();
                            }
                            return true;
                    }
                }
                return false;
            }
        });
        return rootView;
    }

    public void evaluatePhoto(){
        rel_layout_eval.setVisibility(View.GONE);
        try {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            switch(selectedId){
                case R.id.radioOutdoorLandscape:
                    photo.setInputCategory("Outdoor Day - Landscape");
                    break;
                case R.id.radioIndoor:
                    photo.setInputCategory("Indoor - Wall Hanging");
                    break;
                case R.id.radioOutdoorNight:
                    photo.setInputCategory("Outdoor Night - Landmark");
                    break;
                default:
                    break;
            }
            photo.setFilename(filename);
            photo.setFilePath(filepath);
            photo.setState(SinglePhoto.State.UNANALYZED);
            resultBrowser.addResult(filename, photo);
            resultIndex= resultBrowser.getResults().size()-1;
            try {
                Intent startServiceIntent = new Intent();
                startServiceIntent.setClass(getActivity(), PhotoInspectorService.class);
                startServiceIntent.setAction(PhotoInspectorService.INSPECT_SINGLE_PHOTO_ACTION);
                startServiceIntent.putExtra(PhotoInspectorService.RESULT_INDEX, resultIndex);
                getActivity().startService(startServiceIntent);
            } catch (Exception e) {
            }
        } catch (Exception e) {
            Log.e("", "Error while updating photo", e);
        }
    }
    public void checkState(SinglePhoto photo)
    {
        switch(photo.getState()) {
            case UNANALYZED:
                tv_status.setText("");
                busyRing.setVisibility(View.INVISIBLE);
                break;
            case NO_PHOTO:
                busyRing.setVisibility(View.INVISIBLE);
                rel_layout_upload.setVisibility(View.VISIBLE);
                rel_layout_upload_State.setVisibility(View.INVISIBLE);
                break;
            case UPLOAD_STARTED:
                busyRing.setVisibility(View.VISIBLE);
                rel_layout_upload.setVisibility(View.INVISIBLE);
                rel_layout_upload_State.setVisibility(View.VISIBLE);
                tv_status.setText(org.vqeg.viqet.R.string.uploading);
                tv_status.setTextColor(Color.GREEN);
                break;
            case ANALYSIS_STARTED:
                rel_layout_upload.setVisibility(View.INVISIBLE);
                rel_layout_upload_State.setVisibility(View.VISIBLE);
                tv_status.setText(org.vqeg.viqet.R.string.checking);
                tv_status.setTextColor(Color.GREEN);
                break;
            case UPLOAD_FAILED:
                rel_layout_upload.setVisibility(View.INVISIBLE);
                rel_layout_upload_State.setVisibility(View.VISIBLE);
                tv_status.setText("Upload Failed");
                tv_status.setTextColor(Color.RED);
                break;
            case ANALYSIS_FAILED:
                rel_layout_upload.setVisibility(View.INVISIBLE);
                rel_layout_upload_State.setVisibility(View.VISIBLE);
                ConnectivityManager cm =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();
                if (isConnected) {
                    tv_status.setText("Analysis Failed");
                    tv_status.setTextColor(Color.RED);
                } else {
                    tv_status.setText("No network connection");
                    tv_status.setTextColor(Color.RED);
                }
                break;
            case ANALYSIS_INPROGRESS:
                rel_layout_upload.setVisibility(View.INVISIBLE);
                rel_layout_upload_State.setVisibility(View.VISIBLE);
                tv_status.setText(org.vqeg.viqet.R.string.processing);
                tv_status.setTextColor(Color.GREEN);
                tv_status.setVisibility(TextView.VISIBLE);
                break;
            case ANALYSIS_COMPLETE:
                rel_layout_upload.setVisibility(View.INVISIBLE);
                rel_layout_upload_State.setVisibility(View.INVISIBLE);
                rel_layout_result_State.setVisibility(View.VISIBLE);
                busyRing.setVisibility(View.INVISIBLE);
                PhotoDetails photoDetails = new PhotoDetails(photo);
                List<PhotoDetail> photoDetailList = new ArrayList<>(photoDetails.getSinglePhoto().getPhotoDetailMap().values());
                double photo_mos = 0;
                for (PhotoDetail featureDetail : photoDetailList) {
                    if (featureDetail.getParameterName().equals("MOS")) {
                        photo_mos = featureDetail.getValue();
                        break;
                    }
                }
                DecimalFormat df1 = new DecimalFormat("#0.0");
                singleOverallMOS.setText(""+df1.format(photo_mos)+" / 5.0");
                mosRatingBar.setVisibility(View.VISIBLE);
                mosRatingBar.setRating(Float.parseFloat(df1.format(photo_mos)));
                image_name.setVisibility(View.VISIBLE);
                image_name.setText("" + photo.getFilename());
                tv_status.setVisibility(TextView.INVISIBLE);
                break;
            case UPLOAD_COMPLETE:
                tv_status.setText("Upload Complete");
                tv_status.setTextColor(Color.GREEN);
                break;
        }
    }

    @Override
    public void onResume()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(PhotoInspectorService.BROADCAST_PHOTO_STATUS_CHANGE);
        getActivity().registerReceiver(receiver, filter);
        super.onResume();
    }

    @Override
    public void onPause() {
        getActivity().unregisterReceiver(receiver);
        super.onPause();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == PhotoInspectorService.BROADCAST_SINGLE_PHOTO_STATUS_CHANGE) {
                int resultId = intent.getIntExtra("RESULT_INDEX", -1);

                //If the broadcast is for the current result being displayed, invalidate the view
                if (resultIndex == resultId) {
                    checkState(photo);
                }
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        if (requestCode == PHOTO_FROM_CAMERA || requestCode == PHOTO_FROM_GALLERY) {
            if (resultCode == getActivity().RESULT_OK) {
                filepath = returnIntent.getStringExtra("FILE_PATH");
                filename = returnIntent.getStringExtra("FILE_NAME");
                if (filepath != null && filename != null) {
                    new ImageLoadTask(image, filepath).execute();
                    rel_layout_eval.setVisibility(View.VISIBLE);
                    rel_layout_upload.setVisibility(View.GONE);
                }
            }
        }
    }
}