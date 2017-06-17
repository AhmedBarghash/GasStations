package com.badrtask.gasstations2;

import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;
import android.view.ViewGroup;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.support.v4.app.Fragment;
import com.badrtask.gasstations2.pojos.Place;
import com.badrtask.gasstations2.adapter.CustomAdapter;
import com.badrtask.gasstations2.locationmanager.GPSTracker;

public class PLacesListFragment extends Fragment {

    private View view;

    /// Array list will be in the listview.
    private ArrayList<Place> gasStationList;

    // GPS Location to make sure the user didn't close the GPS.
    private GPSTracker gps;

    // CustomAdapter For the List.
    private CustomAdapter customAdapterobj;

    // List view will show up in the Fragmetn.
    private ListView listview;

    // Reference from MainActivity to call the Method GetGasStatioList.
    private MainActivity activity;


    /**
     *
     * @return
     */
    // Create a Instance from the Fragment for the Navigation bar.
    public static PLacesListFragment newInstance() {
        return new PLacesListFragment();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // creating GPS Class object
        gps = new GPSTracker(getActivity());

        // check if GPS location can get
        if (!gps.canGetLocation()) {// Startt of if
            // Can't get user's current location
            gps.showSettingsAlert();
        }/// End of if

        //This layout contains your list view
        view = inflater.inflate(R.layout.fragment_places_list, container, false);

        //now you must initialize your list view
        listview = (ListView) view.findViewById(R.id.my_listview);

        /// Creating an object;
        activity = (MainActivity) getActivity();

        /// Having the Array list  from the Delegate.
        gasStationList = activity.getGasStationPlacesList();

        /// Create an object from the CustomAdapter i made and pass the Context and the list of the Gas Station Array. to it
        customAdapterobj = new CustomAdapter(getActivity(), gasStationList);

        // Setting the Adapter to the list
        listview.setAdapter(customAdapterobj);

        // Inflate the layout for this fragment
        return view;

    }// End of the onCreateView method.
}// End of the Fragmetn.