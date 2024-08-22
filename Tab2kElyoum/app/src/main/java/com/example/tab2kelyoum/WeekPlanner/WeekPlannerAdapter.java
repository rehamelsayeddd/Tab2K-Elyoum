package com.example.tab2kelyoum.WeekPlanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tab2kelyoum.Home.View.TodayPlannerAdapter;
import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeekPlannerAdapter extends RecyclerView.Adapter<WeekPlannerAdapter.ViewHolder> {

    private Context context;
    private ViewGroup viewGroup;
    private List<MealsItem> mealsWeekPlanner = new ArrayList<>();
    private static final String TAG = "WeekPlannerAdapter";
    private Boolean firstTimeInTheView = true;
    private RepoistryLocal rep;
    private ProgressDialog progressDialog;


    public WeekPlannerAdapter(List<MealsItem> mealsWeekPlanner) {
        this.mealsWeekPlanner = mealsWeekPlanner;
    }

    @NonNull
    @Override
    public WeekPlannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        this.viewGroup = parent;
        context = parent.getContext();

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_week_planner, parent, false);          //\\\\\\\\\\
        WeekPlannerAdapter.ViewHolder viewHolder = new WeekPlannerAdapter.ViewHolder(itemView);
        Log.i(TAG, "onCreateViewHolder: ");

        progressDialog = new ProgressDialog(viewGroup.getContext());
        progressDialog.setTitle("Removing meal from week plan");
        progressDialog.setMessage("Please wait while removing the selected item from your week plan.");
        progressDialog.setCanceledOnTouchOutside(true);


        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull WeekPlannerAdapter.ViewHolder holder, int position) {

        MealsItem mealsItem = mealsWeekPlanner.get(position);
        holder.week_planner_tv_mealName.setText(mealsItem.getStrMeal());
        holder.week_planner_tv_mealArea.setText(mealsItem.getStrArea());

        Glide.with(viewGroup.getContext()).load(mealsItem.getStrMealThumb()).into(holder.week_planner_img_mealImg);

        NetworkChecker networkChecker = NetworkChecker.getInstance();

        holder.btn_removeWeekPlannerItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to remove meals from your week plan.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {


                    progressDialog.show();


                    FirebaseFirestore.getInstance().collection("userWeekPlan").document(mealsItem.documentID)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @RequiresApi(api = Build.VERSION_CODES.O)
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(TAG, "DocumentSnapshot successfully deleted!");
                                    //(FavoriteMealsAdapter.this).notifyDataSetChanged();

                                    rep = new RepoistryLocal(context);
                                    rep.delete(mealsItem);

                                    if (mealsItem.getWeekDay().toLowerCase().equals(LocalDate.now().getDayOfWeek().name().toLowerCase()))
                                        TodayPlannerAdapter.getInstance().mealRemovedFromDailyInspirations(mealsItem);


                                    mealsWeekPlanner.remove(position);
                                    notifyDataSetChanged();


                                    progressDialog.dismiss();


                                    if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Saturday")) {
                                        WeekPlanner.tv_Saturday.setVisibility(View.GONE);
                                    } else if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Sunday")) {
                                        WeekPlanner.tv_Sunday.setVisibility(View.GONE);
                                    } else if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Monday")) {
                                        WeekPlanner.tv_Monday.setVisibility(View.GONE);
                                    } else if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Tuesday")) {
                                        WeekPlanner.tv_Tuesday.setVisibility(View.GONE);
                                    } else if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Wednesday")) {
                                        WeekPlanner.tv_Wednesday.setVisibility(View.GONE);
                                    } else if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Thursday")) {
                                        WeekPlanner.tv_Thursday.setVisibility(View.GONE);
                                    } else if (mealsWeekPlanner.size() == 0 & mealsItem.getWeekDay().equals("Friday")) {
                                        WeekPlanner.tv_Friday.setVisibility(View.GONE);
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(TAG, "Error deleting document", e);
                                }
                            });
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                Navigation.findNavController(viewGroup).navigate(WeekPlannerDirections.actionNavWeekPlannersToMealDetailsFragment(mealsWeekPlanner.get(position)));
                //mealsWeekPlanner.clear(); //cause clicked back multiplied whats shown in the view.

            }
        });


    }

    @Override
    public int getItemCount() {


        return mealsWeekPlanner.size();


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView week_planner_tv_mealName;
        public TextView week_planner_tv_mealArea;
        public ImageView week_planner_img_mealImg;
        public ImageButton btn_removeWeekPlannerItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            week_planner_tv_mealName = itemView.findViewById(R.id.week_planner_tv_mealName);
            week_planner_tv_mealArea = itemView.findViewById(R.id.week_planner_tv_mealArea);
            week_planner_img_mealImg = itemView.findViewById(R.id.week_planner_img_mealImg);
            btn_removeWeekPlannerItem = itemView.findViewById(R.id.btn_removeWeekPlannerItem);
        }
    }


}

