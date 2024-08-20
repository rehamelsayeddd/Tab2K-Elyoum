package com.example.tab2kelyoum.Register.Presenter;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface InterfaceRegister {
    void onCompleteRegisterWithEmailAndPassword(Task<AuthResult> task);

}
