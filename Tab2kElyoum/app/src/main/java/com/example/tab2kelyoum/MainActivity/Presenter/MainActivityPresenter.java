package com.example.tab2kelyoum.MainActivity.Presenter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.Home.View.TodayPlannerAdapter;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivityPresenter {
    private Context context;
    private RepoistryLocal repositoryLocal;
    private RepoistryRemote repositoryRemote;
    private InterfaceMain interfaceMain;
    private NetworkChecker networkChecker;

    public MainActivityPresenter(Context context, InterfaceMain interfaceMain) {
        this.context = context;
        this.interfaceMain = interfaceMain;
        this.repositoryLocal = new RepoistryLocal(context);
        this.networkChecker = NetworkChecker.getInstance(context);
        this.repositoryRemote = new RepoistryRemote(interfaceMain);
    }

    public void deleteTableRoom() {
        new Thread(() -> repositoryLocal.deleteTableRoom()).start();
    }

    public void deleteAccountData() {
        repositoryRemote.deleteDataForThisUser();
    }

    public void deleteAccount() {
        repositoryRemote.deleteAccount();
    }

    public void handleLogout() {
        TodayPlannerAdapter.InstanceProvidingMeals = null;

        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Notify view of successful logout
            if (interfaceMain != null) {
                interfaceMain.onLogoutSuccess();
            }
        } else {
            // Notify view of failed logout
            if (interfaceMain != null) {
                interfaceMain.onLogoutFailure();
            }
        }
    }

    public void handleDeleteAccount() {
        if (!networkChecker.checkIfInternetIsConnected()) {
            if (interfaceMain != null) {
                interfaceMain.onNoInternetConnection();
            }
        } else {
            deleteAccountData();
        }
    }

    public void checkNetworkConnection() {
        if (!networkChecker.checkIfInternetIsConnected()) {
            if (interfaceMain != null) {
                interfaceMain.showNoInternetConnection();
            }
        } else {
            if (interfaceMain != null) {
                interfaceMain.showBackInternetConnection();
            }
        }

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                if (interfaceMain != null) {
                    interfaceMain.showBackInternetConnection();
                }
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (interfaceMain != null) {
                    interfaceMain.showNoInternetConnection();
                }
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

  /**  public MainActivityPresenter(Context context) {
        this.context = context;
        repositoryLocal = new RepoistryLocal(context);
        networkChecker = NetworkChecker.getInstance(context);
    }

    public MainActivityPresenter(InterfaceMain interfaceMain) {
        this.interfaceMain = interfaceMain;
    }



    public MainActivityPresenter() {

    }
    public void deleteTableRoom() {

        new Thread(() -> repositoryLocal.deleteTableRoom()).start();
    }


    public void deleteAccountData() {
        repositoryRemote = new RepoistryRemote(interfaceMain);
        repositoryRemote.deleteDataForThisUser();
    }

    public void deleteAccount() {
        repositoryRemote = new RepoistryRemote(interfaceMain);
        repositoryRemote.deleteAccount();
    }
    public void handleLogout() {
        TodayPlannerAdapter.InstanceProvidingMeals = null;

        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Notify view of successful logout
            interfaceMain.onLogoutSuccess();
        } else {
            // Notify view of failed logout
            interfaceMain.onLogoutFailure();
        }
    }

    public void handleDeleteAccount() {
        if (!networkChecker.checkIfInternetIsConnected()) {
            interfaceMain.onNoInternetConnection();
        } else {
            deleteAccountData();
        }
    }

    public void checkNetworkConnection() {
        if (!networkChecker.checkIfInternetIsConnected()) {
            interfaceMain.showNoInternetConnection();
        } else {
            interfaceMain.showBackInternetConnection();
        }

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
                interfaceMain.showBackInternetConnection();
            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                interfaceMain.showNoInternetConnection();
            }
        };

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }
   */
}
