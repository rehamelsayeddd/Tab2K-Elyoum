package com.example.tab2kelyoum;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tab2kelyoum.Model.MealsItem;

import java.util.ArrayList;
import java.util.List;


public class homepageFragment extends Fragment implements homepageInterface {

private ViewPager2 viewPager2;
private Handler slideHandler;
private homepageInterface homepageInterface;
private RecyclerView rvPlan;
private List<MealsItem> savedMeals = new ArrayList<>();
private List<MealsItem> mealsWeekPlan = new ArrayList<>();
private ProgressBar progressBar;
private Boolean isPlannedToday =false; //tracks the adapter has been created

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void responseOfDataOnSuccess(List<MealsItem> mealsList) {

    }

    @Override
    public void responseOfDataOnFailure(Throwable error) {

    }

    @Override
    public void responseOfLoadingDataFromFirestoreToRoom() {

    }
}