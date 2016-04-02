package org.vqeg.viqet.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.activities.MainActivity;
import org.vqeg.viqet.adapters.CustomSingleResultListAdapter;
import org.vqeg.viqet.data.PhotoDetail;
import org.vqeg.viqet.logic.PhotoDetails;
import org.vqeg.viqet.singlephotodata.SingleResult;
import org.vqeg.viqet.singlephotologic.SinglePhotoResultBrowser;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkalidin on 3/26/2016.
 */
public class SingleResultsListFragment extends Fragment {
    private SinglePhotoResultBrowser resultBrowser;
    private SingleResult result;
    private ListView results;
    private ArrayList<String> resultMOSScoreList;
    private TextView tv_heading;

    public SingleResultsListFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(org.vqeg.viqet.R.layout.fragment_result, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Photo Quality Results");
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
        ListAdapter adapter = new CustomSingleResultListAdapter(getActivity(), org.vqeg.viqet.R.layout.list_results_item, resultBrowser.getResults(),resultMOSScoreList);

        tv_heading= (TextView) rootView.findViewById(R.id.tv_heading);
        tv_heading.setText("Rating");
        results = (ListView) rootView.findViewById(org.vqeg.viqet.R.id.list_of_results);
        results.setAdapter(adapter);
        results.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = new UploadSinglePhotoFragment();
                Bundle args = new Bundle();
                args.putInt("RESULT_INDEX", position);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(org.vqeg.viqet.R.id.content_frame, fragment)
                        .commit();
            }
        });

    }

    private class ResultsFetchTask extends AsyncTask<Void,Void,Void> {

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
            resultBrowser = new SinglePhotoResultBrowser();
            int sizeOfResults = resultBrowser.getResults().size();
            resultMOSScoreList= new ArrayList<>();

            for(int i=0; i<sizeOfResults;i++){

                SingleResult res = resultBrowser.getResults().get(i);
                PhotoDetails photoDetails = new PhotoDetails(res.getPhoto());
                if(!(photoDetails.getSinglePhoto().getPhotoDetailMap() == null)){
                List<PhotoDetail> aggregatedDetails =new ArrayList<PhotoDetail>( photoDetails.getSinglePhoto().getPhotoDetailMap().values());

                    for(int j=0;j<aggregatedDetails.size();j++)
                    {
                        PhotoDetail featureDetail = aggregatedDetails.get(j);
                        if(featureDetail.getParameterName().equalsIgnoreCase("MOS"))
                        {
                            if(featureDetail.getValue()!=0.0) {
                                DecimalFormat df1 = new DecimalFormat("#0.0");
                                String result = df1.format(featureDetail.getValue());
                                resultMOSScoreList.add(result);
                            }else{
                                resultMOSScoreList.add("--");
                            }
                        }
                    }
                }else{
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
