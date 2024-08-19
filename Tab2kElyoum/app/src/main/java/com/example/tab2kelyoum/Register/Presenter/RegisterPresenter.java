package com.example.tab2kelyoum.Register.Presenter;

import android.content.Context;

import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class RegisterPresenter {private static final String TAG = "PresenterFirebaseRegister";

    private InterfaceRegister interfaceRegister;
    private RepoistryRemote repositoryRemote;
    private Context context;
    private RepoistryLocal repositoryLocal;


    public RegisterPresenter(InterfaceRegister interfaceRegister) {
        this.interfaceRegister = interfaceRegister;
    }

    public RegisterPresenter(Context context) {

        this.context = context;
        repositoryLocal = new RepoistryLocal(context);

    }

    public void createUserWithEmailAndPassword(String email, String password) {
        repositoryRemote = new RepoistryRemote(interfaceRegister);
        repositoryRemote.createUserWithEmailAndPassword(email, password);
    }


}
