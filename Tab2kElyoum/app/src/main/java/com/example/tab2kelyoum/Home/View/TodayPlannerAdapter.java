package com.example.tab2kelyoum.Home.View;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.Model.MealsItem;
import com.example.tab2kelyoum.Connection.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.Repoistry.RepoistryLocal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class TodayPlannerAdapter extends RecyclerView.Adapter<TodayPlannerAdapter.ViewHolder> {

private ViewGroup viewGroup;
private static final String Tag = "TodayPlannerAdapter";
private ProgressDialog progressdialog;
private RepoistryLocal repoistryLocal;
public static TodayPlannerAdapter InstanceProvidingMeals;
private List<MealsItem> mealsWeekPlanner = new ArrayList<>();


private TodayPlannerAdapter (List<MealsItem> mealsWeekPlanner){
    this.mealsWeekPlanner =mealsWeekPlanner;
}

public static TodayPlannerAdapter getInstanceMeals (List<MealsItem> mealsWeekPlanner){
    if (InstanceProvidingMeals == null) {
        InstanceProvidingMeals = new TodayPlannerAdapter(mealsWeekPlanner);
    }
    return InstanceProvidingMeals;
}
public static TodayPlannerAdapter getInstance(){
    return InstanceProvidingMeals;
}

    @NonNull
    @Override
    public TodayPlannerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
     this.viewGroup=parent;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_week_planner,parent,false);
        TodayPlannerAdapter.ViewHolder viewHolder = new TodayPlannerAdapter.ViewHolder(itemView);
        Log.i(Tag ,"OnCreateViewHolder :");

        progressdialog =new ProgressDialog(viewGroup.getContext());
        progressdialog.setTitle("Removing meal from planner");
        progressdialog.setMessage("Please wait unitl removing item is done");
        progressdialog.setCanceledOnTouchOutside(true);

        return viewHolder;


    }

    @Override
    public void onBindViewHolder(@NonNull TodayPlannerAdapter.ViewHolder holder, int position) {
        MealsItem mealsItem = mealsWeekPlanner.get(position);
        holder.week_plannerMealName.setText(mealsItem.getStrMeal());
        holder.week_plannerMealArea.setText(mealsItem.getStrArea());

        Glide.with(viewGroup.getContext()).load(mealsItem.getStrMealThumb()).into(holder.week_planner_imgMealImg);

        NetworkChecker networkChecker = NetworkChecker.getInstance();



        holder.removeWeekPlannerItembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.mainActivity, "Turn internet on to remove meals from your week planner.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {


                    progressdialog.show();


                    FirebaseFirestore.getInstance().collection("userWeekPlan").document(mealsItem.documentID)
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.i(Tag, "DocumentSnapshot successfully deleted!");


                                    repoistryLocal = new RepoistryLocal(viewGroup.getContext());
                                    repoistryLocal.delete(mealsItem);

                                    mealsWeekPlanner.remove(position);
                                    notifyDataSetChanged();

                                    progressdialog.dismiss();


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i(Tag, "Error deleting document", e);
                                }
                            });
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


           //Navigation.findNavController(viewGroup).navigate(homepageFragmentDirections.actionHomepageFragmentToMealDetailsFragment(mealsWeekPlanner.get(position)));
                Navigation.findNavController(viewGroup)
                        .navigate(R.id.action_homepageFragment_to_mealDetailsFragment);

            }
        });

    }

    @Override
    public int getItemCount() {
       return mealsWeekPlanner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView week_plannerMealName;
        public TextView week_plannerMealArea;
        public ImageView week_planner_imgMealImg;
        public ImageButton removeWeekPlannerItembtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            week_plannerMealName = itemView.findViewById(R.id.week_planner_tv_mealName);
            week_plannerMealArea = itemView.findViewById(R.id.week_planner_tv_mealArea);
            week_planner_imgMealImg = itemView.findViewById(R.id.week_planner_img_mealImg);
            removeWeekPlannerItembtn = itemView.findViewById(R.id.btn_removeWeekPlannerItem);


        }
    }

    public void mealAddedFromDailyInspirations(MealsItem meal) {
        mealsWeekPlanner.add(meal);
        notifyDataSetChanged();
    }

    public void mealRemovedFromDailyInspirations(MealsItem meal) {
        for (int i = 0; i < mealsWeekPlanner.size(); i++) {
            if (meal.getStrMeal().equals(mealsWeekPlanner.get(i).getStrMeal()))
                mealsWeekPlanner.remove(i);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        int size = mealsWeekPlanner.size();
        mealsWeekPlanner.clear();
        notifyItemRangeRemoved(0, size);
    }
}
