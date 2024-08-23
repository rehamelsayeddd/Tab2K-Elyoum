package com.example.tab2kelyoum.MainActivity.View;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.Home.View.TodayPlannerAdapter;
import com.example.tab2kelyoum.MainActivity.Presenter.InterfaceMain;
import com.example.tab2kelyoum.MainActivity.Presenter.MainActivityPresenter;
import com.example.tab2kelyoum.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

        mainActivityPresenter = new MainActivityPresenter(this, this);
      //  mainActivityPresenter = new MainActivityPresenter((InterfaceMain) this);

        mainActivity = this;

        drawerButton = findViewById(R.id.img_logOut);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.homepageFragment) {
                while (MainActivity.navController.popBackStack()) {}
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.homepageFragment);
                return true;
            } else if (item.getItemId() == R.id.nav_search) {
                while (MainActivity.navController.popBackStack()) {}
                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_search);
                return true;
            } else if (item.getItemId() == R.id.nav_favoriteMeal) {
                if (isLoginAsGuest) {
                    Toast.makeText(MainActivity.this, R.string.access, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    while (MainActivity.navController.popBackStack()) {}
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_favoriteMeal);
                    return true;
                }
            } else if (item.getItemId() == R.id.nav_weekPlanners) {
                if (isLoginAsGuest) {
                    Toast.makeText(MainActivity.this, R.string.must_login, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    while (MainActivity.navController.popBackStack()) {}
                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_weekPlanners);
                    return true;
                }
            }
            return false;
        });

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
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
        });


        drawerButton.setOnClickListener(view -> {
            if (navigationView.getVisibility() == View.GONE) {
                navigationView.setVisibility(View.VISIBLE);
            } else {
                navigationView.setVisibility(View.GONE);
            }
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.drawerLogout) {
                navigationView.setVisibility(View.GONE);
                drawerLogOut();
                return true;
            } else if (itemId == R.id.drawerDeleteAcount) {
                if (isLoginAsGuest) {
                    Toast.makeText(MainActivity.this, R.string.needAcount, Toast.LENGTH_SHORT).show();
                } else {
                    if (!networkChecker.checkIfInternetIsConnected()) {
                        Toast.makeText(MainActivity.this, R.string.needAcount, Toast.LENGTH_SHORT).show();
                    } else {
                        navigationView.setVisibility(View.GONE);
                        drawerDeleteAccount();
                    }
                }
                return true;
            }
            return false;
        });

        View headerView = navigationView.getHeaderView(0);
        tv_headerDrawer = headerView.findViewById(R.id.tv_headerDrawer);
    }





 /**   public void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    } **/

  /**  private void drawerLogOut() {
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
   **/
  private void navigateToFragment(int fragmentId) {
      while (navController.popBackStack()) {}
      navController.navigate(fragmentId);
  }
  private void drawerLogOut() {
      // Delegate logout logic to the presenter
      mainActivityPresenter.handleLogout();
  }


   /** private void drawerDeleteAccount() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.WaitAreYouSure);
        builder.setTitle(R.string.DeleteAccount);
        builder.setCancelable(true);


        builder.setPositiveButton(R.string.sure, (DialogInterface.OnClickListener) (dialog, which) -> {
            if (!networkChecker.checkIfInternetIsConnected()) {
                MainActivity.mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.mainActivity, R.string.turnOnInternent, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                mainActivityPresenter = new MainActivityPresenter((InterfaceMain) this);
                mainActivityPresenter.deleteAccountData();
            }
        });

        builder.setNegativeButton(R.string.keep, (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
     **/
   private void drawerDeleteAccount(){
       AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setMessage(R.string.WaitAreYouSure);
       builder.setTitle(R.string.DeleteAccount);
       builder.setCancelable(true);

       builder.setPositiveButton(R.string.sure, (DialogInterface.OnClickListener) (dialog, which) -> {
           // Notify presenter to handle account deletion
           mainActivityPresenter.handleDeleteAccount();
       });

       builder.setNegativeButton(R.string.keep, (DialogInterface.OnClickListener) (dialog, which) -> {
           dialog.cancel();
       });

       AlertDialog alertDialog = builder.create();
       alertDialog.show();
   }
/**
    private void checkNetwork() {
        if (!networkChecker.checkIfInternetIsConnected()) {
            tv_internetConnection.setVisibility(View.VISIBLE);
            tv_internetConnection.setText(R.string.noInternet);
           // tv_internetConnection.setBackgroundColor(getResources().getColor(R.color.red));
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

                         //   tv_internetConnection.setBackgroundColor(getResources().getColor(R.color.green));
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
                       // tv_internetConnection.setBackgroundColor(getResources().getColor(R.color.red));
                    }
                });

            }


        };

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(ConnectivityManager.class);
        connectivityManager.requestNetwork(networkRequest, networkCallback);
    } **/

    private void checkNetwork(){
        mainActivityPresenter.checkNetworkConnection();

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }


    @Override
    public void onFinishedDeletingItemsOfThisAccount() {
        mainActivityPresenter.deleteAccount();


    }

    @Override
    public void onFinishedDeletingAccount() {
        TodayPlannerAdapter.InstanceProvidingMeals = null;
        mainActivityPresenter.deleteTableRoom();

        while (navController.popBackStack()) {
        }

        Toast.makeText(mainActivity, R.string.deleteAcount, Toast.LENGTH_SHORT).show();

        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
    }

    @Override
    public void onLogoutSuccess() {
        isLoginAsGuest = false;

        // Delete Room table
        mainActivityPresenter.deleteTableRoom();

        // Clear backstack
        while (navController.popBackStack()) {}

        // Show logout toast
        Toast.makeText(MainActivity.this, R.string.logout, Toast.LENGTH_SHORT).show();

        // Navigate to sign-in fragment
        Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
    }

    @Override
    public void onLogoutFailure() {
        Toast.makeText(MainActivity.this, R.string.loggingOut, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNoInternetConnection() {
        runOnUiThread(() ->
                Toast.makeText(MainActivity.this, R.string.turnOnInternent, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public void onAccountDeleted() {
        // Clear any user data and navigate back to sign-in screen
        TodayPlannerAdapter.InstanceProvidingMeals = null;
        Toast.makeText(this, R.string.account_deleted_successfully, Toast.LENGTH_SHORT).show();

        // Navigate back to sign-in fragment
        Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
    }
    @Override
    public void onAccountDeletionFailed() {
        Toast.makeText(this, R.string.account_deletion_failed, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showNoInternetConnection() {
        tv_internetConnection.setVisibility(View.VISIBLE);
        tv_internetConnection.setText(R.string.noInternet);
    }

    @Override
    public void showBackInternetConnection() {
        if (!timerIsExists) {
            timerIsExists = true;
            // tv_internetConnection.setBackgroundColor(getResources().getColor(R.color.green));
            tv_internetConnection.setText(R.string.backInternet);
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(() -> tv_internetConnection.setVisibility(View.GONE));
                }
            }, 5000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNetwork();
    }
}