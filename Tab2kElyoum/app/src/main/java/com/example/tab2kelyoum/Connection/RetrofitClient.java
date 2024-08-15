package com.example.tab2kelyoum.Connection;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.tab2kelyoum.Model.RootMeal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RetrofitClient {
//using rxjava
    private static RetrofitClient instance = null;
    private API myApi;
    private static final String TAG = "API_Client";


    public RetrofitClient() {
        Gson gson = new GsonBuilder().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL_MealItems)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        myApi = retrofit.create(API.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public API getMyApi() {
        return myApi;
    }

}