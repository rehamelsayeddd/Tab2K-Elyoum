package com.example.tab2kelyoum.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tab2kelyoum.Model.MealsItem;

// Define the Room Database
@Database(entities = {MealsItem.class}, exportSchema = false, version = 1)
public abstract class DB extends RoomDatabase {

    // Singleton instance of the database
    private static DB instance = null;

    // Abstract method to get the DAO (Data Access Object) for accessing the database
    public abstract MealDao mealDAO();

    // Static method to get the singleton instance of the database
    public static synchronized DB getInstance(Context context) {
        // Check if the instance is null
        if (instance == null) {
            // Create the database instance using Room's databaseBuilder
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            DB.class,  // The database class
                            "Meal")   // The database name
                    .build();
        }
        // Return the singleton instance
        return instance;
    }
}
