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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDrawerListAdapter extends BaseAdapter{

    private final Context context;
    private String[] mOptionsTitles;
    private int[] mImageResources;
    private ImageView iv_icon;
    private TextView tv_name;

    public int getCount() {
        return this.mOptionsTitles.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public CustomDrawerListAdapter(Context context, String[] mOptionsTitles,int[] mImageResources)
    {
        this.context = context;
        this.mOptionsTitles = mOptionsTitles;
        this.mImageResources=mImageResources;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(org.vqeg.viqet.R.layout.drawer_item, parent, false);
            tv_name = (TextView) convertView.findViewById(org.vqeg.viqet.R.id.drawer_text);
            tv_name.setText(mOptionsTitles[position]);
            iv_icon = (ImageView) convertView.findViewById(org.vqeg.viqet.R.id.drawer_icon);
            iv_icon.setImageResource(mImageResources[position]);

        return convertView;
    }
}