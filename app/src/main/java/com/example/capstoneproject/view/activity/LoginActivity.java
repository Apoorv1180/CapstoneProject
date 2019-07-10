package com.example.capstoneproject.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstoneproject.R;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.example.capstoneproject.viewmodel.SignInListViewModel;
import com.example.capstoneproject.viewmodel.SignInListViewModelFactory;
import com.example.capstoneproject.viewmodel.SignUpListViewModel;
import com.example.capstoneproject.viewmodel.SignUpListViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import static com.example.capstoneproject.util.Util.checkPassword;
import static com.example.capstoneproject.util.Util.checkUsername;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_CREDENTIAL = "credential";
    public static final String USER_UUID = "uuid";
    EditText etUsername;
    EditText etPassword;
    Button btSignUp;
    Button btLogin;
    String userName = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkUserLoggedInStatus();
    }

    private void checkUserLoggedInStatus() {
        final CheckUserLoggedInViewModel viewModelLoggedInStatus =
                ViewModelProviders.of(this)
                        .get(CheckUserLoggedInViewModel.class);
        observeViewModelLoggedInStatus(viewModelLoggedInStatus);
    }

    private void observeViewModelLoggedInStatus(CheckUserLoggedInViewModel viewModelLoggedInStatus) {
        viewModelLoggedInStatus.isAlreadyLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser result) {
                if (result != null) {
                    Log.e("USER", "USER ALREADY LOGGED IN" + result.getEmail().toString());
                    sendUserToWelcomeScreen(result);
                } else {
                    Log.e("USER", "USER NOT LOGGED IN");
                    initView();
                    setViewOnclickListerners();
                }
            }
        });
    }

    private void getValues() {
        userName = etUsername.getText().toString();
        password = etPassword.getText().toString();

    }

    private void setViewOnclickListerners() {
        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
                checkUsername(userName);
                checkPassword(password);

                sendCredentialsForVerification(userName, password);
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
                checkUsername(userName);
                checkPassword(password);
                loginWithGivenCredentials(userName, password);
            }
        });
    }

    private void loginWithGivenCredentials(String userName, String password) {
        final SignInListViewModel viewModelSignIn =
                ViewModelProviders.of(this, new SignInListViewModelFactory(getApplication(), userName, password))
                        .get(SignInListViewModel.class);
        observeViewModelSignIn(viewModelSignIn);
    }

    private void observeViewModelSignIn(SignInListViewModel viewModelSignIn) {
        viewModelSignIn.isLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(@Nullable FirebaseUser result) {
                if (result != null) {
                    Log.e("USER", "USER LOGGED IN" + result.getEmail().toString());
                    sendUserToWelcomeScreen(result);
                } else {
                    Log.e("USER", "USER NOT LOGGED IN");
                    initView();
                    setViewOnclickListerners();
                }
            }
        });
    }

    private void sendCredentialsForVerification(String userName, String password) {
        final SignUpListViewModel viewModel =
                ViewModelProviders.of(this, new SignUpListViewModelFactory(getApplication(), userName, password))
                        .get(SignUpListViewModel.class);
        observeViewModelSignUp(viewModel);
    }

    private void observeViewModelSignUp(SignUpListViewModel viewModel) {
        viewModel.isRegisteredStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(@Nullable FirebaseUser result) {
                if (result != null) {
                    Log.e("USER", "USER REGISTERED AND LOGGED IN" + result.getEmail().toString());
                    sendUserToWelcomeScreen(result);
                } else {
                    Log.e("USER", "USER NOT REGISTERED");
                    initView();
                    setViewOnclickListerners();
                }
            }
        });
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        btSignUp = findViewById(R.id.bt_register);
    }

    private void sendUserToWelcomeScreen(FirebaseUser user) {
        Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
        newIntent.putExtra(USER_UUID,user.getUid());
        startActivity(newIntent);
        finish();
    }
}
