package com.example.tab2kelyoum.Register.View;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tab2kelyoum.MainActivity.View.MainActivity;
import com.example.tab2kelyoum.NetworkChecker;
import com.example.tab2kelyoum.R;
import com.example.tab2kelyoum.Register.Presenter.InterfaceRegister;
import com.example.tab2kelyoum.Register.Presenter.RegisterPresenter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.FirebaseNetworkException;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterFragment extends Fragment implements InterfaceRegister {

    private TextInputEditText signUp_email, signUp_password, confirmPassword;
    private Button register;
    private View view;
    private NetworkChecker networkChecker;
    private ProgressDialog loadingBar;
    private RegisterPresenter registerPresenter;
    private String email;
    private static final String TAG = "Register";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        this.view = view;

        signUp_email = view.findViewById(R.id.et_email);
        signUp_password = view.findViewById(R.id.et_password);
        register = view.findViewById(R.id.btnregister1);
        confirmPassword = view.findViewById(R.id.signUp_Confirm_password_edt);
        loadingBar = new ProgressDialog(requireContext());
        networkChecker = NetworkChecker.getInstance();
        registerPresenter = new RegisterPresenter(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = signUp_email.getText().toString();
                String password = signUp_password.getText().toString();
                String confirm = confirmPassword.getText().toString();

                if (!networkChecker.checkIfInternetIsConnected()) {
                    Toast.makeText(MainActivity.mainActivity, "Turn internet on to be able to register.", Toast.LENGTH_SHORT).show();

                } else if (networkChecker.checkIfInternetIsConnected()) {
                    loadingBar.setTitle("Registering");
                    loadingBar.setMessage("Please wait while registering");
                    loadingBar.setCanceledOnTouchOutside(false);

                    if ((!email.isEmpty()) && (!password.isEmpty()) && (password.equals(confirm))) {


                        loadingBar.show();
                        registerPresenter.createUserWithEmailAndPassword(email, password);


                    } else {
                        if (email.isEmpty()) {
                            Toast.makeText(getContext(), "Enter your email", Toast.LENGTH_SHORT).show();
                        } else if (password.isEmpty()) {
                            Toast.makeText(getContext(), "Enter your password", Toast.LENGTH_SHORT).show();
                        } else if (!password.equals(confirm)) {
                            Toast.makeText(getContext(), "Password not identical", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
            }
        });
    }

    @Override
    public void onCompleteRegisterWithEmailAndPassword(Task<AuthResult> task) {
        loadingBar.dismiss();

        if (task.isSuccessful()) {


            Toast.makeText(requireContext(), "Registration was successful", Toast.LENGTH_SHORT).show();

            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_homepageFragment);


        } else {
            Exception exception = task.getException();
            if (exception == null) {
                Toast.makeText(getContext(), "UnExpected error occurred", Toast.LENGTH_SHORT).show();
            } else if (exception.getClass().equals(FirebaseNetworkException.class)) {
                Toast.makeText(getContext(), "Network error", Toast.LENGTH_SHORT).show();

            } else {
                if (((FirebaseAuthException) exception).getErrorCode().equals("ERROR_WEAK_PASSWORD")) {

                    Toast.makeText(getContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}




