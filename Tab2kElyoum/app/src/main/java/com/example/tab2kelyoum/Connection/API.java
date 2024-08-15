package com.example.tab2kelyoum.Connection;

import android.database.Observable;

import com.example.tab2kelyoum.Model.RootAreasList;
import com.example.tab2kelyoum.Model.RootCategoriesList;
import com.example.tab2kelyoum.Model.RootIngredientsList;
import com.example.tab2kelyoum.Model.RootMeal;
import com.example.tab2kelyoum.Model.RootMealsFromSingleLetter;
import com.example.tab2kelyoum.Model.RootSingleMeal;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
        // Base URL for the Meal DB API
        String BASE_URL_MealItems = "https://www.themealdb.com/api/json/v1/1/";

        //(observable) allows you to subscribe to data streams and react to data changes asynchronously by rx java.
             //to get a random meal by using rxjava
                @GET("random.php")
                Observable<RootMeal> getRootRandom();

                //to search for a meal by its name
                @GET("search.php")
                Observable<RootSingleMeal> getRootSingleMeal(@Query("s") String mealName);

                //to search for meals that start with a specific letter
                @GET("search.php")
                Observable<RootMealsFromSingleLetter> getRootMealsBySingleLetter(@Query("f") String firstLetterOfMeal);

                //to get a list of all meal areas (e.g., American, Italian)
                @GET("list.php?a=list")
                Observable<RootAreasList> getRootAreasList();

                //to get a list of all meal categories (e.g., Dessert, Seafood)
                @GET("list.php?c=list")
                Observable<RootCategoriesList> getRootCategoriesList();

                //to get a list of all ingredients
                @GET("list.php?i=list")
                Observable<RootIngredientsList> getRootIngredientsList();

                //to filter meals by selected area
                @GET("filter.php")
                Observable<RootMeal> getMealsOfSelectedArea(@Query("a") String areaSelected);

                //to filter meals by selected category
                @GET("filter.php")
                Observable<RootMeal> getMealsOfSelectedCategory(@Query("c") String categorySelected);

                //to filter meals by selected ingredient
                @GET("filter.php")
                Observable<RootMeal> getMealsOfSelectedIngredient(@Query("i") String ingredientSelected);

                //to get meal details by its ID
                @GET("lookup.php")
                Observable<RootMeal> getMealById(@Query("i") int parseInt);

                //to get all meals that start with a specific letter (similar to getRootMealsBySingleLetter)
                @GET("search.php")
                Observable<RootSingleMeal> getAllMeal(@Query("f") String mealName);

        }
