package com.example.tab2kelyoum.SplashScreen;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.tab2kelyoum.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenFragment extends Fragment {
    private LottieAnimationView gifImageView;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private Handler handler;
    private Runnable handlerRunnable;
    private static final int timer = 4000; //4seconds to check user logged in or not



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        gifImageView = view.findViewById(R.id.gif_food);

        handler = new Handler();
        handlerRunnable = new Runnable() {
            @Override
            public void run() {
                //SharedPreferences sharedPref = requireContext().getSharedPreferences("setting", Context.MODE_PRIVATE);

                FirebaseUser user = firebaseAuth.getCurrentUser();
                Log.i("Splashscreen", "run: " + firebaseAuth.getCurrentUser());
               // boolean isFirst = sharedPref.getBoolean("first_look", false);

                if (user == null) {
                    Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_signInFragment);

                } else {
                    Navigation.findNavController(view).navigate(R.id.action_splashScreenFragment_to_homepageFragment);

                }
            }
        };

        handler.postDelayed(handlerRunnable, timer);

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(handlerRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(handlerRunnable, timer);
    }
}