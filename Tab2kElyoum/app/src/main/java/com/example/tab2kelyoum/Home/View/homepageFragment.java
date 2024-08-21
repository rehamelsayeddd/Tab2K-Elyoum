package com.example.tab2kelyoum.Home.View;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tab2kelyoum.Home.Presenter.homePresenter;
import com.example.tab2kelyoum.Home.Presenter.homepageInterface;
import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.R;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class homepageFragment extends Fragment implements homepageInterface {

    private static final String TAG = "DailyInspirations";


    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();
    private homePresenter homePresenter;
    private RecyclerView recyclerViewPlanToday;
    private TodayPlannerAdapter todayPlannerAdapter;
    private List<MealsItem> allSavedMeals = new ArrayList<>();
    private List<MealsItem> mealsWeekPlannedToday = new ArrayList<>();
    private ProgressBar progressBar;
    private Boolean isPlannedTodayAdapterInstanceCreated = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();


        return inflater.inflate(R.layout.fragment_homepage, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = view.findViewById(R.id.viewPagerImageSlider);
        progressBar = view.findViewById(R.id.loading);
        recyclerViewPlanToday = view.findViewById(R.id.recyclerViewPlannedTodayDailyInspirations);


        homePresenter = new homePresenter(this, requireContext());

        homePresenter.getDailyInspirations();

        if (!MainActivity.isLoginAsGuest) {
            homePresenter.loadRoomFromFirestore();
        }

        if (!MainActivity.isLoginAsGuest) {
            homePresenter.loadHeaderTitle();
        }


    }



    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }

    };


    @Override
    public void responseOfDataOnSuccess(List<MealsItem> mealsList) {
        viewPager2.setAdapter(new SliderAdapter(mealsList, viewPager2));

        hideProgress();


        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(50);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_IF_CONTENT_SCROLLS);
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);

            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);


        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 5000);
            }
        });
    }

    @Override
    public void responseOfDataOnFailure(Throwable error) {
        error.printStackTrace();
    }

    @Override
    public void responseOfLoadingDataFromFirestoreToRoom() {
        allSavedMeals = homePresenter.returnStoredMealsItems().blockingFirst();

        getMealsPlannedForToday();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getMealsPlannedForToday() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerViewPlanToday.setHasFixedSize(true);
        recyclerViewPlanToday.setLayoutManager(linearLayoutManager);
        List<MealsItem> returnStoredMealsItemsWithWeekDayNotNull = new ArrayList<>();

        for (MealsItem mealsItem : allSavedMeals) {
            if (!mealsItem.getWeekDay().equals("NULL")) {
                returnStoredMealsItemsWithWeekDayNotNull.add(mealsItem);
            }
        }


        for (MealsItem mealsItem : returnStoredMealsItemsWithWeekDayNotNull) {
            if (mealsItem.getWeekDay().toLowerCase().equals(LocalDate.now().getDayOfWeek().name().toLowerCase())) {
                mealsWeekPlannedToday.add(mealsItem);
            }
        }


        todayPlannerAdapter = TodayPlannerAdapter.getInstanceMeals(mealsWeekPlannedToday);
        recyclerViewPlanToday.setAdapter(todayPlannerAdapter);
        isPlannedTodayAdapterInstanceCreated = true;
        TodayPlannerAdapter.getInstance().notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onResume() {
        super.onResume();

        sliderHandler.postDelayed(sliderRunnable, 5000);


        Log.i(TAG, "onResume: ");
    }

    private void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

}