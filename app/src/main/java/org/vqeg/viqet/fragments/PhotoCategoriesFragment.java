/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.activities.InstructionsActivity;
import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.activities.PhotoDetailActivity;
import org.vqeg.viqet.activities.PhotoPreviewActivity;
import org.vqeg.viqet.activities.ResultDetailActivity;
import org.vqeg.viqet.adapters.ImageAdapter;
import org.vqeg.viqet.data.Category;
import org.vqeg.viqet.data.Photo;
import org.vqeg.viqet.data.ResultStore;
import org.vqeg.viqet.data.Version;
import org.vqeg.viqet.logic.ResultBrowser;
import org.vqeg.viqet.logic.SelectPhotos;
import org.vqeg.viqet.services.PhotoInspectorService;

import java.util.List;

/**
 * Created by Rohit Kalidindi on 7/30/15.
 */

public class PhotoCategoriesFragment extends Fragment {

    private static final int FETCH_PHOTO = 1;

    private List<Photo> photoList;
    private SelectPhotos selectPhotos;
    private List<Category> categoryList;
    private ListView lv_photos;
    private int resultIndex, stepIndex;

    public PhotoCategoriesFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photo_categories, container, false);
        Bundle bundle = getArguments();
        resultIndex = bundle.getInt("RESULT_INDEX", 0);
        stepIndex = bundle.getInt("STEP_INDEX", 0);
        selectPhotos = new SelectPhotos(resultIndex);
        categoryList = selectPhotos.getCategories();
        photoList= categoryList.get(stepIndex).getPhotoList();
        MainActivity.lastClicked=5;
        initUI(rootView);
        return rootView;
    }

    private void initUI(View rootView) {
        TextView tv_step_number;
        ImageButton btn_back, btn_next, btn_results;
        ImageView breadcrumbs;
        ResultBrowser resultBrowser= new ResultBrowser();
        String title= resultBrowser.getResults().get(resultIndex).getName();
        ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(title);
        tv_step_number =(TextView) rootView.findViewById(R.id.tv_step_number);
        tv_step_number.setText((stepIndex+1)+": "+categoryList.get(stepIndex).getName() );
        btn_back= (ImageButton) rootView.findViewById(R.id.btn_back);
        btn_next= (ImageButton) rootView.findViewById(R.id.btn_next);
        btn_results= (ImageButton) rootView.findViewById(R.id.btn_results);
        breadcrumbs = (ImageView) rootView.findViewById(R.id.breadcrumbs);
        switch(stepIndex){
            case 0: breadcrumbs.setImageResource(R.drawable.bc_one_active);
                btn_back.setVisibility(View.INVISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                btn_results.setVisibility(View.INVISIBLE);
                break;
            case 1: breadcrumbs.setImageResource(R.drawable.bc_two_active);
                btn_back.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                btn_results.setVisibility(View.INVISIBLE);
                break;
            case 2: breadcrumbs.setImageResource(R.drawable.bc_three_active);
                btn_back.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                btn_results.setVisibility(View.INVISIBLE);
                break;
            case 3: breadcrumbs.setImageResource(R.drawable.bc_four_active);
                btn_back.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.INVISIBLE);
                btn_results.setVisibility(View.VISIBLE);
                break;
            default: breadcrumbs.setImageResource(R.drawable.bc_one_active);
                btn_back.setVisibility(View.VISIBLE);
                btn_next.setVisibility(View.VISIBLE);
                btn_results.setVisibility(View.INVISIBLE);
                break;
        }
        lv_photos = (ListView) rootView.findViewById(R.id.lv_photos_list);
        ImageAdapter ima= new ImageAdapter(getActivity(), photoList, resultIndex);
        lv_photos.setAdapter(ima);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStepFunction(resultIndex);
            }
        });
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCategoryComplete()){
                    Fragment fragment = new PhotoCategoriesFragment();
                    Bundle args = new Bundle();
                    args.putInt("RESULT_INDEX", resultIndex);
                    args.putInt("STEP_INDEX", (stepIndex + 1));
                    fragment.setArguments(args);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .commit();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.dialog_less_photos_in_category)
                            .setTitle(R.string.dialog_title_less_photos_in_category);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {  }});
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        btn_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isCategoryComplete()){
                    if (selectPhotos.isResultReady(getActivity().getApplicationContext())) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), ResultDetailActivity.class);
                        intent.putExtra("RESULT_INDEX", resultIndex);
                        intent.putExtra("RESULT", selectPhotos.getResult());
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        builder.setMessage(R.string.methodologyNotCompleteError)
                                .setTitle("Analyzing...");
                        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage(R.string.dialog_less_photos_in_category)
                            .setTitle(R.string.dialog_title_less_photos_in_category);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        lv_photos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Photo photoItem = photoList.get(position);
                //PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.select_photos_popup, popup.getMenu());
                //Set the actions when menu items are clicked
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deletePhoto:
                                deletePhoto(photoItem);
                                break;
                            case R.id.takeNewPhoto:
                                takeNewPhoto(photoItem);
                                break;
                            case R.id.viewPhoto:
                                viewPhoto(photoItem);
                                break;
                            case R.id.viewPhotoDetails:
                                viewPhotoDetails(photoItem);
                                break;
                        }
                        return true;
                    }
                });
                //Hide Options that are not relevant
                if (photoItem.getFilePath() == null) {
                    popup.getMenu().findItem(R.id.takeNewPhoto).setVisible(false);
                    popup.getMenu().findItem(R.id.deletePhoto).setVisible(false);
                    popup.getMenu().findItem(R.id.viewPhoto).setVisible(false);
                }
                if (photoItem.getPhotoDetailMap() == null) {
                    popup.getMenu().findItem(R.id.viewPhotoDetails).setVisible(false);
                }
                //Analyze Photo if the photo analysis has not started or failed
                if ((photoItem.getState() == Photo.State.NO_PHOTO) ||
                        (photoItem.getState() == Photo.State.UPLOAD_STARTED) ||
                        (photoItem.getState() == Photo.State.ANALYSIS_STARTED)) {
                }
                //If we can stop the upload or analyzing, we can remove these two stmts:
                if ((photoItem.getState() == Photo.State.UPLOAD_STARTED) ||
                        (photoItem.getState() == Photo.State.ANALYSIS_STARTED)) {
                    popup.getMenu().findItem(R.id.deletePhoto).setVisible(false);
                    popup.getMenu().findItem(R.id.takeNewPhoto).setVisible(false);
                }
                if (photoItem.getState() == Photo.State.ANALYSIS_COMPLETE) {
                }
                //Show the popup menu
                popup.show();
                return true;
            }
        });

        lv_photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Photo photoItem = photoList.get(position);
                switch (photoItem.getState()) {
                    case NO_PHOTO:
                        if(readAccess()==1) {
                            takeNewPhoto(photoItem);
                        }else{
                            showCustomDialog();
                        }
                        break;
                    case UNANALYZED:
                    case UPLOAD_STARTED:
                    case UPLOAD_FAILED:
                    case UPLOAD_COMPLETE:
                    case ANALYSIS_STARTED:
                    case ANALYSIS_FAILED:
                    case ANALYSIS_INPROGRESS:
//                        viewPhoto(photoItem);
                        break;
                    case ANALYSIS_COMPLETE:
                        viewPhotoDetails(photoItem);
                        break;
                }
            }
        });

        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(R.string.no_network_dialog_message)
                    .setTitle(R.string.no_network_dialog_title);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) {  }});
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {


            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN){
                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(stepIndex>0) {
                        backStepFunction(resultIndex);
                    }else{
                        Fragment fragment = new ResultsListFragment();
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
    }

    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.text_switch).setMessage("Please allow access to upload test photos for quality evaluation");
        builder.setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Fragment fragment = new SettingsFragment();
                FragmentManager fragmentManager4 = getFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.content_frame, fragment, "Settings Fragment").commit();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public int readAccess(){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        int license = sharedPref.getInt(getString(R.string.licenseAcceptance), -1);
        return license;
    }
    public void updateAccess(int accepted){
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.licenseAcceptance), accepted);
        editor.apply();
    }

    private boolean isCategoryComplete(){
        photoList= categoryList.get(stepIndex).getPhotoList();
        for(int i=0;i<photoList.size();i++){
            if(photoList.get(i).getState().toString().equals("NO_PHOTO")){
                return false;
            }
        }
        return true;
    }

    private void backStepFunction(int resultIndex) {
        Fragment fragment = new PhotoCategoriesFragment();
        Bundle args = new Bundle();
        args.putInt("RESULT_INDEX", resultIndex);
        args.putInt("STEP_INDEX", (stepIndex - 1));
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
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

            if (action == PhotoInspectorService.BROADCAST_PHOTO_STATUS_CHANGE) {
                int resultIndex = intent.getIntExtra("RESULT_INDEX", -1);
                int photoIndex = intent.getIntExtra("PHOTO_INDEX", -1);

                //If the broadcast is for the current result being displayed, invalidate the view
                if (selectPhotos.getResultIndex() == resultIndex) {
                    lv_photos.invalidateViews();

                    //Check if the cloud version has changed. If so, initiate re-processing
                    Photo photo = ResultStore.GetResultStore().getResults().get(resultIndex).getPhotoList().get(photoIndex);
                    if (photo.getState() == Photo.State.ANALYSIS_COMPLETE) {
                        boolean majorVersionChange = checkForVersionChangeAndReprocess(photo.getCloudVersion());
                        if (!majorVersionChange) {
                            photo.setState(Photo.State.ANALYSIS_FAILED);
                            ResultStore.GetResultStore().persist();
                        }
                    }
                }
            }
        }
    };

    private void takeNewPhoto(Photo photo)
    {
        Intent intent = new Intent();
        intent.setClass(getActivity(), InstructionsActivity.class);
        intent.putExtra("RESULT_INDEX", selectPhotos.getResultIndex());
        intent.putExtra("PHOTO_INDEX", selectPhotos.getPhotoIndex(photo));
        intent.putExtra("CATEGORY", photo.getInputCategory());
        startActivityForResult(intent, FETCH_PHOTO);
    }

    private void viewPhoto(Photo photo)
    {
        if(photo.getFilePath() != null)
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PhotoPreviewActivity.class);
            intent.putExtra("TITLE", photo.getFilename());
            intent.putExtra("FILE_PATH", photo.getFilePath());
            startActivity(intent);
        }
    }

    private void viewPhotoDetails(Photo photo)
    {
        if(photo.getPhotoDetailMap() != null)
        {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PhotoDetailActivity.class);
            intent.putExtra("PHOTO_ID", photo);
            intent.putExtra("RESULT_ID", resultIndex);
            intent.putExtra("STEP_ID", stepIndex);
            startActivity(intent);
        }
    }

    private void deletePhoto(final Photo photo)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.deletePhotoPopupTitle);
        builder.setMessage(getResources().getString(R.string.deletePhotoPopupMessage) + " '" + photo.getFilename() + "'");

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                String key = photo.getFilePath();
                if (isBitmapInMemCache(key)) {
                    MainActivity.mMemoryCache.remove(key);
                }
                selectPhotos.deletePhoto(photo);
                photoList= categoryList.get(stepIndex).getPhotoList();
                ImageAdapter ima= new ImageAdapter(getActivity(), photoList, resultIndex);
                lv_photos.setAdapter(ima);
                lv_photos.invalidateViews();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }

    public boolean isBitmapInMemCache(String key) {
        if(MainActivity.mMemoryCache.get(key)==null){
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnIntent)
    {
        if (requestCode == FETCH_PHOTO)
        {
            // Make sure the request was successful
            if (resultCode == -1)
            {
                String filepath = returnIntent.getStringExtra("FILE_PATH");
                String filename = returnIntent.getStringExtra("FILE_NAME");
                int photoIndex = returnIntent.getIntExtra("PHOTO_INDEX", -1);

                if(photoIndex >= 0 && filepath != null && filename != null)
                {
                    //Update the photo object
                    selectPhotos.updatePhoto(photoIndex, filename, filepath);
                    lv_photos.invalidateViews();
                    //Start inspecting the photo
                    selectPhotos.InspectPhoto(getActivity(), photoIndex);   //call to process photo

                }
            }
        }
    }

    private boolean checkForVersionChangeAndReprocess(Version version)
    {
//        Version currentVersion = RemoteInfoProvider.getRemoteInfo().getVersion();
//        if(version.getMethodologyVersion() != currentVersion.getMethodologyVersion() || version.getAlgoVersion() != currentVersion.getAlgoVersion())
//        {
//            ReProcessPhotos();
//            RemoteInfoProvider.getRemoteInfo().setVersion(version);
//            return false;
//        }
            //Signal error since Major versions are different
            return true;
    }

}