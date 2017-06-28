package com.badrtask.gasstations2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.badrtask.gasstations2.dialogmanager.AlertDialogManager;
import com.badrtask.gasstations2.locationmanager.GPSTracker;
import com.badrtask.gasstations2.networkmanager.ConnectionDetector;

public class SplashScreen extends AppCompatActivity {

    // Connection detector class
    private ConnectionDetector cd;

    // GPS Location
    private GPSTracker gps;

    // flag for Internet connection status
    private Boolean isInternetPresent = false;

    // Alert Dialog Manager
    private AlertDialogManager alert = new AlertDialogManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        /// Check for a connection .
        cd = new ConnectionDetector(getApplicationContext());
        // creating GPS Class object
        gps = new GPSTracker(this);

        isInternetPresent = cd.isConnectingToInternet();

        Thread timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!isInternetPresent) {
                        // Internet Connection is not present
                        alert.showAlertDialog(SplashScreen.this, "Network", "There is no INternet connection", true);
                        // stop executing code by return
                    } else {
                        // check if GPS location can get
                        if (!gps.canGetLocation()) {// Startt of if
                            // Can't get user's current location
                            gps.showSettingsAlert();
                        }/// End of if
                        else {
                            Thread.sleep(5000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    alert.showAlertDialog(getApplicationContext(), "Phone error", "Memmory Used", true);
                } finally {
                    Intent gotoMain = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(gotoMain);
                }
            }
        });
        timer.start();
    }/// End of OnCreate.

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "permission was granted", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getApplicationContext(), "permission denied the app not going to work", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                100);
                    }
                }///End of else.
                return;
            }/// End of Case.
        }/// End of Switch.
    }// End of onRequestPermissionsResult Method.
}/// End of Splash Screen
