package com.example.tab2kelyoum.SearchByCategory.Presenter;

import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class AllCategoriesPresenter {private InterfaceAllCategories interfaceAllCategories;
    private static final String TAG = "AllCategoriesPresenter";
    private RepoistryRemote repositoryRemote;

    public AllCategoriesPresenter(InterfaceAllCategories interfaceAllCategories) {
        this.interfaceAllCategories = interfaceAllCategories;
    }

    public void getAllCategories() {

        repositoryRemote = new RepoistryRemote(interfaceAllCategories);
        repositoryRemote.getAllCategories();


    }


}
