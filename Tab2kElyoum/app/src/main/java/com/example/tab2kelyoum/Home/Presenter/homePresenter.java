package com.example.tab2kelyoum.Home.Presenter;

import android.content.Context;

import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public class homePresenter {

        private homepageInterface homepageInterface;
        private RepoistryRemote repositoryRemote;
        private RepoistryLocal repositoryLocal;
        Context context;


        public homePresenter(homepageInterface homepageInterface, Context context) {
            this.homepageInterface = homepageInterface;
            this.context = context;
        }

        public void getDailyInspirations() {
            repositoryRemote = new RepoistryRemote(homepageInterface);
            repositoryRemote.getDailyInspirations();
        }

        public Flowable<List<MealsItem>> returnStoredMealsItems() {
            repositoryLocal = new RepoistryLocal(context);
            return repositoryLocal.returnStoredMealsItems();
        }

        public void loadRoomFromFirestore() {
            repositoryLocal = new RepoistryLocal(homepageInterface, context);
            repositoryLocal.loadRoomFromFirestore();
        }


     //   public void loadHeaderTitle() {
       //     repositoryRemote = new RepoistryRemote();
         //   repositoryRemote.changeHeaderTitle();
        //}
    }

