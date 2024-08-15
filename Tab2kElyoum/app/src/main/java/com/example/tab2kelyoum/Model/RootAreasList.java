package com.example.tab2kelyoum.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootAreasList {
    @SerializedName("meals")
    private List<EachAreaModel> meals;

    public List<EachAreaModel> getMeals() {
        return meals;
    }
}
