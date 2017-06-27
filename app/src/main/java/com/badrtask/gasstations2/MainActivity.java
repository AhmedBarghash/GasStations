package com.badrtask.gasstations2;


import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.badrtask.gasstations2.constants.ConstantsClass;
import com.badrtask.gasstations2.delegate.GasStationDelegate;
import com.badrtask.gasstations2.dialogmanager.AlertDialogManager;
import com.badrtask.gasstations2.locationmanager.GPSTracker;
import com.badrtask.gasstations2.networkmanager.ConnectionDetector;
import com.badrtask.gasstations2.networkmanager.interfaces.ApiInterface;
import com.badrtask.gasstations2.pojos.PlacesPOJO;
import com.badrtask.gasstations2.pojos.Result;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


//// Main Activity implements GasStationDelegate so can parse the List of station to the Fragments
public class MainActivity extends AppCompatActivity implements GasStationDelegate, Callback<PlacesPOJO> {


    // My location.
    private Location mylocation;
    private Location otherLocation;

    /// Lsit of Gas Station will pe parsed throw the Delegate.
    private ArrayList<Result> gasStationList;

    // flag for Internet connection status
    private Boolean isInternetPresent = false;

    // Connection detector class
    private ConnectionDetector cd;

    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();

    // GPS Location
    private GPSTracker gps;

    /// ProgressDialog
    private ProgressDialog pDialog;

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

        gasStationList = new ArrayList<>();
        // Check if Internet present
        isInternetPresent = cd.isConnectingToInternet();
        if (!isInternetPresent) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this, "Network", "There is no INternet connection", true);
            // stop executing code by return
        } else {
            // check if GPS location can get
            if (!gps.canGetLocation()) {// Startt of if
                // Can't get user's current location
//                alert.showAlertDialog(MainActivity.this,"GPS is settings","There is no INternet connection",true);
                gps.showSettingsAlert();
            }/// End of if
            else {
                // Showing progress dialog
                pDialog = new ProgressDialog(MainActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

                /// Start to connect to Google APi Using retrofit.
                start();
            }
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

    /// Applying the retrofit to connect to thr Google APi and get the data of the places
    public void start() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantsClass.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface gerritAPI = retrofit.create(ApiInterface.class);
        // calling the method do place and pass the current location of me anf place
        Call<PlacesPOJO> call = gerritAPI.doPlaces(gps.getLatitude() + "," + gps.getLongitude(), ConstantsClass.RADIUS, ConstantsClass.PLACE, ConstantsClass.GOOGLE_PLACE_API_KEY);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<PlacesPOJO> call, Response<PlacesPOJO> response) {

        /// In case of there is a responce will dismiss the Progress bar.
        if (pDialog.isShowing())
            pDialog.dismiss();

        /// Get the list of results form the response
        gasStationList = response.body().getResults();
        otherLocation = new Location("point B");
        // For loop to calculate  the distance of every location
        for (int i = 0; i < gasStationList.size(); i++) {
            otherLocation.setLatitude(response.body().getResults().get(i).getGeometry().getLocation().getLat());
            otherLocation.setLongitude(response.body().getResults().get(i).getGeometry().getLocation().getLng());
            gasStationList.get(i).setDistance(mylocation.distanceTo(otherLocation));
        }
        // Show the Fragments after loading the data.
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, PLacesListFragment.newInstance());
        transaction.commit();
    }

    @Override
    public void onFailure(Call<PlacesPOJO> call, Throwable t) {
        alert.showAlertDialog(MainActivity.this, "Server is Down Error",
                "Please connect to working Internet connection and re Open the App", false);
    }

    @Override
    public ArrayList<Result> getGasStationPlacesList() {
        return gasStationList;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            gps.stopUsingGPS();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(), "permission was granted", Toast.LENGTH_SHORT).show();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Showing progress dialog
                    pDialog = new ProgressDialog(MainActivity.this);
                    pDialog.setMessage("Please wait...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                    /// Start to connect to Google APi Using retrofit.
                    start();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}
