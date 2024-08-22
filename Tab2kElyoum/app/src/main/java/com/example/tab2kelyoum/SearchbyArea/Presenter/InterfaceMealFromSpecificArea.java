package com.example.tab2kelyoum.SearchbyArea.Presenter;

import com.example.tab2kelyoum.Model.MealsItem;

import java.util.List;

public interface InterfaceMealFromSpecificArea {
    //show list of meals from specific area on success
    public void responseOfDataOnSuccess(List<MealsItem> mealsList);
}
