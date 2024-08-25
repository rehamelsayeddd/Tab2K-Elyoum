package com.example.tab2kelyoum.Repoistry;


import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.tab2kelyoum.Connection.RetrofitClient;
import com.example.tab2kelyoum.SearchbyArea.Presenter.InterfaceAllAreas;
import com.example.tab2kelyoum.SearchByCategory.Presenter.InterfaceAllCategories;
import com.example.tab2kelyoum.SearchByIngredient.Presenter.InterfaceAllIngredients;
import com.example.tab2kelyoum.SearchbyMeal.Presenter.InterfaceAllMeals;
import com.example.tab2kelyoum.MainActivity.Presenter.InterfaceMain;
import com.example.tab2kelyoum.SearchbyArea.Presenter.InterfaceMealFromSpecificArea;
import com.example.tab2kelyoum.SearchByCategory.Presenter.InterfaceMealFromSpecificCategory;
import com.example.tab2kelyoum.SearchByIngredient.Presenter.InterfaceMealFromSpecificIngredient;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.Register.Presenter.InterfaceRegister;
import com.example.tab2kelyoum.SignIn.Presenter.InterfaceSignIn;
import com.example.tab2kelyoum.SearchbyArea.Model.EachAreaModel;
import com.example.tab2kelyoum.SearchByCategory.Model.EachCategoryModel;
import com.example.tab2kelyoum.SearchByIngredient.Model.EachIngredientModel;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.SearchbyArea.Model.RootAreasList;
import com.example.tab2kelyoum.SearchByCategory.Model.RootCategoriesList;
import com.example.tab2kelyoum.SearchByIngredient.Model.RootIngredientsList;
import com.example.tab2kelyoum.Model.RootMeal;
import com.example.tab2kelyoum.SearchbyMeal.Model.RootMealsFromSingleLetter;
import com.example.tab2kelyoum.Home.Presenter.homepageInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

public class RepoistryRemote {

        private homepageInterface homepageInterface;
        private InterfaceRegister interfaceRegister;
        private InterfaceAllAreas interfaceAllAreas;
        private InterfaceMealFromSpecificArea interfaceMealFromSpecificArea;
        private InterfaceAllCategories interfaceAllCategories;
        private InterfaceMealFromSpecificCategory interfaceMealFromSpecificCategory;
        private InterfaceAllIngredients interfaceAllIngredients;
        private InterfaceMealFromSpecificIngredient interfaceMealFromSpecificIngredient;
        private InterfaceAllMeals interfaceAllMeals;
        private InterfaceSignIn interfaceSignIn;
        private InterfaceMain interfaceMain;
        private Context context;
        private static final String TAG = "RepositoryRemote";
        private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        private RetrofitClient retrofitClient = RetrofitClient.getInstance();
        private List<MealsItem> meals = new ArrayList<>();
        private List<EachAreaModel> areas;
        private List<EachCategoryModel> categories;
        private List<EachIngredientModel> ingredients;


        public RepoistryRemote(homepageInterface homepageInterface) {
            this.homepageInterface = homepageInterface;
        }

        public RepoistryRemote(InterfaceRegister interfaceRegister) {
            this.interfaceRegister = interfaceRegister;
        }

        public RepoistryRemote(InterfaceAllAreas interfaceAllAreas) {
            this.interfaceAllAreas = interfaceAllAreas;
        }

        public RepoistryRemote(InterfaceMealFromSpecificArea interfaceMealFromSpecificArea) {
            this.interfaceMealFromSpecificArea = interfaceMealFromSpecificArea;
        }

        public RepoistryRemote(InterfaceAllCategories interfaceAllCategories) {
            this.interfaceAllCategories = interfaceAllCategories;
        }

        public RepoistryRemote(InterfaceMealFromSpecificCategory interfaceMealFromSpecificCategory) {
            this.interfaceMealFromSpecificCategory = interfaceMealFromSpecificCategory;
        }

        public RepoistryRemote(InterfaceAllIngredients interfaceAllIngredients) {
            this.interfaceAllIngredients = interfaceAllIngredients;
        }

        public RepoistryRemote(InterfaceMealFromSpecificIngredient interfaceMealFromSpecificIngredient) {
            this.interfaceMealFromSpecificIngredient = interfaceMealFromSpecificIngredient;
        }

