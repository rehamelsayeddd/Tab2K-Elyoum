package com.example.tab2kelyoum.SearchbyArea.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RootAreasList {
    @SerializedName("meals")
    private List<EachAreaModel> meals;
//getter method for area
    public List<EachAreaModel> getMeals() {
        return meals;
    }
}
