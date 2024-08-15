package com.example.tab2kelyoum;

import com.example.tab2kelyoum.Model.MealsItem;

import java.util.List;
//at the homepage screen
public interface homepageInterface {
    // Called when data is successfully retrieved. It passes a list of meal items.
    public void responseOfDataOnSuccess(List<MealsItem> mealsList);

    // Called when there is an error during data retrieval. It passes the error message.
    public void responseOfDataOnFailure(Throwable error);

    // Called when data has been successfully loaded from Firestore to Room database.
    public void responseOfLoadingDataFromFirestoreToRoom();
}
