package com.example.tab2kelyoum.SearchbyMeal.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.SearchbyMeal.Presenter.AllMealsPresenter;
import com.example.tab2kelyoum.SearchbyMeal.Presenter.InterfaceAllMeals;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;


public class AllMeals extends Fragment implements InterfaceAllMeals {

    private AllMealsAdapter allMealsAdapter;
    private RecyclerView recyclerView;
    private TextInputEditText searchTextInput;
    private List<MealsItem> mealsItemList = new ArrayList<>();
    private List<MealsItem> filteredMealsItemList = new ArrayList<>();
    private static final String TAG = "SearchByAllMealsFragment";
    private AllMealsPresenter allMealsPresenter;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();
    private Toast toastMessage;
    private Boolean isInternetDisconnectedWhileTyping = false;
    private Boolean shouldFilterAfterInternetWasOff = false;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.rv_allMeals);
        searchTextInput = view.findViewById(R.id.tinput_search_AllMeals);

        toastMessage = new Toast(requireContext());

        allMealsPresenter = new AllMealsPresenter(this);


        searchTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: Internet is connected");
                //check the internet connection as it is required
                if (networkChecker.checkIfInternetIsConnected()) {
                    handleInternetConnected(s);
                } else {
                    // Optionally, you can add a message or UI indication that the internet is required.
                    Toast.makeText(MainActivity.mainActivity, "Internet connection is required to search.", Toast.LENGTH_SHORT).show();
                }
            }

            private void handleInternetConnected(CharSequence s) {
                if (s.length() == 0) {
                    clearMeals();
                } else if (s.length() == 1) {
                    allMealsPresenter.getAllMeals(s.charAt(0)); //fetching all meals using interface that passes to repo remote
                } else {
                    handleLongSearchText(s);
                }
            }

            private void clearMeals() {
                mealsItemList.clear(); //clears the lish when the serach input is empty
                updateRecyclerView(mealsItemList);
            }

            private void handleLongSearchText(CharSequence s) {
                if (!mealsItemList.isEmpty()) { //handle charcter of serach input string
                    filterAndUpdateRecyclerView(s.toString());
                }
            }

            private void filterAndUpdateRecyclerView(String searchText) {
                filteredMealsItemList.clear();
                 //using filter method to filter based on input char
                Observable.fromIterable(mealsItemList)
                        .filter(mealItem -> mealItem.getStrMeal().toLowerCase().contains(searchText.toLowerCase()))
                        .subscribe(
                                filteredMealsItemList::add,
                                Throwable::printStackTrace,
                                () -> updateRecyclerView(filteredMealsItemList)
                        );
            }
              //then update recycler view
            private void updateRecyclerView(List<MealsItem> mealsList) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                allMealsAdapter = new AllMealsAdapter(mealsList);
                recyclerView.setAdapter(allMealsAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_by_all_meals, container, false);


    }

    @Override
    public void responseOfDataOnSuccess(List<MealsItem> mealsReceived) {
        if (networkChecker.checkIfInternetIsConnected()) {
            // Update the main meal list and set up the RecyclerView
            mealsItemList = mealsReceived;
            setupRecyclerView(mealsItemList);

            // Apply filtering if the flag indicates that filtering is needed
            if (shouldFilterAfterInternetWasOff) {
                shouldFilterAfterInternetWasOff = false; // Reset the flag
                if (!mealsItemList.isEmpty()) {
                    applyFilterAndRefreshRecyclerView();
                }
            }
        } else {
            // Inform the user that an internet connection is required
            Toast.makeText(MainActivity.mainActivity, "Internet connection is required to search.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView(List<MealsItem> mealsList) {
        // Set up the RecyclerView with a vertical LinearLayoutManager and the provided meal list
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        allMealsAdapter = new AllMealsAdapter(mealsList);
        recyclerView.setAdapter(allMealsAdapter);
    }

    private void applyFilterAndRefreshRecyclerView() {
        filteredMealsItemList.clear(); // Clear any previous filtered data

        // Filter the meals based on the search input and update the RecyclerView
        Observable.fromIterable(mealsItemList)
                .filter(mealsItem -> mealsItem.getStrMeal().toLowerCase().contains(searchTextInput.getText().toString().toLowerCase()))
                .subscribe(
                        filteredMealsItemList::add, // Add each filtered item to the filtered list
                        Throwable::printStackTrace, // Handle any errors during filtering
                        () -> setupRecyclerView(filteredMealsItemList) // Refresh the RecyclerView with filtered data
                );
    }


}