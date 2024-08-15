package com.example.tab2kelyoum.Model;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class RootMeal {


        //The JSON response from the API is expected to have a "meals" field,which contains a list of meal items.
        @SerializedName("meals")
        private List<MealsItem> meals;

        // Getter method to access the list of meals.
        public List<MealsItem> getMeals() {
            return meals;
        }

}
