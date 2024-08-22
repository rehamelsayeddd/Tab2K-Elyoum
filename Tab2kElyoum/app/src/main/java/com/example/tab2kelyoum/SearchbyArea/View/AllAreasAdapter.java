package com.example.tab2kelyoum.SearchbyArea.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.SearchbyArea.Model.EachAreaModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllAreasAdapter extends RecyclerView.Adapter<AllAreasAdapter.MyViewHolder> {

    private ViewGroup CountryView;
    private List<EachAreaModel> countries;
    private NetworkChecker networkChecker = NetworkChecker.getInstance();

    public AllAreasAdapter(List<EachAreaModel> countries) {
        this.countries = countries;
    }

    @NonNull
    @Override
    public AllAreasAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CountryView = parent;

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item , parent , false);
       return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllAreasAdapter.MyViewHolder holder, int position) {
       EachAreaModel eachAreaModel = countries.get(position);
       holder.country.setText(eachAreaModel.getStrArea());
       String area = eachAreaModel.getStrArea();
       if(area.equals("American")){
           holder.circleImageView.setImageResource(R.drawable.america);
       }else if (area.equals("British")){
           holder.circleImageView.setImageResource(R.drawable.unitedkingdom);
       }else if (area.equals("Canadian")){
           holder.circleImageView.setImageResource(R.drawable.canada);
       }else if (area.equals("Chinese")){
           holder.circleImageView.setImageResource(R.drawable.china);
       } else if (area.equals("Egyptian")){
           holder.circleImageView.setImageResource(R.drawable.egypt);
       }else if (area.equals("Indian")){
           holder.circleImageView.setImageResource(R.drawable.india);
       }else if (area.equals("Russian")){
           holder.circleImageView.setImageResource(R.drawable.russia);
       }else if (area.equals("Greek")) {
           holder.circleImageView.setImageResource(R.drawable.greece);
       } else if (area.equals("Italian")){
           holder.circleImageView.setImageResource(R.drawable.italy);
       } else if (area.equals("Japanese")){
           holder.circleImageView.setImageResource(R.drawable.japan);
       } else if (area.equals("French")){
           holder.circleImageView.setImageResource(R.drawable.france);
       }else if (area.equals("Polish")) {
           holder.circleImageView.setImageResource(R.drawable.croatia);
       }else if (area.equals("Turkish")){
           holder.circleImageView.setImageResource(R.drawable.turkey);
       }else if (area.equals("Portuguese")){
           holder.circleImageView.setImageResource(R.drawable.portghal);
       } else if (area.equals("Mexican")){
           holder.circleImageView.setImageResource(R.drawable.mexicoo);
       }else if (area.equals("Moroccan")){
           holder.circleImageView.setImageResource(R.drawable.morocoo);
       }else if (area.equals("Malaysian")){
           holder.circleImageView.setImageResource(R.drawable.malaysia);
       }else if (area.equals("Tunisian")){
           holder.circleImageView.setImageResource(R.drawable.tunis);
       }else if (area.equals("Dutch")){
           holder.circleImageView.setImageResource(R.drawable.dutch);
       }else if (area.equals("Jamaican")){
           holder.circleImageView.setImageResource(R.drawable.jamaica);
       }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!networkChecker.checkIfInternetIsConnected()) {
                    MainActivity.mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.mainActivity, "Turn internet on to view meals related to this area.", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    AllAreas.textInputEditText.setText("");
                    Navigation.findNavController(CountryView).navigate(AllAreasDirections.actionSearchBYCountryFragmentToMealByCountryFragment(countries.get(position).getStrArea()));

                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView country;
        CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            country = itemView.findViewById(R.id.item_country);
            circleImageView = itemView.findViewById(R.id.country_image);
        }
    }
}
