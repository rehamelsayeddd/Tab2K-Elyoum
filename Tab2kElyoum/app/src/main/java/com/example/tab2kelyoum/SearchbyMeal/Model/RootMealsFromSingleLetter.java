package com.example.tab2kelyoum.SearchbyMeal.Model;

import com.example.tab2kelyoum.Model.MealsItem;

import java.util.List;
// represents a JSON response from an API where the response contains a list of meals.
public class RootMealsFromSingleLetter {

    private List<MealsItem> meals;
//getter method for meal when the first letter enters
    public List<MealsItem> getMeals() {
        return meals;
    }
}
