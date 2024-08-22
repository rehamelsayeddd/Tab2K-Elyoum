package com.example.tab2kelyoum.SearchByCategory.Presenter;

import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class MealFromSpecificCategoryPresenter { private InterfaceMealFromSpecificCategory interfaceMealFromSpecificCategory;
    private static final String TAG = "MealFromSpecificCategoryPresenter";
    private RepoistryRemote repositoryRemote;

    public MealFromSpecificCategoryPresenter(InterfaceMealFromSpecificCategory interfaceMealFromSpecificCategory) {
        this.interfaceMealFromSpecificCategory = interfaceMealFromSpecificCategory;
    }


    public void getMealFromSpecificCategory(String categorySelected) {

        repositoryRemote = new RepoistryRemote(interfaceMealFromSpecificCategory);
        repositoryRemote.getMealFromSpecificCategory(categorySelected);

    }


}
