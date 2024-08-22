package com.example.tab2kelyoum.SearchByCategory.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tab2kelyoum.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.SearchByCategory.Model.EachCategoryModel;
import com.example.tab2kelyoum.SearchByCategory.Presenter.AllCategoriesPresenter;
import com.example.tab2kelyoum.SearchByCategory.Presenter.InterfaceAllCategories;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;

public class AllCategories extends Fragment implements InterfaceAllCategories {
    private List<EachCategoryModel> categories;
    public static TextInputEditText textInputEditText;
    private AllCategoriesAdapter allCategoriesAdapter;
    private RecyclerView recyclerView;
    private AllCategoriesPresenter allCategoriesPresenter;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        textInputEditText = view.findViewById(R.id.tinput_search_Categoreies);

        recyclerView = view.findViewById(R.id.rv_Categories);

        allCategoriesPresenter = new AllCategoriesPresenter(this);
        allCategoriesPresenter.getAllCategories();



        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                allCategoriesAdapter = new AllCategoriesAdapter(categories.stream().filter(
                        AreaModel -> AreaModel.getStrCategory().toLowerCase().startsWith(s.toString().toLowerCase())).collect(Collectors.toList()));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(allCategoriesAdapter);
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
        return inflater.inflate(R.layout.fragment_category, container, false);
    }


    @Override
    public void responseOfDataOnSuccess(List<EachCategoryModel> categoriesReceived) {
        categories = categoriesReceived;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        allCategoriesAdapter = new AllCategoriesAdapter(categories);
        recyclerView.setAdapter(allCategoriesAdapter);
    }
}