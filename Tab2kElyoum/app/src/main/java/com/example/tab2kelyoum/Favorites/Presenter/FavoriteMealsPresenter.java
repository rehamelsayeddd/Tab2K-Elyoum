package com.example.tab2kelyoum.Favorites.Presenter;

import android.content.Context;

import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class FavoriteMealsPresenter {
    Context context;
    RepoistryLocal repositoryLocal;


    public FavoriteMealsPresenter(Context context) {
        this.context = context;
        repositoryLocal = new RepoistryLocal(context);
    }

    public Flowable<List<MealsItem>> returnStoredMealsItems() {
        return repositoryLocal.returnStoredMealsItems();
    }
}