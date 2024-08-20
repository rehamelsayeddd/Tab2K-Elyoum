package com.example.tab2kelyoum;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class mealByCountryFragment extends Fragment {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meal_by_country, container, false);

        // Retrieve the arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            String country = arguments.getString("country");
            // Use the 'country' string as needed, e.g., update UI or make a network request
            // Example: update a TextView with the country name
            TextView countryTextView = view.findViewById(R.id.countryTextView);
            countryTextView.setText(country);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}