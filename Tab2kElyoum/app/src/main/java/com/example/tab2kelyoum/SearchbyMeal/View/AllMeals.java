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
                    Log.i(TAG, "onTextChanged: " + isInternetDisconnectedWhileTyping);

                    boolean isConnected = networkChecker.checkIfInternetIsConnected();

                    // Handling when there is no internet connection
                    if (!isConnected) {
                        handleNoInternetConnection();
                        return;
                    }

                    // Handling when internet is reconnected after disconnection
                    if (isInternetDisconnectedWhileTyping) {
                        handleInternetReconnection(s);
                    } else {
                        handleTypingWithInternet(s);
                    }
                }

                private void handleNoInternetConnection() {
                    MainActivity.mainActivity.runOnUiThread(() -> {
                        isInternetDisconnectedWhileTyping = true;
                        toastMessage.cancel();
                        toastMessage = Toast.makeText(MainActivity.mainActivity, "Please,Turn internet on to search.", Toast.LENGTH_SHORT);
                        toastMessage.show();
                    });
                }

                private void handleInternetReconnection(CharSequence s) {
                    isInternetDisconnectedWhileTyping = false;

                    if (s.length() == 1) {
                        allMealsPresenter.getAllMeals(s.charAt(0));
                    } else if (s.length() > 1) {
                        shouldFilterAfterInternetWasOff = true;
                        allMealsPresenter.getAllMeals(s.charAt(0));
                        filterMealsAfterInternetReconnection(s);
                    }
                }

                private void handleTypingWithInternet(CharSequence s) {
                    isInternetDisconnectedWhileTyping = false;

                    if (s.length() == 0) {
                        clearMealList();
                    } else if (s.length() == 1) {
                        allMealsPresenter.getAllMeals(s.charAt(0));
                    } else if (s.length() > 1 && !mealsItemList.isEmpty()) {
                        filterMeals(s);
                    }
                }

                private void filterMealsAfterInternetReconnection(CharSequence s) {
                    if (!mealsItemList.isEmpty()) {
                        filteredMealsItemList.clear();
                        Observable.fromIterable(mealsItemList)
                                .filter(mealsItem -> mealsItem.getStrMeal().toLowerCase().contains(s.toString().toLowerCase()))
                                .subscribe(
                                        filteredMealsItemList::add,
                                        throwable -> Log.e(TAG, "Error filtering meals", throwable),
                                        this::updateRecyclerViewWithFilteredMeals
                                );
                    }
                }

                private void filterMeals(CharSequence s) { //filter based on the cuurent input
                    filteredMealsItemList.clear();
                    Observable.fromIterable(mealsItemList)
                            .filter(mealsItem -> mealsItem.getStrMeal().toLowerCase().contains(s.toString().toLowerCase()))
                            .subscribe(
                                    filteredMealsItemList::add,
                                    throwable -> Log.e(TAG, "Error filtering meals", throwable),
                                    this::updateRecyclerViewWithFilteredMeals
                            );
                }

                private void updateRecyclerViewWithFilteredMeals() {
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                    allMealsAdapter = new AllMealsAdapter(filteredMealsItemList);
                    recyclerView.setAdapter(allMealsAdapter);
                }

                private void clearMealList() {
                    mealsItemList.clear();
                    updateRecyclerViewWithFilteredMeals();
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
        // Update the main meal list with the received data
        mealsItemList = mealsReceived;

        // Setup the RecyclerView with the received data
        setupRecyclerView(mealsItemList);

        // Check if filtering is needed after the internet was off
        if (shouldFilterAfterInternetWasOff) {
            shouldFilterAfterInternetWasOff = false;
            filterMealsBasedOnSearchInput();
        }
    }

    private void setupRecyclerView(List<MealsItem> mealsList) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        allMealsAdapter = new AllMealsAdapter(mealsList);
        recyclerView.setAdapter(allMealsAdapter);
    }

    private void filterMealsBasedOnSearchInput() {
        if (!mealsItemList.isEmpty()) {
            filteredMealsItemList.clear();
            Observable.fromIterable(mealsItemList)
                    .filter(mealsItem -> mealsItem.getStrMeal().toLowerCase()
                            .contains(searchTextInput.getText().toString().toLowerCase()))
                    .subscribe(
                            filteredMealsItemList::add,
                            throwable -> Log.e(TAG, "Error filtering meals", throwable),
                            this::updateRecyclerViewWithFilteredMeals
                    );
        }
    }

    private void updateRecyclerViewWithFilteredMeals() {
        setupRecyclerView(filteredMealsItemList);
    }


}