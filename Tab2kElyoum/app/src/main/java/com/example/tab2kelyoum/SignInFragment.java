package com.example.tab2kelyoum;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignInFragment extends Fragment {
    private TextInputEditText et_email;
    private TextInputEditText et_password;

    private SignInButton googleSignIn;
    private TextView register;
    private Button btnloginAsGuest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        // Initialize the views
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        googleSignIn = view.findViewById(R.id.btn_signInWithGoogle);
        register = view.findViewById(R.id.buttonSignIn);
        btnloginAsGuest = view.findViewById(R.id.btn_loginasguest);

        // Set the click listener for the register button
        register.setOnClickListener(v ->
                NavHostFragment.findNavController(SignInFragment.this)
                        .navigate(R.id.action_signInFragment_to_registerFragment)
        );

        return view;
    }
}
