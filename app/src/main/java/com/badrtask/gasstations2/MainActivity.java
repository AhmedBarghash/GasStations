package com.badrtask.gasstations2;


import android.app.ProgressDialog;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.badrtask.gasstations2.delegate.GasStationDelegate;
import com.badrtask.gasstations2.dialogmanager.AlertDialogManager;
import com.badrtask.gasstations2.locationmanager.GPSTracker;
import com.badrtask.gasstations2.networkmanager.ConnectionDetector;
import com.badrtask.gasstations2.networkmanager.HttpHandler;
import com.badrtask.gasstations2.pojos.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


//// Main Activty implements GasStationDelegate so can parse the List of station to the Fragments
public class MainActivity extends AppCompatActivity implements GasStationDelegate {

    // URL to get contacts JSON
    private static String url;
    // My location.
    private double longitude;
    private double latitude;
    private Location mylocation;
    private Location otherLocation;
    // Tag of the MainActivity to see the Log by the name of the Activty.
    private String TAG = MainActivity.class.getSimpleName();
    /// ProgressDialog
    private ProgressDialog pDialog;
    /// Lsit of Gas Station will pe parsed throw the Delegate.
    private ArrayList<Place> gasStationList;

    // Object from Gas Station Place.
    private Place gasStation;

    // flag for Internet connection status
    private Boolean isInternetPresent = false;

    // Connection detector class
    private ConnectionDetector cd;

    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();

    // GPS Location
    private GPSTracker gps;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /// Check for a connection .
        cd = new ConnectionDetector(getApplicationContext());

        // creating GPS Class object
        gps = new GPSTracker(this);

        mylocation = new Location("point A");
        mylocation.setLatitude(gps.getLatitude());
        mylocation.setLongitude(gps.getLongitude());

        otherLocation = new Location("point B");
        url = "https://maps.googleapis.com/maps/api/place/search/json?location=" + gps.getLatitude() + "," + gps.getLongitude() + "&radius=1000&types=gas_station&key=AIzaSyDGFElSfFOOdyACz355cXbr8ZLY0t2T8pQ";

        gasStationList = new ArrayList<>();

        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
        } else {
            new GasStationList().execute();
        }


        /// This BottomNavigationView used to create a tabs at the end of the
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.action_item1:
                                selectedFragment = PLacesListFragment.newInstance();
                                break;
                            case R.id.action_item2:
                                selectedFragment = PlacesMapFragment.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });
    }

    /**
     * @return
     */
    @Override
    public ArrayList<Place> getGasStationPlacesList() {
        return gasStationList;
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GasStationList extends AsyncTask<Void, Void, Void> {

        /**
         *
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * @param arg0
         * @return
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            // Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < results.length(); i++) {
                        gasStation = new Place();
                        JSONObject c = results.getJSONObject(i);

                        String name = c.getString("name");
                        JSONObject geometry = c.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        otherLocation.setLatitude(lat);
                        otherLocation.setLongitude(lng);
                        gasStation.setName(name);
                        gasStation.setDistance(mylocation.distanceTo(otherLocation));
                        gasStation.setLat(lat);
                        gasStation.setLng(lng);

                        gasStationList.add(gasStation);
                        System.out.println("---------------->Name is " + name + "\t lat: " + lat + "\t lat: " + lng + "\n");
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert.showAlertDialog(getApplicationContext(), "Something Wrong with the Server"
                                    , "Make Sure your GPS and Internet Working First and then reopen the App", true);
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        alert.showAlertDialog(getApplicationContext(), "Something Wrong with the Server"
                                , "Make Sure your GPS and Internet Working First and then reopen the App", true);
                    }
                });

            }

            return null;
        }

        /**
         * @param result
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            //Manually displaying the first fragment - one time only

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, PLacesListFragment.newInstance());
            transaction.commit();
        }
    }
}
