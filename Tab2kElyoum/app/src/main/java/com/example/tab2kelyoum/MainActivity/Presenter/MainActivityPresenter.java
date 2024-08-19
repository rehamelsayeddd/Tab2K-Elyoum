package com.example.tab2kelyoum.MainActivity.Presenter;

import android.content.Context;

import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class MainActivityPresenter {
    Context context;
    RepoistryLocal repositoryLocal;
    RepoistryRemote repositoryRemote;
    InterfaceMain interfaceMain;

    public MainActivityPresenter(Context context) {
        this.context = context;
        repositoryLocal = new RepoistryLocal(context);
    }

    public MainActivityPresenter(InterfaceMain interfaceMain) {
        this.interfaceMain = interfaceMain;
    }

    public MainActivityPresenter() {

    }


    public void deleteTableRoom() {
        new Thread(() -> repositoryLocal.deleteTableRoom()).start();
    }

    public void deleteAccountData() {
        repositoryRemote = new RepoistryRemote(interfaceMain);
        repositoryRemote.deleteDataForThisUser();
    }

    public void deleteAccount() {
        repositoryRemote = new RepoistryRemote(interfaceMain);
        repositoryRemote.deleteAccount();
    }

    public void loadHeaderTitle() {
        repositoryRemote = new RepoistryRemote();
        repositoryRemote.changeHeaderTitle();
    }
}


