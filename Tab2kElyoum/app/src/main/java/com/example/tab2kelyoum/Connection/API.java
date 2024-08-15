package com.example.tab2kelyoum.Connection;

import com.example.tab2kelyoum.Model.RootAreasList;
import com.example.tab2kelyoum.Model.RootCategoriesList;
import com.example.tab2kelyoum.Model.RootIngredientsList;
import com.example.tab2kelyoum.Model.RootMeal;
import com.example.tab2kelyoum.Model.RootMealsFromSingleLetter;
import com.example.tab2kelyoum.Model.RootSingleMeal;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
        //Base url for daily inspirations
        String BASE_URL_MealItems = "https://www.themealdb.com/api/json/v1/1/";

        @GET("random.php")
        io.reactivex.rxjava3.core.Observable<RootMeal> getRootRandom();

        @GET("search.php")
        io.reactivex.rxjava3.core.Observable<RootSingleMeal> getRootSingleMeal(@Query("s") String mealName);

        @GET("search.php")
        io.reactivex.rxjava3.core.Observable<RootMealsFromSingleLetter> getRootMealsBySingleLetter(@Query("f") String firstLetterOfMeal);

        @GET("list.php?a=list")
        io.reactivex.rxjava3.core.Observable<RootAreasList> getRootAreasList();

        @GET("list.php?c=list")
        io.reactivex.rxjava3.core.Observable<RootCategoriesList> getRootCategoriesList();

        @GET("list.php?i=list")
        io.reactivex.rxjava3.core.Observable<RootIngredientsList> getRootIngredientsList();

        @GET("filter.php")
        io.reactivex.rxjava3.core.Observable<RootMeal> getMealsOfSelectedArea(@Query("a") String areaSelected);

        @GET("filter.php")
        io.reactivex.rxjava3.core.Observable<RootMeal> getMealsOfSelectedCategory(@Query("c") String categorySelected);

        @GET("filter.php")
        io.reactivex.rxjava3.core.Observable<RootMeal> getMealsOfSelectedIngredient(@Query("i") String ingredientSelected);

        @GET("lookup.php")
        io.reactivex.rxjava3.core.Observable<RootMeal> getMealById(@Query("i") int parseInt);

        @GET("search.php")
        Observable<RootSingleMeal> getAllMeal(@Query("f") String mealName);


}
