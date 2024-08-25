package com.example.tab2kelyoum.Repoistry;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.tab2kelyoum.Database.DB;
import com.example.tab2kelyoum.Database.MealDao;
import com.example.tab2kelyoum.Home.Presenter.homepageInterface;
import com.example.tab2kelyoum.Model.MealsItem;
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
    private MealDao mealDAO; //local
    private Flowable<List<MealsItem>> storedMealsItems;
    private List<MealsItem> mealsItemsFromFirestore = new ArrayList<>();
    private List<MealsItem> mealsWeekPlanSaturday = new ArrayList<>(), mealsWeekPlanSunday = new ArrayList<>(), mealsWeekPlanMonday = new ArrayList<>(), mealsWeekPlanTuesday = new ArrayList<>(), mealsWeekPlanWednesday = new ArrayList<>(), mealsWeekPlanThursday = new ArrayList<>(), mealsWeekPlanFriday = new ArrayList<>();
    private homepageInterface homepageInterface;//as presenter to communicate with view

    public RepoistryLocal(Context context) {
        this.context = context;

        DB db = DB.getInstance(context);
        mealDAO = db.mealDAO();

        storedMealsItems = mealDAO.getStoredMealsItems();
    }

    public RepoistryLocal(homepageInterface homepageInterface, Context context) {
        this.context = context;

        DB db = DB.getInstance(context);
        mealDAO = db.mealDAO();

        storedMealsItems = mealDAO.getStoredMealsItems();

        this.homepageInterface = homepageInterface;
    }
//to return from room database
    public Flowable<List<MealsItem>> returnStoredMealsItems() {

        return storedMealsItems;
    }

    public void delete(MealsItem mealsItem) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mealDAO.deleteMeal(mealsItem);
            }
        }).start();
    }

  /** public Completable delete (MealsItem mealsItem) {
       return Completable.fromAction(() -> mealDAO.deleteMeal(mealsItem))
               .subscribeOn(Schedulers.io());
   }
**/


   /** public Completable insert (MealsItem mealsItem, String weekDay, String documentID) {
        return Completable.fromAction(() -> {
            mealsItem.setWeekDay(weekDay);
            mealsItem.setDocumentID(documentID);
            mealDAO.insertMeal(mealsItem);
        }).subscribeOn(Schedulers.io());
    }
   **/
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

    public MealsItem findMealByName(String mealName, String weekDayString) {
        return mealDAO.findMealByName(mealName, weekDayString);
    }

//to load meals from firestore and store them in roomDB to be shown offline
    public void loadRoomFromFirestore() {
        getFavoriteMealsUsingFirestore();
        getWeekPlanMealsUsingFirestore();
    }


    public void deleteTableRoom() {
        mealDAO.deleteTableRoom();
    }

//reterives the fav meals based on userEmail from firestore  and then inserts to roomDB
    private void getFavoriteMealsUsingFirestore() {
        FirebaseFirestore.getInstance().collection("userFavorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {

                                                       if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                           mealsItemsFromFirestore.add(new MealsItem(document.getId(),
                                                                           document.get("strMeal").toString(),
                                                                           document.get("strArea").toString(),
                                                                           document.get("strMealThumb").toString(),
                                                                           document.get("strInstructions").toString(),
                                                                           document.get("strYoutube").toString(),
                                                                           null
                                                                   )
                                                           );
                                                           insert(mealsItemsFromFirestore.get((mealsItemsFromFirestore.size() - 1)), "NULL", document.getId());

                                                       }


                                                   }


                                               } else {
                                                   Log.i(TAG, "Error loading documents from firestore to Room.", task.getException());
                                               }
                                           }
                                       }
                );
    }
//retervies based on userEmail and week of the day from firebase to roomDB
    private void getWeekPlanMealsUsingFirestore() {
        FirebaseFirestore.getInstance().collection("userWeekPlan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {

                                                       if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Saturday")) {
                                                           mealsWeekPlanSaturday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanSaturday.get((mealsWeekPlanSaturday.size() - 1)), "Saturday", document.getId());
                                                       } else if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Sunday")) {
                                                           mealsWeekPlanSunday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanSunday.get((mealsWeekPlanSunday.size() - 1)), "Sunday", document.getId());
                                                       } else if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Monday")) {
                                                           mealsWeekPlanMonday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanMonday.get((mealsWeekPlanMonday.size() - 1)), "Monday", document.getId());
                                                       } else if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Tuesday")) {
                                                           mealsWeekPlanTuesday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanTuesday.get((mealsWeekPlanTuesday.size() - 1)), "Tuesday", document.getId());
                                                       } else if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Wednesday")) {
                                                           mealsWeekPlanWednesday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanWednesday.get((mealsWeekPlanWednesday.size() - 1)), "Wednesday", document.getId());
                                                       } else if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Thursday")) {
                                                           mealsWeekPlanThursday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanThursday.get((mealsWeekPlanThursday.size() - 1)), "Thursday", document.getId());
                                                       } else if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").toString().equals("Friday")) {
                                                           mealsWeekPlanFriday.add(new MealsItem(document.getId(), document.get("strMeal").toString(), document.get("strArea").toString(), document.get("strMealThumb").toString(), document.get("strInstructions").toString(), document.get("strYoutube").toString(), document.get("weekDay").toString()));
                                                           insert(mealsWeekPlanFriday.get((mealsWeekPlanFriday.size() - 1)), "Friday", document.getId());
                                                       }

                                                   }
                                                   homepageInterface.responseOfLoadingDataFromFirestoreToRoom();
                                               } else {
                                                   Log.i(TAG, "Error loading documents from firestore to Room.", task.getException());
                                               }
                                           }
                                       }
                );

    }


}
