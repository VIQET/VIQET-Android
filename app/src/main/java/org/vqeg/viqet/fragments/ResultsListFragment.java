/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.fragments;

/**
 * Created by Rohit Kalidindi on 7/30/15.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.activities.ResultDetailActivity;
import org.vqeg.viqet.adapters.CustomResultListAdapter;
import org.vqeg.viqet.data.FeatureDetail;
import org.vqeg.viqet.data.Result;
import org.vqeg.viqet.logic.ResultBrowser;
import org.vqeg.viqet.logic.ResultView;
import org.vqeg.viqet.logic.SelectPhotos;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Results Fragment
 */
public class ResultsListFragment extends Fragment {
    private ResultBrowser resultBrowser;
    private ResultView resultView;
    private SelectPhotos selectPhotos;
    private Result result;
    private ListView results;
    private ArrayList<String> resultMOSScoreList;

    public ResultsListFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(org.vqeg.viqet.R.layout.fragment_result, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Camera Quality Results");
        new ResultsFetchTask(getActivity(),rootView).execute();

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("FRAGMENT",1);
    }

    private void fetchResults(View rootView) {
        // Create an adapter using the item layout and resultBrowser resultsList
        // and assign it to ListView
        ListAdapter adapter = new CustomResultListAdapter(getActivity(), org.vqeg.viqet.R.layout.list_results_item, resultBrowser.getResults(),resultMOSScoreList);

        results = (ListView) rootView.findViewById(org.vqeg.viqet.R.id.list_of_results);
        results.setAdapter(adapter);
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Result result = (Result) parent.getItemAtPosition(position);
                SelectPhotos selectPhotos = new SelectPhotos(position);
                if (selectPhotos.isResultReady(getActivity().getApplicationContext())) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ResultDetailActivity.class);
                    intent.putExtra("RESULT_INDEX", resultBrowser.getIndex(result));
                    intent.putExtra("RESULT", selectPhotos.getResult());
                    startActivity(intent);
                } else {
                    Fragment fragment = new PhotoCategoriesFragment();
                    Bundle args = new Bundle();
                    args.putInt("RESULT_INDEX", resultBrowser.getIndex(result));
                    args.putInt("STEP_INDEX", 0);
                    fragment.setArguments(args);
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(org.vqeg.viqet.R.id.content_frame, fragment)
                            .commit();
                }

            }
        });
        results.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                //PopupMenu
                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(org.vqeg.viqet.R.menu.results_delete_popup, popup.getMenu());
                //Set the actions when menu items are clicked
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        ResultBrowser resultBrowser = new ResultBrowser();
                        Result result = (Result) parent.getItemAtPosition(position);
                        resultBrowser.deleteResult(result);
                        ListAdapter adapter = new CustomResultListAdapter(getActivity(), org.vqeg.viqet.R.layout.list_results_item, resultBrowser.getResults(), resultMOSScoreList);
                        results.setAdapter(adapter);
                        results.invalidateViews();
                        return true;
                    }
                });

                //Show the popup menu
                popup.show();
                return true;
                    }
                });

    }

    private class ResultsFetchTask extends AsyncTask<Void,Void,Void>{

        private ProgressDialog mProgressDialog;
        private View rootView;

        public ResultsFetchTask(Context context, View rootView){
            this.rootView=rootView;
            mProgressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage(getResources().getString(org.vqeg.viqet.R.string.dialog_message_fetch_results));
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            resultBrowser = new ResultBrowser();

            int sizeOfResults = resultBrowser.getResults().size();
            resultMOSScoreList= new ArrayList<>();

            for(int i=0; i<sizeOfResults;i++){
                selectPhotos=new SelectPhotos(i);
                result=selectPhotos.getResult();

                if(result != null)
                {
                    resultView = new ResultView(result);

                    //Set the Overall Score
                    FeatureDetail mos = resultView.getOverallMOS();

                    if(selectPhotos.isResultReady(getActivity())) {
                        DecimalFormat df1 = new DecimalFormat("#0.0");
                        df1.setRoundingMode(RoundingMode.DOWN);
                        String result = df1.format(mos.getValue()) + " +/- " + df1.format(mos.getStandardError());
                        resultMOSScoreList.add(result);
                    }else{
                        resultMOSScoreList.add("--");
                    }
                }
                else{
                    resultMOSScoreList.add("--");
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            fetchResults(rootView);
            mProgressDialog.dismiss();
        }
    }
}
