package com.example.tab2kelyoum.SearchbyArea.Presenter;

import com.example.tab2kelyoum.Repoistry.RepoistryRemote;

public class AllAreasPresenter {

    private InterfaceAllAreas interfaceAllAreas;
    private RepoistryRemote repoistryRemote;
    private static final String TAG = "ALL AREAS PRESENTER";

    public AllAreasPresenter(InterfaceAllAreas interfaceAllAreas) {
        this.interfaceAllAreas = interfaceAllAreas;
    }

    public void getAreas(){
        repoistryRemote = new RepoistryRemote(interfaceAllAreas);
        repoistryRemote.getAreas();
    }
}
