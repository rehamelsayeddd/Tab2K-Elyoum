package com.example.tab2kelyoum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tab2kelyoum.Connection.RetrofitClient;
import com.example.tab2kelyoum.Home.View.TodayPlannerAdapter;
import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MealDetailsFragment extends Fragment {

    private TextView mealName, area, instructions;
    private ImageView mealImage;
    private YouTubePlayerView videoView;
    private RecyclerView recyclerView;
    private List<String> ingrediant = new ArrayList<>();
    private List<String> megure = new ArrayList<>();
    private MealDetailIngrediantAdapter mealDetailIngrediantAdapter;
    private String[] split;
    private Boolean youtubeURLisExists = false;
    private ImageButton btn_addToFavorites_meal_details;
    private AutoCompleteTextView autoCompleteTextView;
    private TextInputLayout textInputLayout;
    private ProgressDialog progressDialog;
    private Boolean isAlreadyInFavorites;
    private RepoistryLocal rep;
    private static final String TAG = "MealDetailsFragment";
    private MealsItem mealsItem;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();
    private String[] weekDays = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private ArrayAdapter<String> arrayAdapter;
    private ImageButton btn_addToCalendar;
    private MealsItem mealsItemSelectedFull;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //  mealsItem = MealDetailsFragmentArgs.fromBundle(getArguments()).getMealDetailsArgs();

        mealName = view.findViewById(R.id.tv_mealName);
        area = view.findViewById(R.id.tv_meal_area);
        instructions = view.findViewById(R.id.tv_Meal_instructions);
        mealImage = view.findViewById(R.id.mealImage);
        videoView = view.findViewById(R.id.video);
        recyclerView = view.findViewById(R.id.rv_ingrediant);
        btn_addToFavorites_meal_details = view.findViewById(R.id.btn_addToFavorites_meal_details);
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        btn_addToCalendar = view.findViewById(R.id.btn_addToCalendar);

        arrayAdapter = new ArrayAdapter<String>(requireContext(), R.layout.list_weekdays, weekDays);
        autoCompleteTextView.setAdapter(arrayAdapter);

        getLifecycle().addObserver((LifecycleObserver) videoView);
        if (!mealsItem.getStrYoutube().isEmpty()) {

            split = mealsItem.getStrYoutube().split("=");
            youtubeURLisExists = true;
            videoView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    if (youtubeURLisExists) {
                        String videoId = split[1];
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                }
            });
        } else {
            videoView.setVisibility(View.GONE);
        }

        mealName.setText(mealsItem.getStrMeal());
        area.setText(mealsItem.getStrArea());
        Glide.with(view.getContext()).load(mealsItem.getStrMealThumb()).into(mealImage);
        instructions.setText(mealsItem.getStrInstructions());

        getIngredient(mealsItem.getStrIngredient1());
        getIngredient(mealsItem.getStrIngredient2());
        getIngredient(mealsItem.getStrIngredient3());
        getIngredient(mealsItem.getStrIngredient4());
        getIngredient(mealsItem.getStrIngredient5());
        getIngredient(mealsItem.getStrIngredient6());
        getIngredient(mealsItem.getStrIngredient7());
        getIngredient(mealsItem.getStrIngredient8());
        getIngredient(mealsItem.getStrIngredient9());
        getIngredient(mealsItem.getStrIngredient10());
        getIngredient(mealsItem.getStrIngredient11());
        getIngredient(mealsItem.getStrIngredient12());
        getIngredient(mealsItem.getStrIngredient13());
        getIngredient(mealsItem.getStrIngredient15());
        getMegure(mealsItem.getStrMeasure1());
        getMegure(mealsItem.getStrMeasure2());
        getMegure(mealsItem.getStrMeasure3());
        getMegure(mealsItem.getStrMeasure4());
        getMegure(mealsItem.getStrMeasure5());
        getMegure(mealsItem.getStrMeasure6());
        getMegure(mealsItem.getStrMeasure7());
        getMegure(mealsItem.getStrMeasure8());
        getMegure(mealsItem.getStrMeasure9());
        getMegure(mealsItem.getStrMeasure10());
        getMegure(mealsItem.getStrMeasure11());
        getMegure(mealsItem.getStrMeasure12());
        getMegure(mealsItem.getStrMeasure13());
        getMegure(mealsItem.getStrMeasure15());


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        mealDetailIngrediantAdapter = new MealDetailIngrediantAdapter(ingrediant, megure);
        recyclerView.setAdapter(mealDetailIngrediantAdapter);

        if (MainActivity.isLoginAsGuest == false) {
            btn_addToFavorites_meal_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NetworkChecker networkChecker = NetworkChecker.getInstance();

                    if (!networkChecker.checkIfInternetIsConnected()) {
                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to save meals to your favorites.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (networkChecker.checkIfInternetIsConnected()) {
                        checkIfItemAlreadyExistsInFavoritesOfFirestore(mealsItem);
                    }
                }
            });

            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positionDay, long id) {

                    NetworkChecker networkChecker = NetworkChecker.getInstance();

                    if (!networkChecker.checkIfInternetIsConnected()) {
                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to save meals to your week plan.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (networkChecker.checkIfInternetIsConnected()) {
                        String daySelected = parent.getItemAtPosition(positionDay).toString();
                        checkIfItemAlreadyExistsInWeekPlan(mealsItem, daySelected);

                    }

                }
            });


        } else if (MainActivity.isLoginAsGuest == true) {
            btn_addToFavorites_meal_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(requireContext(), "You need to log in to be able to save meals to your favorites.", Toast.LENGTH_SHORT).show();

                }
            });

            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positionDay, long id) {
                    Toast.makeText(requireContext(), "You need to log in to be able to save meals to your week plan.", Toast.LENGTH_SHORT).show();
                }
            });


        }

        btn_addToCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Navigation.findNavController(view).navigate(MealDetailsFragmentDirections.actionMealDeatailsFragmentToNavCalendar(mealsItem.getStrMeal()));


            }
        });


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_meal_details, container, false);
    }

    public MealDetailsFragment() {

    }

    private List<String> getIngredient(String ingredientName) {
        if (ingredientName != null && !ingredientName.isEmpty())
            ingrediant.add(ingredientName);
        return ingrediant;
    }

    private List<String> getMegure(String ingredientName) {
        if (ingredientName != null && !ingredientName.isEmpty())
            megure.add(ingredientName);
        return megure;
    }

    private void checkIfItemAlreadyExistsInFavoritesOfFirestore(MealsItem mealsItemSelected) {
        isAlreadyInFavorites = false;
        FirebaseFirestore.getInstance().collection("userFavorites")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               mealsItemSelected.setWeekDay("NULL");
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {
                                                       if (document.get("strMeal").equals(mealsItemSelected.getStrMeal()) & document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                                                           isAlreadyInFavorites = true;
                                                           mealsItemSelected.documentID = document.getId();
                                                       }
                                                   }
                                                   if (!isAlreadyInFavorites) {

                                                       RetrofitClient.getInstance().getMyApi().getMealById(Integer.parseInt(mealsItemSelected.getIdMeal()))
                                                               .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                                       next -> {
                                                                           mealsItemSelectedFull = next.getMeals().get(0);
                                                                       },
                                                                       error -> {
                                                                       },
                                                                       () -> {
                                                                           uploadDataToFireStoreInFavorites(mealsItemSelectedFull);
                                                                       }
                                                               );


                                                   } else {
                                                       AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                                                       builder.setTitle("This item is already in your favorite meals list.");
                                                       builder.setMessage("Would you like to remove it?");
                                                       builder.setCancelable(true);

                                                       builder.setPositiveButton("Remove it.", (DialogInterface.OnClickListener) (dialog, which) -> {
                                                           if (!networkChecker.checkIfInternetIsConnected()) {
                                                               MainActivity.mainActivity.runOnUiThread(new Runnable() {
                                                                   @Override
                                                                   public void run() {
                                                                       Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to remove meals from your favorites.", Toast.LENGTH_SHORT).show();
                                                                   }
                                                               });

                                                           } else if (networkChecker.checkIfInternetIsConnected()) {
                                                               progressDialog = new ProgressDialog(requireContext());
                                                               progressDialog.setTitle("Removing favorites");
                                                               progressDialog.setMessage("Please wait while removing the selected item from your favorite meals.");
                                                               progressDialog.setCanceledOnTouchOutside(true);
                                                               progressDialog.show();
                                                               FirebaseFirestore.getInstance().collection("userFavorites").document(mealsItemSelected.documentID)
                                                                       .delete()
                                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                           @Override
                                                                           public void onSuccess(Void aVoid) {
                                                                               progressDialog.dismiss();
                                                                               Toast.makeText(requireContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                                               Log.i(TAG, "DocumentSnapshot successfully deleted!");

                                                                               rep = new RepoistryLocal(requireContext());
                                                                               rep.delete(mealsItemSelected);

                                                                           }
                                                                       })
                                                                       .addOnFailureListener(new OnFailureListener() {
                                                                           @Override
                                                                           public void onFailure(@NonNull Exception e) {
                                                                               progressDialog.dismiss();
                                                                               Toast.makeText(requireContext(), "Item removal failed", Toast.LENGTH_SHORT).show();
                                                                               Log.i(TAG, "Error deleting document", e);
                                                                           }
                                                                       });
                                                           }

                                                       });
                                                       builder.setNegativeButton("Keep it.", (DialogInterface.OnClickListener) (dialog, which) -> {

                                                       });


                                                       AlertDialog alertDialog = builder.create();
                                                       alertDialog.show();

                                                   }
                                               } else {
                                                   Log.i(TAG, "Error getting documents.", task.getException());
                                               }
                                           }
                                       }
                );
    }

    private void uploadDataToFireStoreInFavorites(MealsItem mealsItem) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Adding to favorites");
        progressDialog.setMessage("Please wait while adding the selected item to your favorite meals.");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();


        Map<String, Object> userFavorites = new HashMap<>();

        userFavorites.put("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        userFavorites.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userFavorites.put("timeAdded", System.currentTimeMillis());
        userFavorites.put("strMeal", mealsItem.getStrMeal());
        userFavorites.put("strArea", mealsItem.getStrArea());
        userFavorites.put("strMealThumb", mealsItem.getStrMealThumb());
        userFavorites.put("strYoutube", mealsItem.getStrYoutube());
        userFavorites.put("strInstructions", mealsItem.getStrInstructions());
        userFavorites.put("weekDay", "NULL");


        FirebaseFirestore.getInstance().collection("userFavorites")
                .add(userFavorites)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        rep = new RepoistryLocal(requireContext());
                        rep.insert(mealsItem, "NULL", documentReference.getId());

                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Error adding the item", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    private void checkIfItemAlreadyExistsInWeekPlan(MealsItem mealsItemSelected, String weekDay) {
        isAlreadyInFavorites = false;
        FirebaseFirestore.getInstance().collection("userWeekPlan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                               mealsItemSelected.setWeekDay(weekDay);
                                               if (task.isSuccessful()) {
                                                   for (QueryDocumentSnapshot document : task.getResult()) {

                                                       if (document.get("strMeal").equals(mealsItemSelected.getStrMeal()) & document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) & document.get("weekDay").equals(weekDay)) {
                                                           Log.i(TAG, "onComplete: here i am!!");
                                                           isAlreadyInFavorites = true;
                                                           mealsItemSelected.documentID = document.getId();
                                                       }
                                                   }
                                                   if (!isAlreadyInFavorites) {
                                                       RetrofitClient.getInstance().getMyApi().getMealById(Integer.parseInt(mealsItemSelected.getIdMeal()))
                                                               .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                                                                       next -> {
                                                                           mealsItemSelectedFull = next.getMeals().get(0);
                                                                       },
                                                                       error -> {
                                                                       },
                                                                       () -> {
                                                                           uploadDataToFireStoreInWeekPlan(mealsItemSelectedFull, weekDay);
                                                                       }
                                                               );
                                                   } else {
                                                       AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                                                       builder.setTitle("This item is already in your week plan on this day.");
                                                       builder.setMessage("Would you like to remove it?");
                                                       builder.setCancelable(true);
                                                       builder.setPositiveButton("Remove it.", (DialogInterface.OnClickListener) (dialog, which) -> {
                                                           if (!networkChecker.checkIfInternetIsConnected()) {
                                                               MainActivity.mainActivity.runOnUiThread(new Runnable() {
                                                                   @Override
                                                                   public void run() {
                                                                       Toast.makeText(MainActivity.mainActivity, "Turn internet on to remove meals from your week plan.", Toast.LENGTH_SHORT).show();
                                                                   }
                                                               });

                                                           } else if (networkChecker.checkIfInternetIsConnected()) {
                                                               progressDialog = new ProgressDialog(requireContext());
                                                               progressDialog.setTitle("Removing favorites");
                                                               progressDialog.setMessage("Please wait while removing the selected item from your favorite meals.");
                                                               progressDialog.setCanceledOnTouchOutside(true);
                                                               progressDialog.show();

                                                               FirebaseFirestore.getInstance().collection("userWeekPlan").document(mealsItemSelected.documentID)
                                                                       .delete()
                                                                       .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                           @RequiresApi(api = Build.VERSION_CODES.O)
                                                                           @Override
                                                                           public void onSuccess(Void aVoid) {
                                                                               progressDialog.dismiss();
                                                                               Toast.makeText(requireContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
                                                                               Log.i(TAG, "DocumentSnapshot successfully deleted!");
                                                                               //(FavoriteMealsAdapter.this).notifyDataSetChanged();

                                                                               rep = new RepoistryLocal(requireContext());
                                                                               rep.delete(mealsItemSelected);

                                                                               if (weekDay.toLowerCase().equals(LocalDate.now().getDayOfWeek().name().toLowerCase()))
                                                                                   TodayPlannerAdapter.getInstance().mealRemovedFromDailyInspirations(mealsItemSelected);


                                                                           }
                                                                       })
                                                                       .addOnFailureListener(new OnFailureListener() {
                                                                           @Override
                                                                           public void onFailure(@NonNull Exception e) {
                                                                               progressDialog.dismiss();
                                                                               Toast.makeText(requireContext(), "Item removal failed", Toast.LENGTH_SHORT).show();
                                                                               Log.i(TAG, "Error deleting document", e);
                                                                           }
                                                                       });
                                                           }


                                                       });
                                                       builder.setNegativeButton("Keep it.", (DialogInterface.OnClickListener) (dialog, which) -> {

                                                       });
                                                       AlertDialog alertDialog = builder.create();
                                                       alertDialog.show();

                                                   }
                                               } else {
                                                   Log.i(TAG, "Error getting documents.", task.getException());
                                               }
                                           }
                                       }
                );
        Log.i(TAG, " 3: => " + isAlreadyInFavorites.toString());
    }

    private void uploadDataToFireStoreInWeekPlan(MealsItem mealsItem, String weekDay) {
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Adding to week plan");
        progressDialog.setMessage("Please wait while adding the selected item to your week plan.");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();


        Map<String, Object> userWeekPlan = new HashMap<>();

        userWeekPlan.put("userID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        userWeekPlan.put("userEmail", FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userWeekPlan.put("timeAdded", System.currentTimeMillis());
        userWeekPlan.put("strMeal", mealsItem.getStrMeal());
        userWeekPlan.put("strArea", mealsItem.getStrArea());
        userWeekPlan.put("strMealThumb", mealsItem.getStrMealThumb());
        userWeekPlan.put("strYoutube", mealsItem.getStrYoutube());
        userWeekPlan.put("strInstructions", mealsItem.getStrInstructions());
        userWeekPlan.put("weekDay", weekDay);


        FirebaseFirestore.getInstance().collection("userWeekPlan")
                .add(userWeekPlan)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        rep = new RepoistryLocal(requireContext());
                        rep.insert(mealsItem, weekDay, documentReference.getId());

                        if (weekDay.toLowerCase().toLowerCase().equals(LocalDate.now().getDayOfWeek().name().toLowerCase()))
                            TodayPlannerAdapter.getInstance().mealAddedFromDailyInspirations(mealsItem);

                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        progressDialog.dismiss();
                        Toast.makeText(requireContext(), "Error while adding the item", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}

