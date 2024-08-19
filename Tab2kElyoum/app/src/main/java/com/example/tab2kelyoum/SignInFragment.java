package com.example.tab2kelyoum;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.material.textfield.TextInputEditText;

public class SignInFragment extends Fragment {

 private TextInputEditText et_email;
 private TextInputEditText et_password;
 private Button btnlogin;
 private SignInButton googleSignIn;
 private TextView register;
 private Button btnloginAsGuest;
 private FirebaseAuth firebaseAuth;
 private ProgressDialog loadingBar;
 private NetworkChecker networkChecker;
 private GoogleSignInClient googleSignInClient;
 private View view;


 private static final String TAG = "SignInFragment";
 private static final int RC_SIGN_IN = 9001;

 @Override
 public void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);

  // Initialize FirebaseAuth instance
  firebaseAuth = FirebaseAuth.getInstance();

  // Initialize NetworkChecker with the context
  networkChecker = NetworkChecker.getInstance();

  // Configure Google Sign-In
  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
          .requestIdToken(getString(R.string.client_id)) // Replace with your Web client ID
          .requestEmail()
          .build();
  googleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
 }

 @Override
 public View onCreateView(LayoutInflater inflater, ViewGroup container,
                          Bundle savedInstanceState) {
  // Inflate the layout for this fragment
  return inflater.inflate(R.layout.fragment_sign_in, container, false);
 }

 @Override
 public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
  super.onViewCreated(view, savedInstanceState);

  this.view = view;
  // Set up ProgressDialog for loading indication
  loadingBar = new ProgressDialog(requireContext());
  loadingBar.setTitle("Signing in");
  loadingBar.setMessage("Please wait while signing in");
  loadingBar.setCanceledOnTouchOutside(false);

  // Initialize UI components
  btnlogin = view.findViewById(R.id.btnlogin);
  et_email = view.findViewById(R.id.et_email);
  et_password = view.findViewById(R.id.et_password);
  googleSignIn = view.findViewById(R.id.btn_signInWithGoogle);
  register = view.findViewById(R.id.btnregister);
  btnloginAsGuest = view.findViewById(R.id.btn_loginasguest);

  // Set up click listeners for buttons
  register.setOnClickListener(v ->
          NavHostFragment.findNavController(SignInFragment.this)
                  .navigate(R.id.action_signInFragment_to_registerFragment)
  );

  btnlogin.setOnClickListener(v -> signInWithEmailAndPassword());

  googleSignIn.setOnClickListener(v -> signInWithGoogle());

  btnloginAsGuest.setOnClickListener(v -> promptGuestLogin());
 }

 // Method to sign in with email and password
 private void signInWithEmailAndPassword() {
  String email = et_email.getText().toString();
  String password = et_password.getText().toString();

  // Check internet connection before proceeding
  if (!networkChecker.checkIfInternetIsConnected()) {
   Toast.makeText(requireContext(), "Turn internet on to be able to sign in.", Toast.LENGTH_SHORT).show();
  } else if (email.isEmpty() || password.isEmpty()) {
   // Handle empty email or password fields
   if (email.isEmpty()) {
    Toast.makeText(requireContext(), "Enter your email", Toast.LENGTH_SHORT).show();
   } else if (password.isEmpty()) {
    Toast.makeText(requireContext(), "Enter your password", Toast.LENGTH_SHORT).show();
   }
  } else {
   // Show loading bar while signing in
   loadingBar.show();
   firebaseAuth.signInWithEmailAndPassword(email, password)
           .addOnCompleteListener(task -> {
            loadingBar.dismiss();
            if (task.isSuccessful()) {
             // Navigate to homepage upon successful login
             NavController navController = NavHostFragment.findNavController(SignInFragment.this);
             navController.navigate(R.id.action_signInFragment_to_homepageFragment);
            } else {
             // Handle sign-in errors
             handleSignInError(task.getException());
            }
           });
  }
 }

 // Method to handle Google Sign-In
 private void signInWithGoogle() {
  if (!networkChecker.checkIfInternetIsConnected()) {
   Toast.makeText(requireContext(), "Turn internet on to be able to sign in.", Toast.LENGTH_SHORT).show();
   return;
  }

  // Start the sign-in intent
  Intent signInIntent = googleSignInClient.getSignInIntent();
  startActivityForResult(signInIntent, RC_SIGN_IN);
 }

 @Override
 public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
  super.onActivityResult(requestCode, resultCode, data);

  if (requestCode == RC_SIGN_IN) {
   Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
   try {
    GoogleSignInAccount account = task.getResult(ApiException.class);
    if (account != null) {
     firebaseAuthWithGoogle(account);
    }
   } catch (ApiException e) {
    // Handle sign-in error
    Toast.makeText(requireContext(), "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
   }
  }
 }

 // Method to authenticate with Firebase using Google account
 private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
  AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
  firebaseAuth.signInWithCredential(credential)
          .addOnCompleteListener(requireActivity(), task -> {
           if (task.isSuccessful()) {
            // Sign-in successful
            Toast.makeText(requireContext(), "Google Sign-In successful", Toast.LENGTH_SHORT).show();
            NavController navController = NavHostFragment.findNavController(SignInFragment.this);
            navController.navigate(R.id.action_signInFragment_to_homepageFragment);
           } else {
            // Handle sign-in error
            handleSignInError(task.getException());
           }
          });
 }

 // Method to prompt the user before logging in as a guest
 private void promptGuestLogin() {
  if (!networkChecker.checkIfInternetIsConnected()) {
   Toast.makeText(requireContext(), "Turn internet on to be able to skip.", Toast.LENGTH_SHORT).show();
  } else {
   new AlertDialog.Builder(requireContext())
           .setTitle("Wait! Are You Sure You Want To Skip?")
           .setMessage("You'll miss out on personalized content and saving our delicious recipes")
           .setPositiveButton("YES, I'M SURE", (dialog, which) -> {
            Toast.makeText(requireContext(), "Login as guest was successful.", Toast.LENGTH_SHORT).show();
            NavController navController = NavHostFragment.findNavController(SignInFragment.this);
            navController.navigate(R.id.action_signInFragment_to_homepageFragment);
           })
           .setNegativeButton("NO, Go BACK", (dialog, which) -> dialog.cancel())
           .show();
  }
 }

 // Method to handle errors during sign-in
 private void handleSignInError(Exception exception) {
  if (exception == null) {
   Toast.makeText(requireContext(), "Unexpected error occurred", Toast.LENGTH_SHORT).show();
   return;
  }

  if (exception instanceof FirebaseAuthException) {
   String errorCode = ((FirebaseAuthException) exception).getErrorCode();
   if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
    Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
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
