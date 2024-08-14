package com.example.tab2kelyoum;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseNetworkException;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment {

    private TextInputEditText et_email, et_password;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        et_email = view.findViewById(R.id.et_email);
        et_password = view.findViewById(R.id.et_password);
        loadingBar = new ProgressDialog(requireContext());

        // Set up the register button click listener
        view.findViewById(R.id.btnregister1).setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        // Validate email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(requireContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(requireContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length() < 6) {
            Toast.makeText(requireContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show a loading bar while the registration process is ongoing
        loadingBar.setTitle("Creating Account");
        loadingBar.setMessage("Please wait, we are creating your account...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // Register the user with Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            // Registration successful
                            Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            // Navigate to the next fragment or activity
                            Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_signInFragment);
                        } else {
                            // Handle errors during registration
                            handleRegistrationError(task.getException());
                        }
                    }
                });
    }

    private void handleRegistrationError(Exception exception) {
        if (exception == null) {
            Toast.makeText(requireContext(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
            return;
        }

        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                Toast.makeText(requireContext(), "This email is already in use", Toast.LENGTH_SHORT).show();
            } else if (errorCode.equals("ERROR_INVALID_EMAIL")) {
                Toast.makeText(requireContext(), "Invalid email format", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else if (exception instanceof FirebaseNetworkException) {
            Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
