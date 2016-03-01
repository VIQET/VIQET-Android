/*
 * Copyright © 2015 Intel Corporation
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0,
 *  which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html . https://github.com/viqet
 *  Contributors:
 *     Intel Corporation - initial API and implementation and/or initial documentation
 */

package org.vqeg.viqet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.vqeg.viqet.data.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Kalidindi on 7/28/15.
 */
public class CustomResultListAdapter extends ArrayAdapter<Result>
{
    private final Context context;
    private List<Result> resultList;
    private ArrayList<String> resultMOSList;

    public CustomResultListAdapter(Context context, int resource, List<Result> resultList, ArrayList<String> resultMOSList)
    {
        super(context, resource, resultList);
        this.context = context;
        this.resultList = resultList;
        this.resultMOSList=resultMOSList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolderResultList viewHolder;
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(org.vqeg.viqet.R.layout.list_results_item, parent, false);
            viewHolder = new ViewHolderResultList();
            viewHolder.tv_name = (TextView) convertView.findViewById(org.vqeg.viqet.R.id.test_name);
            viewHolder.tv_score = (TextView) convertView.findViewById(org.vqeg.viqet.R.id.test_result);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolderResultList)convertView.getTag();
        }
        viewHolder.tv_name.setText(this.resultList.get(position).getName());
        viewHolder.tv_score.setText(""+resultMOSList.get(position));

        return convertView;
    }
}
