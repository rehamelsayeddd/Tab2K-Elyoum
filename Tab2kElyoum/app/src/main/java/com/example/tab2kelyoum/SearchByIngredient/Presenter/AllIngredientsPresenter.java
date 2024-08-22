package com.example.tab2kelyoum.SearchByIngredient.Presenter;

import com.example.tab2kelyoum.Repoistry.RepoistryRemote;
import com.example.tab2kelyoum.SearchByIngredient.Model.EachIngredientModel;

import java.util.List;

public class AllIngredientsPresenter {

    private InterfaceAllIngredients interfaceAllIngredients;
    private List<EachIngredientModel> ingredients;
    private static final String TAG = "AllIngredientsPresenter";
    private RepoistryRemote repositoryRemote;

    public AllIngredientsPresenter(InterfaceAllIngredients interfaceAllIngredients) {
        this.interfaceAllIngredients = interfaceAllIngredients;
    }

    public void getAllIngredients() {
        repositoryRemote = new RepoistryRemote(interfaceAllIngredients);
        repositoryRemote.getAllIngredients();
    }

}

