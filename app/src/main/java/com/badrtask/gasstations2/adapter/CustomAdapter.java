package com.badrtask.gasstations2.adapter;

import android.view.View;

import java.util.ArrayList;

import android.view.ViewGroup;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;

import com.badrtask.gasstations2.R;
import com.badrtask.gasstations2.pojos.Place;


/**
 * Created by ITIain on 6/17/2017.
 */

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Place> GasStationList;
    private static LayoutInflater inflater = null;

    /**
     * @param context
     * @param GasStationList
     */
    public CustomAdapter(Context context, ArrayList<Place> GasStationList) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.GasStationList = GasStationList;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * @return
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return GasStationList.size();
    }

    /**
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * @param position
     * @return
     */
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.list_item, null);
        holder.tv = (TextView) rowView.findViewById(R.id.name);
        holder.ts = (TextView) rowView.findViewById(R.id.rating);

        holder.tv.setText(GasStationList.get(position).getName());
       // holder.ts.setText(String.valueOf((int) GasStationList.get(position).getDistance()));
        return rowView;
    }

    /**
     *
     */
    public class Holder {
        TextView tv;
        ImageView img;
        TextView ts;

    }

}