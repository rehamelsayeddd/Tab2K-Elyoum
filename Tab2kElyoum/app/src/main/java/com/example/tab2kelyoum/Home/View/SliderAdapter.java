package com.example.tab2kelyoum.Home.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {
    private ViewGroup viewGroupOfMeal;
    private ProgressDialog progressDialog;
    private static final String TAG = "SliderAdapter";
    private Boolean isAlreadyInFavorites;
    private RepoistryLocal repoistryLocal;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();


    String[] weekDays = {"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    ArrayAdapter<String> arrayAdapter;


    private ViewPager2 viewPager2;
    List<MealsItem> meals = new ArrayList<>();


    public SliderAdapter(List<MealsItem> meals, ViewPager2 viewPager2) {

        this.meals = meals;
        this.viewPager2 = viewPager2;


    }


    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewGroupOfMeal = parent;


        return new SliderViewHolder(
                LayoutInflater.from(viewGroupOfMeal.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false)

        );

    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        //holder.imageView.setImageResource(sliderItemList.get(position).getImage());
        MealsItem mealsItem = meals.get(position);
        Glide.with(viewGroupOfMeal.getContext()).load(mealsItem.getStrMealThumb()).into(holder.imageView);
        holder.tv_mealName.setText(mealsItem.getStrMeal());


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Navigation.findNavController(viewGroupOfMeal).navigate(homepageFragmentDirections.actionHomepageFragmentToMealDetailsFragment(meals.get(position)));

            }
        });

        if (MainActivity.isLoginAsGuest == false) {




            holder.btn_addToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    NetworkChecker networkChecker = NetworkChecker.getInstance();

                    if (!networkChecker.checkIfInternetIsConnected()) {
                        MainActivity.mainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to save meals to your favorites.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (networkChecker.checkIfInternetIsConnected()) {
                        checkIfItemAlreadyExistsInFavoritesOfFirestore(meals.get(position));
                    }


                }
            });


            holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        checkIfItemAlreadyExistsInWeekPlan(meals.get(position), daySelected);


                    }

                }
            });


        } else if (MainActivity.isLoginAsGuest == true) {
            holder.btn_addToFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(viewGroupOfMeal.getContext(), "You need to log in to save meals to your favorites.", Toast.LENGTH_SHORT).show();

                }
            });

            holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int positionDay, long id) {
                    Toast.makeText(viewGroupOfMeal.getContext(), "You need to log in to save meals to your week planenr.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (position == meals.size() - 2) {
            viewPager2.post(runnable);
        }
    }


    private void checkIfItemAlreadyExistsInFavoritesOfFirestore(MealsItem mealsItemSelected) {
        isAlreadyInFavorites = false;
        mealsItemSelected.setWeekDay("NULL");

        FirebaseFirestore.getInstance().collection("userFavorites")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        checkIfMealExistsInFavorites(task.getResult(), mealsItemSelected);
                    } else {
                        Log.i(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

    private void checkIfMealExistsInFavorites(QuerySnapshot result, MealsItem mealsItemSelected) {
        String currentUserEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        for (QueryDocumentSnapshot document : result) {
            if (isMealInFavorites(document, mealsItemSelected, currentUserEmail)) {
                isAlreadyInFavorites = true;
                mealsItemSelected.documentID = document.getId();
                break;
            }
        }

        if (isAlreadyInFavorites) {
            showRemoveFavoriteDialog(mealsItemSelected);
        } else {
            uploadDataToFireStoreInFavorites(mealsItemSelected);
        }
    }

    private boolean isMealInFavorites(QueryDocumentSnapshot document, MealsItem mealsItemSelected, String currentUserEmail) {
        return document.get("strMeal").equals(mealsItemSelected.getStrMeal()) &&
                document.get("userEmail").equals(currentUserEmail);
    }

    private void showRemoveFavoriteDialog(MealsItem mealsItemSelected) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroupOfMeal.getContext());
        builder.setTitle("This item is already in your favorite meals list.")
                .setMessage("Would you like to remove it?")
                .setCancelable(true)
                .setPositiveButton("Remove it.", (dialog, which) -> handleRemoveFavorite(mealsItemSelected))
                .setNegativeButton("Keep it.", (dialog, which) -> {
                    // Do nothing, just dismiss the dialog
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void handleRemoveFavorite(MealsItem mealsItemSelected) {
        if (!networkChecker.checkIfInternetIsConnected()) {
            showToastOnMainThread("Turn internet on to be able to remove meals from your favorites.");
        } else {
            showProgressDialog("Removing favorites", "Please wait while removing the selected item from your favorite meals.");
            FirebaseFirestore.getInstance().collection("userFavorites").document(mealsItemSelected.documentID)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        showToastOnMainThread("Item removed successfully");
                        Log.i(TAG, "DocumentSnapshot successfully deleted!");

                        // Delete from local database (Room)
                        repoistryLocal = new RepoistryLocal(viewGroupOfMeal.getContext());
                        repoistryLocal.delete(mealsItemSelected);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        showToastOnMainThread("Item removal failed");
                        Log.i(TAG, "Error deleting document", e);
                    });
        }
    }

    private void showToastOnMainThread(String message) {
        MainActivity.mainActivity.runOnUiThread(() -> Toast.makeText(MainActivity.mainActivity, message, Toast.LENGTH_SHORT).show());
    }

    private void showProgressDialog(String title, String message) {
        progressDialog = new ProgressDialog(viewGroupOfMeal.getContext());
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();
    }


    private void uploadDataToFireStoreInFavorites(MealsItem mealsItem) {
        progressDialog = new ProgressDialog(viewGroupOfMeal.getContext());
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

                        //insert to local database
                        repoistryLocal = new RepoistryLocal(viewGroupOfMeal.getContext());
                        repoistryLocal.insert(mealsItem, "NULL", documentReference.getId());

                        progressDialog.dismiss();
                        Toast.makeText(viewGroupOfMeal.getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        progressDialog.dismiss();
                        Toast.makeText(viewGroupOfMeal.getContext(), "Error adding the item", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkIfItemAlreadyExistsInWeekPlan(MealsItem mealsItemSelected, String weekDay) {
        isAlreadyInFavorites = false;

        FirebaseFirestore.getInstance()
                .collection("userWeekPlan")
                .get()
                .addOnCompleteListener(task -> {
                    mealsItemSelected.setWeekDay(weekDay);

                    if (task.isSuccessful()) {
                        checkIfMealAlreadyExists(task.getResult(), mealsItemSelected, weekDay);
                    } else {
                        Log.i(TAG, "Error getting documents.", task.getException());
                    }
                });

        Log.i(TAG, " 3: => " + isAlreadyInFavorites.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkIfMealAlreadyExists(QuerySnapshot result, MealsItem mealsItemSelected, String weekDay) {
        for (QueryDocumentSnapshot document : result) {
            if (documentMatchesMealAndDay(document, mealsItemSelected, weekDay)) {
                isAlreadyInFavorites = true;
                mealsItemSelected.documentID = document.getId();
                break;
            }
        }

        if (!isAlreadyInFavorites) {
            uploadDataToFireStoreInWeekPlan(mealsItemSelected, weekDay);
        } else {
            showRemoveMealAlertDialog(mealsItemSelected, weekDay);
        }
    }

    private boolean documentMatchesMealAndDay(QueryDocumentSnapshot document, MealsItem mealsItemSelected, String weekDay) {
        return document.get("strMeal").equals(mealsItemSelected.getStrMeal()) &&
                document.get("userEmail").equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()) &&
                document.get("weekDay").equals(weekDay);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showRemoveMealAlertDialog(MealsItem mealsItemSelected, String weekDay) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroupOfMeal.getContext());
        builder.setTitle("This item is already in your week plan on this day.")
                .setMessage("Would you like to remove it?")
                .setCancelable(true)
                .setPositiveButton("Remove it.", (dialog, which) -> handleRemoveMealFromWeekPlan(mealsItemSelected, weekDay))
                .setNegativeButton("Keep it.", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleRemoveMealFromWeekPlan(MealsItem mealsItemSelected, String weekDay) {
        if (!networkChecker.checkIfInternetIsConnected()) {
            MainActivity.mainActivity.runOnUiThread(() ->
                    Toast.makeText(MainActivity.mainActivity, "Turn internet on to remove meals from your week plan.", Toast.LENGTH_SHORT).show()
            );
        } else {
            showProgressDialog("Removing favorites", "Please wait while removing the selected item from your favorite meals.");

            FirebaseFirestore.getInstance()
                    .collection("userWeekPlan")
                    .document(mealsItemSelected.documentID)
                    .delete()
                    .addOnSuccessListener(aVoid -> handleSuccessfulRemoval(mealsItemSelected, weekDay))
                    .addOnFailureListener(e -> handleFailedRemoval(e));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handleSuccessfulRemoval(MealsItem mealsItemSelected, String weekDay) {
        dismissProgressDialog();
        Toast.makeText(viewGroupOfMeal.getContext(), "Item removed successfully", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "DocumentSnapshot successfully deleted!");
         //deleted from local databse (room)
        repoistryLocal = new RepoistryLocal(viewGroupOfMeal.getContext());
        repoistryLocal.delete(mealsItemSelected);

        if (weekDay.equalsIgnoreCase(LocalDate.now().getDayOfWeek().name())) {
            TodayPlannerAdapter.getInstance().mealRemovedFromDailyInspirations(mealsItemSelected);
        }
    }

    private void handleFailedRemoval(@NonNull Exception e) {
        dismissProgressDialog();
        Toast.makeText(viewGroupOfMeal.getContext(), "Item removal failed", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "Error deleting document", e);
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    private void uploadDataToFireStoreInWeekPlan(MealsItem mealsItem, String weekDay) {
        progressDialog = new ProgressDialog(viewGroupOfMeal.getContext());
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

                        repoistryLocal = new RepoistryLocal(viewGroupOfMeal.getContext());
                        repoistryLocal.insert(mealsItem, weekDay, documentReference.getId());

                        if (weekDay.toLowerCase().toLowerCase().equals(LocalDate.now().getDayOfWeek().name().toLowerCase()))
                            TodayPlannerAdapter.getInstance().mealAddedFromDailyInspirations(mealsItem);

                        progressDialog.dismiss();
                        Toast.makeText(viewGroupOfMeal.getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        progressDialog.dismiss();
                        Toast.makeText(viewGroupOfMeal.getContext(), "Error while adding the item", Toast.LENGTH_SHORT).show();

                    }
                });


    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;
        private TextView tv_mealName;
        private ImageButton btn_addToFavorites;
        private AutoCompleteTextView autoCompleteTextView;
        private TextInputLayout textInputLayout;

        SliderViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            tv_mealName = itemView.findViewById(R.id.tv_mealName);
            btn_addToFavorites = itemView.findViewById(R.id.btn_addToFavorites);

            autoCompleteTextView = itemView.findViewById(R.id.auto_complete_textview);
            textInputLayout = itemView.findViewById(R.id.text_input_layout);
            arrayAdapter = new ArrayAdapter<String>(viewGroupOfMeal.getContext(), R.layout.list_weekdays, weekDays);
            autoCompleteTextView.setAdapter(arrayAdapter);

        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            meals.addAll(meals);
            notifyDataSetChanged();
        }
    };
}

