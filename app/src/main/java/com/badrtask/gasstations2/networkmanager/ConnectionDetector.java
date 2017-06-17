package com.badrtask.gasstations2.networkmanager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ahmed Osama  on 6/16/2017.
 */
public class ConnectionDetector {

    /// This will be the Context of the activity that going to call isConnectingToInternet method.
    private Context _context;

    /**
     * @param context
     */
    public ConnectionDetector(Context context) {
        this._context = context;
    }

    /**
     * Checking for all possible internet providers
     **/
    public boolean isConnectingToInternet() {
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }// end for

        }// end if the
        return false;
    }// End of the method.
}// End of the class.