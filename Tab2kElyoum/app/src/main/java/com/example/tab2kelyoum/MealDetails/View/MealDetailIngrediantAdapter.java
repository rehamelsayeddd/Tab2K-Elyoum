package com.example.tab2kelyoum.MealDetails.View;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tab2kelyoum.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MealDetailIngrediantAdapter extends RecyclerView.Adapter<MealDetailIngrediantAdapter.MyViewHolder> {
    private ViewGroup CountryView;
    private List<String> mealsItems = new ArrayList<>();
    private List<String> measure = new ArrayList<>();

    public MealDetailIngrediantAdapter(List<String> mealsItems, List<String> measure) {
        this.mealsItems = mealsItems;
        this.measure = measure;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryView = parent;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingrediant_item, parent, false);
        return new MealDetailIngrediantAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ingrediant.setText(mealsItems.get(position).toString());
        holder.measure.setText(measure.get(position).toString());
        Glide.with(CountryView).load(String.format("https://www.themealdb.com/images/ingredients/%s-Small.png", mealsItems.get(position)))
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {

        return mealsItems.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ingrediant, measure;
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ingrediant = itemView.findViewById(R.id.ingrediant_text);
            imageView = itemView.findViewById(R.id.ingrediant_image);
            measure = itemView.findViewById(R.id.measure_text);
        }
    }
}
