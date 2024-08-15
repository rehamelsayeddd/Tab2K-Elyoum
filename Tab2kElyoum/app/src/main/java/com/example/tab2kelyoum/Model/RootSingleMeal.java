package com.example.tab2kelyoum.Model;

import java.util.List;
// represents a JSON response from an API where the response contains a list of meals.

public class RootSingleMeal {
    private List<MealsItem> meals;
//getter method for all meals
    public List<MealsItem> getMeals() {
        return meals;
    }
}