package com.tvpage.lib.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.tvpage.lib.R;

import java.util.ArrayList;

/**
 * Created by MTPC-110 on 12/15/2016.
 */

public class SpinnerAdapterProductCateg extends ArrayAdapter {

    private ArrayList<String> mData;

    private LayoutInflater mInflater;
    //private Activty mainActivity;


    public SpinnerAdapterProductCateg(

            Context activitySpinner,
            int textViewResourceId,
            ArrayList<String> objects

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

        View row = mInflater.inflate(R.layout.adapter_spinner_product_cat, parent, false);
        TextView label = (TextView) row.findViewById(R.id.list_item);

        if (mData.get(position) != null) {
            label.setText(mData.get(position));
        }
        //Log.d("Posss: ", "" + position + " : " + item.size() + " : " + mData.size());


        //Set meta data here and later we can access these values from OnItemSelected Event Of Spinner
        //row.setTag(R.string.meta_position, Integer.toString(position));
        //row.setTag(R.string.meta_title, mData.get(position).toString());

        return row;
    }


}
