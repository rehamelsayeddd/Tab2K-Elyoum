package com.example.tab2kelyoum.SignIn.Presenter;

import android.content.Context;
import android.content.Intent;

import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class SignInPresenter {

    private InterfaceSignIn interfaceSignIn;
    private Context context;
    private static final String TAG = "PresenterFirebaseSignin";
    private RepoistryRemote repositoryRemote;
    private RepoistryLocal repositoryLocal;


    public SignInPresenter(InterfaceSignIn interfaceSignIn, Context context) {
        this.interfaceSignIn = interfaceSignIn;
        this.context = context;
    }

    public SignInPresenter(Context context) {
        this.context = context;
        repositoryLocal = new RepoistryLocal(context);
    }

    public void signIn(String email, String password) {

        repositoryRemote = new RepoistryRemote(interfaceSignIn, context);
        repositoryRemote.signIn(email, password);


    }

    public void signInGoogle() {

        repositoryRemote = new RepoistryRemote(interfaceSignIn, context);
        repositoryRemote.signInGoogle();

    }

    public void respondToActivityResultOfGoogleSignIn(int requestCode, int resultCode, Intent data) {

        repositoryRemote = new RepoistryRemote(interfaceSignIn, context);
        repositoryRemote.respondToActivityResultOfGoogleSignIn(requestCode, resultCode, data);


    }

}
