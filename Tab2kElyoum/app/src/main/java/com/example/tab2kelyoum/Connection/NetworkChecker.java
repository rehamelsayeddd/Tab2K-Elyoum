package com.example.tab2kelyoum.Connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * NetworkChecker class to check if the device is connected to the internet.
 * It follows a Singleton pattern to ensure only one instance is used throughout the app.
 */
public class NetworkChecker {

    private static Context context;
    public static NetworkChecker instance = null;

    private NetworkChecker() {

    }

    public static NetworkChecker getInstance(Context contextInput) {
        if (instance == null) {
            context = contextInput;
            instance = new NetworkChecker();
        }
        return instance;
    }

    public static NetworkChecker getInstance() {
        if (instance == null) {
            instance = new NetworkChecker();
        }
        return instance;
    }


    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

    public Boolean checkIfInternetIsConnected() {
        return ((connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED));
    }

}
