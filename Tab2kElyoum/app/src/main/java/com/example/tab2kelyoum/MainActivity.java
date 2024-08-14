package com.example.tab2kelyoum;
import com.example.tab2kelyoum.R;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private ImageView drawerButton;
    public static NavController navController;
    public static Boolean isLoginAsGuest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view); // Ensure you use the correct ID for your NavigationView
        bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
        drawerButton = findViewById(R.id.img_logOut); // Ensure you use the correct ID for your drawer button

        // Set up the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.menu); // Menu icon for drawer
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Set up the NavController
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        // Handle bottom navigation item selections
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.homepageFragment:
                                while (MainActivity.navController.popBackStack() == true) {
                                }
                                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_dailyInspirations);
                                return true;
                            case R.id.nav_search:
                                while (MainActivity.navController.popBackStack() == true) {
                                }
                                Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_search);
                                return true;
                            case R.id.nav_favoriteMeal:
                                if (isLoginAsGuest == true) {
                                    Toast.makeText(MainActivity.this, R.string.access, Toast.LENGTH_SHORT).show();
                                    return false;
                                } else {
                                    while (MainActivity.navController.popBackStack() == true) {
                                    }
                                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_favoriteMeals);
                                    return true;
                                }

                            case R.id.nav_weekPlanners:
                                if (isLoginAsGuest == true) {
                                    Toast.makeText(MainActivity.this, R.string.must_login, Toast.LENGTH_SHORT).show();
                                    return false;
                                } else {
                                    while (MainActivity.navController.popBackStack() == true) {
                                    }
                                    Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.nav_weekPlanner);
                                    return true;
                                }
                        }
                        return false;
                    }
                });

        // Handle navigation drawer item selections
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.drawerLogout:
                        navigationView.setVisibility(View.GONE);
                        logOut();
                        break;

                    default:
                        // Handle other cases or provide default behavior if needed
                        break;
                }
                return false;
            }
        });



        // Handle drawer button click
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }
        });

        // Handle destination changes to show/hide bottom navigation and drawer button
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                switch (destination.getId()) {
                    case R.id.homepageFragment:
                    case R.id.nav_search:
                    case R.id.nav_favoriteMeal:
                    case R.id.nav_weekPlanners:
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        drawerButton.setVisibility(View.VISIBLE);
                        break;
                    default:
                        bottomNavigationView.setVisibility(View.GONE);
                        drawerButton.setVisibility(View.GONE);
                }
            }
        });
    }

    // Method to handle user logout
    private void logOut() {
        // Clear any specific instance variables or application data
        // PlannedTodayAdapter.InstanceProvidingMeals = null;

        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

        // Check if the user is logged out
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Set login state to guest
            // isLoginAsGuest = false;

            // Delete local data (assuming a method for local data removal)
            deleteLocalData();

            // Clear back stack and navigate to sign-in screen
            while (navController.popBackStack()) { /* pop all fragments from the stack */ }

            // Show logout toast message
            Toast.makeText(MainActivity.this, R.string.logout, Toast.LENGTH_SHORT).show();

            // Navigate to the sign-in fragment
            Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment).navigate(R.id.signInFragment);
        } else {
            Toast.makeText(MainActivity.this, R.string.loggingOut, Toast.LENGTH_SHORT).show();
        }
    }

    // Method to delete local data
    private void deleteLocalData() {
        // Add code to delete local data, such as Room database tables, shared preferences, etc.
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}
