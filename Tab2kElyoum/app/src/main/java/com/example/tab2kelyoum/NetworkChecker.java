package com.example.tab2kelyoum;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;

/**
 * NetworkChecker class to check if the device is connected to the internet.
 * It follows a Singleton pattern to ensure only one instance is used throughout the app.
 */
public class NetworkChecker {

    // Singleton instance of NetworkChecker
    private static NetworkChecker instance = null;

    // ConnectivityManager to check network state
    private ConnectivityManager connectivityManager;


    private NetworkChecker(Context context) {
        // Initialize the ConnectivityManager only if the context is not null
        if (context != null) {
            this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
    }

    /**
     * Provides a global point of access to the NetworkChecker instance.
     * It initializes the instance if it hasn't been created yet.
     */
    public static NetworkChecker getInstance(Context context) {
        if (instance == null) {
            // Create a new instance if it doesn't exist, using the provided context
            instance = new NetworkChecker(context);
        }
        return instance;
    }


    /**
     * Checks if the device is currently connected to the internet.
     * return True if the device is connected to the internet, false otherwise.
     */
    public boolean checkIfInternetIsConnected() {
        // Retrieve the active network information
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Return true if the device is connected to the internet, false otherwise
        return networkInfo != null && networkInfo.isConnected();
    }
}
