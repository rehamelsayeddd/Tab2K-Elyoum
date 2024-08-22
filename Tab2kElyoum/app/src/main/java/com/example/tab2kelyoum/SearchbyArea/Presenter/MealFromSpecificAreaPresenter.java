package com.example.tab2kelyoum.SearchbyArea.Presenter;

import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

import java.util.ArrayList;
import java.util.List;

public class MealFromSpecificAreaPresenter {

    private InterfaceMealFromSpecificArea interfaceMealFromSpecificArea;
    private List<MealsItem> meals = new ArrayList();
    private RepoistryRemote repoistryRemote;
    private static final String TAG = "MEAL FROM SPECIFIC AREA PRESENTER";

    public MealFromSpecificAreaPresenter(InterfaceMealFromSpecificArea interfaceMealFromSpecificArea) {
        this.interfaceMealFromSpecificArea = interfaceMealFromSpecificArea;
    }

 public void getMealFromSpecificCountry (String countrySelected){
        repoistryRemote = new RepoistryRemote(interfaceMealFromSpecificArea);
        repoistryRemote.getMealFromSpecificCountry(countrySelected);
 }
}
