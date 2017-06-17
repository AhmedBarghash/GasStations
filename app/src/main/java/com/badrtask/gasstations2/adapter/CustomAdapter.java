package com.badrtask.gasstations2.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.badrtask.gasstations2.R;
import com.badrtask.gasstations2.pojos.Place;

import java.util.ArrayList;


/**
 * Created by ITIain on 6/17/2017.
 */

public class CustomAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Location mylocation;
    private Location otherLocation;
    private Context context;
    private ArrayList<Place> GasStationList;

    /**
     * @param context
     * @param GasStationList
     */
    public CustomAdapter(Context context, ArrayList<Place> GasStationList, Location location) {
        // TODO Auto-generated constructor stub
        this.mylocation = location;
        this.context = context;
        this.GasStationList = GasStationList;
        otherLocation = new Location("PLace Location");
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
        holder.ts.setText(String.valueOf((int) GasStationList.get(position).getDistance()) + "meter");


        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                otherLocation.setLongitude(GasStationList.get(position).getLng());
                otherLocation.setLatitude(GasStationList.get(position).getLat());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + GasStationList.get(position).getLat() + "," + GasStationList.get(position).getLng()));
                context.startActivity(intent);
            }
        });
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