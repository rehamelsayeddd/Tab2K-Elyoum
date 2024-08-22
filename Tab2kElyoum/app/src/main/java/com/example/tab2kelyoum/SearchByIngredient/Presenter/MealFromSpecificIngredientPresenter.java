package com.example.tab2kelyoum.SearchByIngredient.Presenter;

import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class MealFromSpecificIngredientPresenter {

    private InterfaceMealFromSpecificIngredient interfaceMealFromSpecificIngredient;
    private static final String TAG = "MealFromSpecificIngredientPresenter";
    private RepoistryRemote repositoryRemote;

    public MealFromSpecificIngredientPresenter(InterfaceMealFromSpecificIngredient interfaceMealFromSpecificIngredient) {
        this.interfaceMealFromSpecificIngredient = interfaceMealFromSpecificIngredient;
    }

    public void getMealFromSpecificIngredient(String ingredientSelected) {

        repositoryRemote = new RepoistryRemote(interfaceMealFromSpecificIngredient);
        repositoryRemote.getMealFromSpecificIngredient(ingredientSelected);
    }
}
