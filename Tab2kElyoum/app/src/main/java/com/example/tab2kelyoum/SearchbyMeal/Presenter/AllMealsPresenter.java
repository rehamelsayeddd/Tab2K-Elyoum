package com.example.tab2kelyoum.SearchbyMeal.Presenter;

import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

import java.util.List;

public class AllMealsPresenter { private InterfaceAllMeals interfaceAllMeals;
    private List<MealsItem> meals;
    private RepoistryRemote repositoryRemote;

    public AllMealsPresenter(InterfaceAllMeals interfaceAllMeals) {
        this.interfaceAllMeals = interfaceAllMeals;
    }

    public void getAllMeals(Character s) {

        repositoryRemote = new RepoistryRemote(interfaceAllMeals);
        repositoryRemote.getAllMeals(s);


    }
}