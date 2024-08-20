package com.example.tab2kelyoum.Repoistry;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tab2kelyoum.Database.DB;
import com.example.tab2kelyoum.Database.MealDao;
import com.example.tab2kelyoum.Model.MealsItem;

import com.example.tab2kelyoum.Home.Presenter.homepageInterface;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
public class RepoistryLocal {

    private static final String TAG = "Repository";
    private Context context;
    private MealDao mealDAO;
    private Flowable<List<MealsItem>> storedMealsItems; // Observable list of stored meals items (rxjava)
    private List<MealsItem> mealsItemsFromFirestore = new ArrayList<>();
    private List<MealsItem> mealsWeekPlanSaturday = new ArrayList<>(), mealsWeekPlanSunday = new ArrayList<>(), mealsWeekPlanMonday = new ArrayList<>(), mealsWeekPlanTuesday = new ArrayList<>(), mealsWeekPlanWednesday = new ArrayList<>(), mealsWeekPlanThursday = new ArrayList<>(), mealsWeekPlanFriday = new ArrayList<>();
    private homepageInterface homepageInterface; // Interface for responding to data loading events

    // Constructor initializing Room database
    public RepoistryLocal(Context context) {
        this.context = context;

        DB db = DB.getInstance(context);
        mealDAO = db.mealDAO();

        storedMealsItems = mealDAO.getStoredMealsItems();
    }

    // Constructor with InterfaceDailyInspirations for callback
    public RepoistryLocal(homepageInterface homepageInterface, Context context) {
        this.context = context;
        DB db = DB.getInstance(context);
        mealDAO = db.mealDAO();
        storedMealsItems = mealDAO.getStoredMealsItems();
        this.homepageInterface = homepageInterface; // Set callback interface
    }

    // Return Flowable of stored meals
    public Flowable<List<MealsItem>> returnStoredMealsItems() {
        return storedMealsItems;
    }

    // Delete a meal from Room database
    public void delete(MealsItem mealsItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDAO.deleteMeal(mealsItem);
            }
        }).start();
    }

    // Insert a meal into Room database
    public void insert(MealsItem mealsItem, String weekDay, String documentID) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealsItem.setWeekDay(weekDay);
                mealsItem.setDocumentID(documentID);
                mealDAO.insertMeal(mealsItem);
            }
        }).start();
    }

    // Find a meal by name and week day
    public MealsItem findMealByName(String mealName, String weekDayString) {
        return mealDAO.findMealByName(mealName, weekDayString);
    }

    // Load data from Firestore and populate Room database
    public void loadRoomFromFirestore() {
        getFavoriteMealsUsingFirestore();
        getWeekPlanMealsUsingFirestore();
    }

    // Delete all data from Room database
    public void deleteTableRoom() {
        mealDAO.deleteTableRoom();
    }

    // Fetch favorite meals from Firestore and insert into Room database
    private void getFavoriteMealsUsingFirestore() {
        FirebaseFirestore.getInstance().collection("userFavorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       // Check if the current document belongs to the logged-in user
                                                       if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                           // Create a MealsItem from the Firestore document
                                                           mealsItemsFromFirestore.add(new MealsItem(document.getId(),
                                                                   document.get("strMeal").toString(),
                                                                   document.get("strArea").toString(),
                                                                   document.get("strMealThumb").toString(),
                                                                   document.get("strInstructions").toString(),
                                                                   document.get("strYoutube").toString(),
                                                                   null
                                                           ));
                                                           // Insert the meal into Room database
                                                           insert(mealsItemsFromFirestore.get(mealsItemsFromFirestore.size() - 1), "NULL", document.getId());
                                                       }
                                                   }
                                               } else {
                                                   Log.i(TAG, "Error loading documents from Firestore to Room.", task.getException());
                                               }
                                           }
                                       }
                );
    }

    // Fetch week plan meals from Firestore and insert into Room database
    private void getWeekPlanMealsUsingFirestore() {
        FirebaseFirestore.getInstance().collection("userWeekPlan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       // Check if the current document belongs to the logged-in user
                                                       if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                           String weekDay = document.get("weekDay").toString();
                                                           MealsItem meal = new MealsItem(document.getId(),
                                                                   document.get("strMeal").toString(),
                                                                   document.get("strArea").toString(),
                                                                   document.get("strMealThumb").toString(),
                                                                   document.get("strInstructions").toString(),
                                                                   document.get("strYoutube").toString(),
                                                                   weekDay);
                                                           // Add meal to the appropriate list based on the weekday
                                                           switch (weekDay) {
                                                               case "Saturday":
                                                                   mealsWeekPlanSaturday.add(meal);
                                                                   insert(meal, "Saturday", document.getId());
                                                                   break;
                                                               case "Sunday":
                                                                   mealsWeekPlanSunday.add(meal);
                                                                   insert(meal, "Sunday", document.getId());
                                                                   break;
                                                               case "Monday":
                                                                   mealsWeekPlanMonday.add(meal);
                                                                   insert(meal, "Monday", document.getId());
                                                                   break;
                                                               case "Tuesday":
                                                                   mealsWeekPlanTuesday.add(meal);
                                                                   insert(meal, "Tuesday", document.getId());
                                                                   break;
                                                               case "Wednesday":
                                                                   mealsWeekPlanWednesday.add(meal);
                                                                   insert(meal, "Wednesday", document.getId());
                                                                   break;
                                                               case "Thursday":
                                                                   mealsWeekPlanThursday.add(meal);
                                                                   insert(meal, "Thursday", document.getId());
                                                                   break;
                                                               case "Friday":
                                                                   mealsWeekPlanFriday.add(meal);
                                                                   insert(meal, "Friday", document.getId());
                                                                   break;
                                                           }
                                                       }
                                                   }
                                                   // Notify the interface that data loading is complete
                                                   homepageInterface.responseOfLoadingDataFromFirestoreToRoom();
                                               } else {
                                                   Log.i(TAG, "Error loading documents from Firestore to Room.", task.getException());
                                               }
                                           }
                                       }
                );
    }
}

