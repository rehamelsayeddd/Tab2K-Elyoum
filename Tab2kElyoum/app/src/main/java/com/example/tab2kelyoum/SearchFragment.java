package com.example.tab2kelyoum;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tab2kelyoum.MainActivity.View.MainActivity;


public class SearchFragment extends Fragment {


    private CardView searchByCountry, searchByCategory, searchIngrediant, searchByAllMeals;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();
    private Toast toast;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        return inflater.inflate(R.layout.fragment_nav_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        searchByCountry = view.findViewById(R.id.card_country);
        searchByCategory = view.findViewById(R.id.CardView_category);
        searchIngrediant = view.findViewById(R.id.cardView_ingrediant);
        searchByAllMeals = view.findViewById(R.id.card_AllMeals);

        toast = new Toast(requireContext());

        searchByCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                            toast = Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to access search feature.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    // Create a Bundle if needed to pass any arguments, or leave it null if not
                    Bundle bundle = new Bundle(); // Only if you need to pass data

                    Navigation.findNavController(view)
                            .navigate(R.id.action_nav_search_to_searchBYCountryFragment, bundle);
                }


            }
        });

        searchByCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                            toast = Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to access search feature.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    Navigation.findNavController(view)
                            .navigate(R.id.action_nav_search_to_categoryFragment);
                }



            }
        });
        searchIngrediant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                            toast = Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to access search feature.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    Navigation.findNavController(view)
                            .navigate(R.id.action_nav_search_to_searchByIngrdiantFragment);
                }


            }
        });

        searchByAllMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                            toast = Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to access search feature.", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    Navigation.findNavController(view)
                            .navigate(R.id.action_nav_search_to_searchByAllMealsFragment);
                }


            }
        });

    }
}
