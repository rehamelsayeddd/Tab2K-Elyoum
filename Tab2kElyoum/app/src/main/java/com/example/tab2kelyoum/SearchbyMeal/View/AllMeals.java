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
import com.example.tab2kelyoum.NetworkChecker;
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

                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isInternetDisconnectedWhileTyping = true;
                            toastMessage.cancel();
                            toastMessage = Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to search.", Toast.LENGTH_SHORT);
                            toastMessage.show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    if (isInternetDisconnectedWhileTyping == true & s.length() == 1) {
                        isInternetDisconnectedWhileTyping = false;
                        allMealsPresenter.getAllMeals(s.charAt(0));
                    } else if (isInternetDisconnectedWhileTyping == true & s.length() > 1) {
                        shouldFilterAfterInternetWasOff = true;
                        isInternetDisconnectedWhileTyping = false;
                        allMealsPresenter.getAllMeals(s.charAt(0));
                        if (mealsItemList.size() != 0) {
                            filteredMealsItemList.clear();
                            Observable<MealsItem> observable = Observable.fromIterable(mealsItemList);
                            observable
                                    .filter
                                            (mealsItem ->
                                                    mealsItem.getStrMeal().toLowerCase().contains(searchTextInput.getText().toString().toLowerCase()))
                                    .subscribe(
                                            mealsItem -> {
                                                filteredMealsItemList.add(mealsItem);

                                            }
                                            , (error) -> {
                                            }
                                            , () -> {
                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                                                recyclerView.setLayoutManager(linearLayoutManager);
                                                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                                allMealsAdapter = new AllMealsAdapter(filteredMealsItemList);
                                                recyclerView.setAdapter(allMealsAdapter);
                                            }
                                    );
                        }
                    } else {
                        isInternetDisconnectedWhileTyping = false;
                        if (s.length() == 0) {
                            mealsItemList.clear();
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                            recyclerView.setLayoutManager(linearLayoutManager);

                            linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                            allMealsAdapter = new AllMealsAdapter(mealsItemList);
                            recyclerView.setAdapter(allMealsAdapter);
                        }
                        if (s.length() == 1) {

                            allMealsPresenter.getAllMeals(s.charAt(0));


                        } else if (s.length() > 1) {

                            if (mealsItemList.size() != 0) {
                                filteredMealsItemList.clear();
                                Observable<MealsItem> observable = Observable.fromIterable(mealsItemList);
                                observable
                                        .filter
                                                (mealsItem ->
                                                        mealsItem.getStrMeal().toLowerCase().contains(searchTextInput.getText().toString().toLowerCase()))
                                        .subscribe(
                                                mealsItem -> {
                                                    filteredMealsItemList.add(mealsItem);

                                                }
                                                , (error) -> {
                                                }
                                                , () -> {
                                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                                                    recyclerView.setLayoutManager(linearLayoutManager);
                                                    linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                                                    allMealsAdapter = new AllMealsAdapter(filteredMealsItemList);
                                                    recyclerView.setAdapter(allMealsAdapter);
                                                }
                                        );
                            }


                        }
                    }


                }


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
        mealsItemList = mealsReceived;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        allMealsAdapter = new AllMealsAdapter(mealsItemList);
        recyclerView.setAdapter(allMealsAdapter);

        if (shouldFilterAfterInternetWasOff) {
            shouldFilterAfterInternetWasOff = false;
            if (mealsItemList.size() != 0) {
                filteredMealsItemList.clear();
                Observable<MealsItem> observable = Observable.fromIterable(mealsItemList);
                observable
                        .filter
                                (mealsItem ->
                                        mealsItem.getStrMeal().toLowerCase().contains(searchTextInput.getText().toString().toLowerCase()))
                        .subscribe(
                                mealsItem -> {
                                    filteredMealsItemList.add(mealsItem);

                                }
                                , (error) -> {
                                }
                                , () -> {
                                    LinearLayoutManager linearLayoutManage = new LinearLayoutManager(requireContext());
                                    recyclerView.setLayoutManager(linearLayoutManage);
                                    linearLayoutManage.setOrientation(RecyclerView.VERTICAL);
                                    allMealsAdapter = new AllMealsAdapter(filteredMealsItemList);
                                    recyclerView.setAdapter(allMealsAdapter);
                                }
                        );
            }
        }
    }

}