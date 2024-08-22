package com.example.tab2kelyoum.SearchMain.View;

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
import com.example.tab2kelyoum.NetworkChecker;
import com.example.tab2kelyoum.R;


public class SearchMainFragment extends Fragment {

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
                    Navigation.findNavController(view).navigate(SearchMainFragmentDirections.actionNavSearchToSearchBYCountryFragment());
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
                    Navigation.findNavController(view).navigate(SearchMainFragmentDirections.actionNavSearchToCategoryFragment());
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
                    Navigation.findNavController(view).navigate(SearchMainFragmentDirections.actionNavSearchToSearchByIngrdiantFragment());
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
                    Navigation.findNavController(view).navigate(SearchMainFragmentDirections.actionNavSearchToSearchByAllMealsFragment());
                }


            }
        });

    }
}