package com.example.tab2kelyoum.Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.example.tab2kelyoum.Model.MealsItem;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;


@Dao
public interface MealDao {

    // DAO (Data Access Object) interface for interacting with the MealsItem table in the Room database
    //by using rxjava to handle data and make it synchronizied when data changes
    // Query to retrieve all stored MealsItem records as a Flowable list
        @Query("SELECT * FROM MealsItem")
        Flowable<List<MealsItem>> getStoredMealsItems();

        // Query to find a specific MealsItem by name and weekday
        @Query("SELECT * FROM MealsItem WHERE strMeal LIKE :mealsItemName AND weekDay LIKE :weekDayString")
        MealsItem findMealByName(String mealsItemName, String weekDayString);

        // Insert a MealsItem into the database, ignoring if there is a conflict
        @Insert(onConflict = OnConflictStrategy.IGNORE)
        void insertMeal(MealsItem mealsItem);

        // Delete a specific MealsItem from the database
        @Delete
        void deleteMeal(MealsItem mealsItem);

        // Query to delete all records from the MealsItem table
        @Query("DELETE FROM MealsItem")
        void deleteTableRoom();
    }


