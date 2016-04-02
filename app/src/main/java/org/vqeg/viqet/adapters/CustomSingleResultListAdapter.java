package org.vqeg.viqet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.vqeg.viqet.singlephotodata.SingleResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rkalidin on 3/26/2016.
 */
public class CustomSingleResultListAdapter extends ArrayAdapter<SingleResult>
{
    private final Context context;
    private List<SingleResult> resultList;
    private ArrayList<String> resultMOSList;

    public CustomSingleResultListAdapter(Context context, int resource, List<SingleResult> resultList, ArrayList<String> resultMOSList)
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
