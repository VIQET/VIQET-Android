/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.adapters.ResultsDetailListAdapter;
import org.vqeg.viqet.data.FeatureDetail;
import org.vqeg.viqet.data.Result;
import org.vqeg.viqet.logic.ResultView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class ResultDetailActivity extends ActionBarActivity {

    private ResultView resultView;
    private ExpandableListView elv_results;
    private int resultIndex;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_detail);

        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        result = (Result)getIntent().getSerializableExtra("RESULT");
        resultIndex = getIntent().getIntExtra("RESULT_INDEX",0);

        initialize();
    }

    private void initialize() {
        getSupportActionBar().setTitle(""+result.getName());
        if(result != null)
        {
            this.resultView = new ResultView(result);
        }
        //Set the Overall Score
        FeatureDetail mos = resultView.getOverallMOS();
        if(mos != null)
        {
            //Set The MOS
            TextView overallMOSTextView = (TextView) findViewById(R.id.overallMOS);
            DecimalFormat df = new DecimalFormat("#0.0");
            df.setRoundingMode(RoundingMode.DOWN);
            overallMOSTextView.setText(df.format(mos.getValue()));
            DecimalFormat df1 = new DecimalFormat("#0.0");
            df1.setRoundingMode(RoundingMode.DOWN);
            //Set Confidence Interval
            TextView overallMOSCITextView = (TextView) findViewById(R.id.overallMOSCI);
            overallMOSCITextView.setText(df1.format(mos.getStandardError()));
            //Set the MOS Rating Stars
            RatingBar mosRatingBar = (RatingBar) findViewById(R.id.mosRatingBar);
            mosRatingBar.setRating((float)mos.getValue());
        }
        //get reference to the ExpandableListView and connect it to an adapter
        this.elv_results = (ExpandableListView) findViewById(R.id.elv_results_detail);
        ResultsDetailListAdapter resultsListAdapter = new ResultsDetailListAdapter(ResultDetailActivity.this, resultView);
        this.elv_results.setAdapter(resultsListAdapter);
        ImageButton btn_details = (ImageButton) findViewById(R.id.details);
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass( ResultDetailActivity.this,PhotoResultActivity.class);
                intent.putExtra("RESULT_INDEX", resultIndex);
                startActivity(intent);
            }
        });
    }
}
