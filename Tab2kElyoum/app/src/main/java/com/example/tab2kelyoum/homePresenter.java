package com.example.tab2kelyoum;

import android.content.Context;

import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class homePresenter {

    private homepageInterface homepageInterface;
    private RepoistryLocal repoistryLocal;
    private RepoistryRemote repoistryRemote;
    Context context;
//constructor to  enable the presenter to communicate with the view and access resources.
    public homePresenter (homepageInterface homepageInterface , Context context){
        this.homepageInterface=homepageInterface;
        this.context=context;
    }
    //fetching daily inspo from api and update the view
    public void getDailyInspo(){
        repoistryRemote =new RepoistryRemote(homepageInterface);
        repoistryRemote.getDailyInspirations();
    }
 // to reterive the stored meals in local DB
    public Flowable<List<MealsItem>> returnStoredMeals(){
        repoistryLocal= new RepoistryLocal(context);
        return repoistryLocal.returnStoredMealsItems();
    }
    //calls local repo to load data from firestore into local roomDATABASE
    public void loadFromfirestire (){
        repoistryLocal = new RepoistryLocal(homepageInterface , context);
        repoistryLocal.loadRoomFromFirestore();
    }

    public void loadTitle(){
        repoistryRemote= new RepoistryRemote();
        repoistryRemote.changeHeaderTitle();
    }


}
