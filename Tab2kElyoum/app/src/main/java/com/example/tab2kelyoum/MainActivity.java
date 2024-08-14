package com.example.tab2kelyoum;

import android.os.Bundle;
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

        try {
            // Initialize views
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            bottomNavigationView = findViewById(R.id.activity_main_bottom_navigation_view);
            drawerButton = findViewById(R.id.img_logOut);

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

            // Define resource IDs as variables
            final int homepageFragmentId = R.id.homepageFragment;
            final int navSearchId = R.id.nav_search;
            final int navFavoriteMealId = R.id.nav_favoriteMeal;
            final int navWeekPlannersId = R.id.nav_weekPlanners;
            final int drawerLogoutId = R.id.drawerLogout;

            // Handle bottom navigation item selections
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        int itemId = item.getItemId();
                        if (itemId == homepageFragmentId) {
                            clearBackStack();
                            navController.navigate(R.id.homepageFragment);
                            return true;
                        } else if (itemId == navSearchId) {
                            clearBackStack();
                            navController.navigate(R.id.nav_search);
                            return true;
                        } else if (itemId == navFavoriteMealId) {
                            if (isLoginAsGuest) {
                                Toast.makeText(MainActivity.this, R.string.access, Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                clearBackStack();
                                navController.navigate(R.id.nav_favoriteMeal);
                                return true;
                            }
                        } else if (itemId == navWeekPlannersId) {
                            if (isLoginAsGuest) {
                                Toast.makeText(MainActivity.this, R.string.must_login, Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                clearBackStack();
                                navController.navigate(R.id.nav_weekPlanners);
                                return true;
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error navigating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

            // Handle navigation drawer item selections
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    try {
                        if (item.getItemId() == drawerLogoutId) {
                            navigationView.setVisibility(View.GONE);
                            logOut();
                            return true;
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error handling drawer item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

            // Handle drawer button click
            drawerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (drawerLayout.isDrawerOpen(navigationView)) {
                            drawerLayout.closeDrawer(navigationView);
                        } else {
                            drawerLayout.openDrawer(navigationView);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error handling drawer button: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            // Handle destination changes to show/hide bottom navigation and drawer button
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                    try {
                        int destinationId = destination.getId();
                        if (destinationId == homepageFragmentId || destinationId == navSearchId || destinationId == navFavoriteMealId || destinationId == navWeekPlannersId) {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                            drawerButton.setVisibility(View.VISIBLE);
                        } else {
                            bottomNavigationView.setVisibility(View.GONE);
                            drawerButton.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Error handling destination change: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error initializing MainActivity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void clearBackStack() {
        try {
            while (navController.popBackStack()) {
                // pop all fragments from the stack
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error clearing back stack: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Method to handle user logout
    private void logOut() {
        try {
            FirebaseAuth.getInstance().signOut();

            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                deleteLocalData();
                clearBackStack();
                Toast.makeText(MainActivity.this, R.string.logout, Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.signInFragment);
            } else {
                Toast.makeText(MainActivity.this, R.string.loggingOut, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error logging out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteLocalData() {
        try {
            // Add code to delete local data, such as Room database tables, shared preferences, etc.
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error deleting local data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        try {
            if (item.getItemId() == android.R.id.home) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error handling options item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        try {
            return navController.navigateUp() || super.onSupportNavigateUp();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error navigating up: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
