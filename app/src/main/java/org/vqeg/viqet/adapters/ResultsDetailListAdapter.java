/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.vqeg.viqet.R;
import org.vqeg.viqet.data.Category;
import org.vqeg.viqet.data.FeatureDetail;
import org.vqeg.viqet.logic.ResultView;

import java.text.DecimalFormat;

/**
 * Created by rkalidin on 8/11/2015.
 */
public class ResultsDetailListAdapter extends BaseExpandableListAdapter
{
    private ResultView resultView;
    private Activity activity;

    public ResultsDetailListAdapter(Activity activity, ResultView resultView)
    {
        this.activity = activity;
        this.resultView=resultView;
    }

    @Override
    public Object getChild(int arg0, int arg1)
    {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {

            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.result_view_category_detail_item, parent, false);

            FeatureDetail featureDetail = resultView.getCategoryList().get(groupPosition).FeatureDetails().get(childPosition);

            //Set Feature Name
            TextView featureNameTextView = (TextView) view.findViewById(R.id.featureDetailName);
            featureNameTextView.setText(featureDetail.getParameterName());

            //Set Feature Value
            TextView featureValueTextView = (TextView) view.findViewById(R.id.featureDetailValue);
            featureValueTextView.setText(new DecimalFormat("#0.00").format(featureDetail.getValue()));

            TextView featureVariabilityTextView = (TextView) view.findViewById(R.id.featureDetailVariability);
            featureVariabilityTextView.setText("Variability: " + new DecimalFormat("#0.00").format(featureDetail.getConfidenceInterval95()));

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return resultView.getCategoryList().get(groupPosition).FeatureDetails().size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return null;
    }

    @Override
    public int getGroupCount()
    {
        return resultView.getCategoryList().size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.result_view_category_item, parent, false);

            Category category = resultView.getCategoryList().get(groupPosition);

            FeatureDetail mosFeature = resultView.getCategoryMOS(category);

            ImageView group_icon = (ImageView) view.findViewById(R.id.group_icon);
            int imageResourceId = isExpanded ? R.drawable.forward_arrow
                    : R.drawable.down_arrow;
            group_icon.setImageResource(imageResourceId);

            //Set Category Name
            TextView categoryNameTextView = (TextView) view.findViewById(R.id.categoryName);
            categoryNameTextView.setText(category.getName());

            //Set Feature Value
            TextView CategoryMOStextView = (TextView) view.findViewById(R.id.categoryMOS);
            CategoryMOStextView.setText(new DecimalFormat("#0.0").format(mosFeature.getValue()));

            TextView CategoryMOSVariabilitytextView = (TextView) view.findViewById(R.id.categoryMOSVariability);
            CategoryMOSVariabilitytextView.setText("Variability: "+new DecimalFormat("#0.0").format(mosFeature.getStandardError()));

            return view;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);

    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1)
    {
        return false;
    }

}
