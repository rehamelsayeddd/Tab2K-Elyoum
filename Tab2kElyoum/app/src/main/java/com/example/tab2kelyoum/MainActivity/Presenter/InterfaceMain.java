package com.example.tab2kelyoum.MainActivity.Presenter;

public interface InterfaceMain {

    public void onFinishedDeletingItemsOfThisAccount();

    public void onFinishedDeletingAccount();

    public void onLogoutSuccess();

    public void onLogoutFailure();

    public void onNoInternetConnection();
    public void onAccountDeleted();
    public void onAccountDeletionFailed();

    public void showNoInternetConnection();
    public void showBackInternetConnection();
}
