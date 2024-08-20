package com.example.tab2kelyoum.SearchbyArea;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tab2kelyoum.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.SearchbyArea.Model.EachAreaModel;
import com.example.tab2kelyoum.SearchbyArea.Presenter.AllAreasPresenter;
import com.example.tab2kelyoum.SearchbyArea.Presenter.InterfaceAllAreas;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.stream.Collectors;


public class searchBYCountryFragment extends Fragment implements InterfaceAllAreas {
    private List<EachAreaModel> areas;
    private AreasAdapter areasAdapter;
    private RecyclerView recyclerView;
    public static TextInputEditText textInputEditText;
    private AllAreasPresenter allAreasPresenter;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rv_country);
        textInputEditText = view.findViewById(R.id.tinput_search);

        allAreasPresenter = new AllAreasPresenter(this);
        allAreasPresenter.getAreas();

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                areasAdapter  = new AreasAdapter(areas.stream().filter(EachAreaModel -> EachAreaModel.getStrArea().toLowerCase().startsWith(charSequence.toString().toLowerCase())).collect(Collectors.toList()));
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(areasAdapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_b_y_country, container, false);
    }

    @Override
    public void responseOfDataOnSuccess(List<EachAreaModel> areaReceived) {
        areas = areaReceived;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        areasAdapter = new AreasAdapter(areas);
        recyclerView.setAdapter(areasAdapter);

    }
}