        public RepoistryRemote(InterfaceAllMeals interfaceAllMeals) {
            this.interfaceAllMeals = interfaceAllMeals;
        }

        public RepoistryRemote(InterfaceSignIn interfaceSignIn, Context context) {
            this.interfaceSignIn = interfaceSignIn;
            this.context = context;
        }

        public RepoistryRemote() {

        }

        public RepoistryRemote(InterfaceMain interfaceMain) {
            this.interfaceMain = interfaceMain;
        }

        //fetching random meals from api and passes to homepage interface
        public void getDailyInspirations() {

            String[] countriesList = {"Indian", "Italian", "Chinese", "French", "British"};
            String randomCountry = countriesList[(new Random()).nextInt(countriesList.length)];

            Observable<RootMeal> observableRandom1 = retrofitClient.getMyApi().getRootRandom();
            Observable<RootMeal> observableRandom2 = retrofitClient.getMyApi().getRootRandom();
            Observable<RootMeal> observableRandom3 = retrofitClient.getMyApi().getRootRandom();
            Observable<RootMeal> observableRandom4 = retrofitClient.getMyApi().getRootRandom();
            Observable<RootMeal> observableRandom5 = retrofitClient.getMyApi().getRootRandom();

            ArrayList<Observable<RootMeal>> arrayListObservablesRandomMeal = new ArrayList<>(Arrays.asList(observableRandom1, observableRandom2, observableRandom3, observableRandom4, observableRandom5));

            Observable<RootMeal> combinedObservable = Observable.merge(arrayListObservablesRandomMeal);

            combinedObservable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            response -> {

                                for (int i = 0; i < response.getMeals().size(); i++) {
                                    meals.add(response.getMeals().get(i));
                                }

                            },

                            error -> {
                                homepageInterface.responseOfDataOnFailure(error);
                            },
                            () -> {
                                homepageInterface.responseOfDataOnSuccess(meals);
                            }
                    );

        }

        //to register user with firebase
        public void createUserWithEmailAndPassword(String email, String password) {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    interfaceRegister.onCompleteRegisterWithEmailAndPassword(task);
                }
            }) ;
        }
//fetch list of countries  from api
        public void getAreas() {
            Observable<RootAreasList> observableAreas = retrofitClient.getMyApi().getRootAreasList();
            observableAreas.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    response -> {
                        areas = response.getMeals();

                    },
                    error -> {
                        error.printStackTrace();
                    },
                    () -> {
                        interfaceAllAreas.responseOfDataOnSuccess(areas);
                    }
            );
        }

//fetching by meals by countries and passes result to interface
        public void getMealFromSpecificCountry(String areaSelected) {
        retrofitClient.getMyApi()
                .getMealsOfSelectedArea(areaSelected)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            meals = response.getMeals();
                            interfaceMealFromSpecificArea.responseOfDataOnSuccess(meals);
                        },
                        error -> {
                            Log.e(TAG, "Error fetching meals by area: " + error.getMessage(), error);
                        }
                );
    }

//fetch all categories from api and passes it to interface
        public void getAllCategories() {
            Observable<RootCategoriesList> observableCategory = retrofitClient.getMyApi().getRootCategoriesList();

            observableCategory.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    response -> {
                        Log.i(TAG, "getAllCategories: " + response.getCategories());
                        categories = response.getCategories();
                        Log.i(TAG, "getAllCategories:---- " + categories.size());


                    },
                    error -> {
                        error.printStackTrace();
                    },
                    () -> {
                        interfaceAllCategories.responseOfDataOnSuccess(categories);
                    }
            );
        }

//fetch meal based on category and pass it to interface
        public void getMealFromSpecificCategory(String categorySelected) {
            retrofitClient.getMyApi()
                    .getMealsOfSelectedCategory(categorySelected)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            next -> {
                                meals = next.getMeals();


                            },
                            error -> {
                                Log.i(TAG, "getCategoryMeal: " + error.getMessage());
                            },
                            () -> {
                                interfaceMealFromSpecificCategory.responseOfDataOnSuccess(meals);

                            }
                    );
        }

      //fetch all ingrideints from api
        public void getAllIngredients() {
            Observable<RootIngredientsList> observableCategory = RetrofitClient.getInstance().getMyApi().getRootIngredientsList();

            observableCategory.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    response -> {
                        ingredients = response.getMeals();


                    },
                    error -> {
                        error.printStackTrace();
                    },
                    () -> {
                        interfaceAllIngredients.responseOfDataOnSuccess(ingredients);
                    }
            );


        }

