package com.example.tab2kelyoum.SearchByIngredient.Presenter;

import com.example.tab2kelyoum.SearchByIngredient.Model.EachIngredientModel;

import java.util.List;

public interface InterfaceAllIngredients {
    public void responseOfDataOnSuccess(List<EachIngredientModel> ingredientsReceived);
}

