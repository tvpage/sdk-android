package com.tvpage.lib.utils;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.tvpage.lib.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by MTPC-110 on 12/15/2016.
 */

public class SpinnerAdapterQuality extends ArrayAdapter {

    private ArrayList<HashMap<String, String>> mData;

    private LayoutInflater mInflater;
    //private Activty mainActivity;


    public SpinnerAdapterQuality(

            Context activitySpinner,
            int textViewResourceId,
            ArrayList<HashMap<String, String>> objects

    ) {
        super(activitySpinner, textViewResourceId, objects);

        mData = objects;
        mInflater = (LayoutInflater) activitySpinner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mainActivity = (MainActivity) activitySpinner;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = mInflater.inflate(R.layout.adapter_spinner_quality, parent, false);
        TextView label = (TextView) row.findViewById(R.id.list_item);
        RelativeLayout relativeSpinnerParent = (RelativeLayout) row.findViewById(R.id.relativeSpinnerParent);

        HashMap<String, String> item = mData.get(position);

        //Log.d("Posss: ", "" + position + " : " + item.size() + " : " + mData.size());

        if (item.size() > 0) {
            for (String key : item.keySet()) {
                if (key != null && key.trim().length() > 0) {
                    label.setText(key);
                }

            }
        }

        //Set meta data here and later we can access these values from OnItemSelected Event Of Spinner
        //row.setTag(R.string.meta_position, Integer.toString(position));
        //row.setTag(R.string.meta_title, mData.get(position).toString());

        return row;
    }


}