//fetch all meals by specific Ingrident and pass it to interface
        public void getMealFromSpecificIngredient(String ingredientSelected) {
            RetrofitClient.getInstance().getMyApi()
                    .getMealsOfSelectedIngredient(ingredientSelected)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            next -> {
                                meals = next.getMeals();

                            },
                            error -> {
                                Log.i(TAG, "onViewCreated: " + error.getMessage());
                            },
                            () -> {
                                interfaceMealFromSpecificIngredient.responseOfDataOnSuccess(meals);
                            }
                    );
        }
//fetchs the meals by specific letter
        public void getAllMeals(Character s) {
            Observable<RootMealsFromSingleLetter> observable = RetrofitClient.getInstance().getMyApi().getRootMealsBySingleLetter(s.toString());
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    response -> {

                        if (response.getMeals() != null) {
                            meals = response.getMeals();

                        }

                    },
                    error -> {
                        error.printStackTrace();
                    },
                    () -> {
                        interfaceAllMeals.responseOfDataOnSuccess(meals);
                    }
            );
        }
//usin firebase Auth and pass result to interface
        public void signIn(String email, String password) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    interfaceSignIn.onCompleteSignInWithEmailAndPassword(task);

                }
            }) ;

        }

    public void signInGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.client_id)) // Fetch client ID from resources
                .requestEmail()
                .build();
        GoogleSignInClient gsc = GoogleSignIn.getClient(context, gso);

        Intent signInIntent = gsc.getSignInIntent();

        // Assuming interfaceSignIn is a valid instance of a class implementing the appropriate interface
        interfaceSignIn.onCompleteSignInIntent(signInIntent, 1000); // Ensure this method is correctly implemented
    }

//to handle the process of signin in google
    public void respondToActivityResultOfGoogleSignIn(int requestCode, int resultCode, Intent data) {
            if (requestCode == 1000) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(task.getResult().getIdToken(), null)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        interfaceSignIn.onCompleteGoogleSignIn(task);
                    }
                }) ;
            }
        }


      //  public void changeHeaderTitle() {
          //  MainActivity.tv_headerDrawer.setText(firebaseAuth.getCurrentUser().getEmail().split("@")[0]);
        //}

        public void deleteDataForThisUser() {
            List<String> documentIDs = new ArrayList<>();
            getMealsToBeDeletedFromFavorites(documentIDs);
        }
//fetches and delete user's meals from firestore using userEmail
        private void getMealsToBeDeletedFromFavorites(List<String> documentIDs) {

            FirebaseFirestore.getInstance().collection("userFavorites")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                   if (task.isSuccessful()) {
                                                       for (QueryDocumentSnapshot document : task.getResult()) {

                                                           if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                               //documentIDs.add(document.getId());
                                                               FirebaseFirestore.getInstance().collection("userFavorites").document(document.getId())
                                                                       .delete();
                                                           }
                                                       }

                                                   } else {
                                                       Log.i(TAG, "Error loading documents from firestore to Room.", task.getException());
                                                   }
                                               }
                                           }
                    );

            FirebaseFirestore.getInstance().collection("userWeekPlan")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                   if (task.isSuccessful()) {
                                                       for (QueryDocumentSnapshot document : task.getResult()) {

                                                           if (document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                               FirebaseFirestore.getInstance().collection("userWeekPlan").document(document.getId())
                                                                       .delete();
                                                           }
                                                       }

                                                       interfaceMain.onFinishedDeletingItemsOfThisAccount();
                                                   } else {
                                                       Log.i(TAG, "Error loading documents from firestore to Room.", task.getException());
                                                   }
                                               }
                                           }
                    );


        }
//delete firebase account and passing it to interface
        public void deleteAccount() {
            firebaseAuth.getCurrentUser().delete();
            interfaceMain.onFinishedDeletingAccount();
        }
    }
