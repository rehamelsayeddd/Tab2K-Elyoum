package com.example.tab2kelyoum.MainActivity.View;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.tab2kelyoum.Home.View.TodayPlannerAdapter;
import com.example.tab2kelyoum.MainActivity.Presenter.InterfaceMain;
import com.example.tab2kelyoum.MainActivity.Presenter.MainActivityPresenter;
import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements InterfaceMain {

    public static NavController navController;
    public static BottomNavigationView bottomNavigationView;
    private ImageView drawerButton;
    public static Boolean isLoginAsGuest = false;
    private NetworkChecker networkChecker;
    public static MainActivity mainActivity;
    private TextView tv_internetConnection;
    private static final String TAG = "MainActivity";
    private Timer timer;
    private Boolean timerIsExists = false;
    private MainActivityPresenter mainActivityPresenter;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public static TextView tv_headerDrawer;
    private Toast toastLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        networkChecker = NetworkChecker.getInstance(this);


        tv_internetConnection = findViewById(R.id.tv_internetConnection);

        mainActivity = this;

        drawerButton = findViewById(R.id.img_logOut);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        if (item.getItemId() == R.id.homepageFragment) {
                            while (MainActivity.navController.popBackStack()) {
                            }
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.homepageFragment);
                            return true;
                        } else if (item.getItemId() == R.id.nav_search) {
                            while (MainActivity.navController.popBackStack()) {
                            }
                            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_search);
                            return true;
                        } else if (item.getItemId() == R.id.nav_favoriteMeal) {
                            if (isLoginAsGuest) {
                                Toast.makeText(MainActivity.this, R.string.access, Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                while (MainActivity.navController.popBackStack()) {
                                }
                                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_favoriteMeal);
                                return true;
                            }
                        } else if (item.getItemId() == R.id.nav_weekPlanners) {
                            if (isLoginAsGuest) {
                                Toast.makeText(MainActivity.this, R.string.must_login, Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                while (MainActivity.navController.popBackStack()) {
                                }
                                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_weekPlanners);
                                return true;
                            }
                        }
                        return false;
                    }
                });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int destinationId = destination.getId();

                if (destinationId == R.id.homepageFragment ||
                        destinationId == R.id.nav_search ||
                        destinationId == R.id.nav_favoriteMeal ||
                        destinationId == R.id.nav_weekPlanners) {

                    bottomNavigationView.setVisibility(View.VISIBLE);
                    drawerButton.setVisibility(View.VISIBLE);
                } else {
                    bottomNavigationView.setVisibility(View.GONE);
                    drawerButton.setVisibility(View.GONE);
                }
            }
        });


        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (navigationView.getVisibility() == View.GONE) {
                    navigationView.setVisibility(View.VISIBLE);
                } else {
                    navigationView.setVisibility(View.GONE);
                }


            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.drawerLogout) {
                    navigationView.setVisibility(View.GONE);
                    drawerLogOut();
                }

                return false;
            }
        });



        View headerView = navigationView.getHeaderView(0);
        tv_headerDrawer = (TextView) headerView.findViewById(R.id.tv_headerDrawer);


    }

    private void drawerChangeAppLanguage() {
        if (Locale.getDefault().getDisplayLanguage().toString().equals("English")) {
            setLocale("ar");
        } else {
            setLocale("en");
        }

        recreate();

    }

    public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void drawerLogOut() {
        TodayPlannerAdapter.InstanceProvidingMeals = null;
        FirebaseAuth.getInstance().signOut();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            isLoginAsGuest = false;

            mainActivityPresenter = new MainActivityPresenter((Context) this);
            mainActivityPresenter.deleteTableRoom();

            while (navController.popBackStack() == true) {
            }

            toastLogOut = new Toast(this);
            toastLogOut.makeText(MainActivity.this, R.string.logout, Toast.LENGTH_SHORT).show();


            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
        } else {
            Toast.makeText(MainActivity.this, R.string.loggingOut, Toast.LENGTH_SHORT).show();
        }
    }


    private void checkNetwork() {
        if (!networkChecker.checkIfInternetIsConnected()) {
            tv_internetConnection.setVisibility(View.VISIBLE);
            tv_internetConnection.setText(R.string.noInternet);

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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!timerIsExists) {
                            timerIsExists = true;


                            tv_internetConnection.setText(R.string.backInternet);
                            timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            tv_internetConnection.setVisibility(View.GONE);
                                        }
                                    });

                                }
                            }, 5000);
                        }


                    }
                });

            }

            @Override
            public void onLost(@NonNull Network network) {
                super.onLost(network);
                if (timerIsExists) {
                    timerIsExists = false;
                    timer.cancel();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_internetConnection.setVisibility(View.VISIBLE);
                        tv_internetConnection.setText(R.string.noInternet);

                    }
                });

            }


        };

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


    @Override
    public void onFinishedDeletingItemsOfThisAccount() {
        mainActivityPresenter = new MainActivityPresenter((InterfaceMain) this);
        mainActivityPresenter.deleteAccount();

    }

    @Override
    public void onFinishedDeletingAccount() {
        TodayPlannerAdapter.InstanceProvidingMeals = null;
        mainActivityPresenter = new MainActivityPresenter((Context) this);
        mainActivityPresenter.deleteTableRoom();


        while (navController.popBackStack() == true) {
        }

        Toast.makeText(mainActivity, R.string.deleteAcount, Toast.LENGTH_SHORT).show();

        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();
    }
}