package com.example.capstoneproject.view.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.capstoneproject.R;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.example.capstoneproject.viewmodel.SaveUserViewModel;
import com.example.capstoneproject.viewmodel.SaveUserViewModelFactory;
import com.example.capstoneproject.viewmodel.SignInListViewModel;
import com.example.capstoneproject.viewmodel.SignInListViewModelFactory;
import com.example.capstoneproject.viewmodel.SignUpListViewModel;
import com.example.capstoneproject.viewmodel.SignUpListViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import static com.example.capstoneproject.util.Util.isValidPassword;
import static com.example.capstoneproject.util.Util.isValidUsername;
import static com.example.capstoneproject.util.Util.isEmptyText;
import static com.example.capstoneproject.util.Util.setRole;

public class LoginActivity extends AppCompatActivity {

    public static final String USER_CREDENTIAL = "credential";
    public static final String USER_UUID = "uuid";
    EditText etUsername;
    EditText etPassword;
    CardView cardView;
    Button btSignUp;
    Button btLogin;
    String userName = "";
    String password = "";
    Toolbar mToolbar;

    ProgressDialog progressBar, progressDialog;

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
                if (userName.equalsIgnoreCase(getResources().getString(R.string.admin_email)) && password.equalsIgnoreCase(getResources().getString(R.string.admin_password))) {
                    btSignUp.setEnabled(false);
                } else {

                    progressBar = ProgressDialog.show(LoginActivity.this, getString(R.string.loading),
                            "Please Wait" == null ? getString(R.string.wait) : "Please Wait", true, false);
                    btSignUp.setEnabled(true);
                    getValues();
                    validateSignup();
                }
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = ProgressDialog.show(LoginActivity.this, getString(R.string.loading),
                        "Please Wait" == null ? getString(R.string.wait) : "Please Wait", true, false);

                getValues();
                validateLogin();
            }
        });
    }

    private void doLogin() {
        if (userName.equalsIgnoreCase(getResources().getString(R.string.admin_email)) && password.equalsIgnoreCase(getResources().getString(R.string.admin_password))) {
            setRole(LoginActivity.this, getResources().getString(R.string.role_admin));
            sendCredentialsForVerification(userName, password);
        } else {
            setRole(LoginActivity.this, "");
            getValues();
            loginWithGivenCredentials(userName, password);
        }
    }

    private void validateLogin() {
        if (isEmptyText(userName)) {
            etUsername.setError(getString(R.string.username_empty));
        } else if (isEmptyText(password)) {
            etPassword.setError(getString(R.string.password_empty));
        } else {
            doLogin();
        }
    }

    private void validateSignup() {
        if (isEmptyText(userName)) {
            etUsername.setError(getString(R.string.username_empty));
        } else if (isEmptyText(password)) {
            etPassword.setError(getString(R.string.password_empty));
        } else if (!isValidUsername(userName)) {
            Util.displaySnackBar(btSignUp, getString(R.string.invalid_username));
        } else if (!isValidPassword(password)) {
            Util.displaySnackBar(btSignUp, getString(R.string.invalid_password));
        } else if ((isValidUsername(userName)) && (isValidPassword(password))) {
            sendCredentialsForVerification(userName, password);
        } else {
            Util.displaySnackBar(btSignUp, getString(R.string.invalid_username_password));
        }

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
                    Util.displaySnackBar(btLogin, getString(R.string.loggedIn));
                    Log.e(getResources().getString(R.string.key_user), getResources().getString(R.string.loggedIn) + result.getEmail().toString());
                    fetchInformationInProfileDialog(result);
                } else {
                    Log.e("USER", "USER NOT LOGGED IN");
                    Util.displaySnackBar(btLogin, getString(R.string.Not_logged_in));
                    Log.e(getResources().getString(R.string.key_user), getResources().getString(R.string.Not_logged_in));
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
                    Util.displaySnackBar(btSignUp, getString(R.string.loggedIn));
                    Log.e(getResources().getString(R.string.key_user), getResources().getString(R.string.loggedIn) + result.getEmail().toString());
                    Log.e("USER", "USER REGISTERED AND LOGGED IN" + result.getEmail().toString());
                    fetchInformationInProfileDialog(result);
                } else {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog.cancel();
                    }
                    if (progressBar != null) {
                        progressBar.dismiss();
                        progressBar.cancel();
                    }
                    Util.displaySnackBar(btSignUp, getString(R.string.Not_logged_in));
                    Log.e(getResources().getString(R.string.key_user), getResources().getString(R.string.Not_logged_in));
                    Log.e("USER", "USER NOT REGISTERED");
                }
            }

        });
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btLogin = findViewById(R.id.bt_login);
        btSignUp = findViewById(R.id.bt_register);
        cardView = findViewById(R.id.login_card);
    }

    private void sendUserToWelcomeScreen(FirebaseUser user) {
        Intent newIntent = new Intent(LoginActivity.this, MainActivity.class);
        newIntent.putExtra(USER_UUID, user.getUid());
        newIntent.putExtra(USER_CREDENTIAL, user.getEmail());
        startActivity(newIntent);
        finish();
    }

    private void fetchInformationInProfileDialog(final FirebaseUser result) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog.cancel();
        }
        if (progressBar != null) {
            progressBar.cancel();
            progressBar.dismiss();
        }
        // get prompts.xml view
        cardView.setVisibility(View.GONE);
        final EditText userName, userPhoneNumber;

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.custom_profile_input_dialog_box, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        userName = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInputName);
        userPhoneNumber = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserPhoneNumber);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                            }

                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                Button btn_ok = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btn_ok.setTextColor(getResources().getColor(R.color.white));

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Util.isEmptyText(userName.getText().toString().trim())) {
                            userName.setError(getString(R.string.name_empty));
                        } else if (Util.isEmptyText(userPhoneNumber.getText().toString().trim())) {
                            userPhoneNumber.setError(getString(R.string.phone_no_empty));
                        } else if (!Util.isValidPhoneNumber(userPhoneNumber.getText().toString().trim())) {
                            Util.displaySnackBar(userName, getString(R.string.invalid_phone_no));
                        } else {
                            saveUserValues(result.getUid(), userName.getText().toString().trim(), userPhoneNumber.getText().toString().trim(), result, alertDialog);
                        }
                    }
                });
            }
        });


        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.cornered_card_layout));
        // show it

        alertDialog.show();
    }

    private void saveUserValues(String userId, String Uname, String Password, FirebaseUser result, DialogInterface dialog) {
        final SaveUserViewModel viewModelSignIn =
                ViewModelProviders.of(this, new SaveUserViewModelFactory(this.getApplication(), userId, Uname, Password))
                        .get(SaveUserViewModel.class);
        boolean status = observeViewModelSaveUserStatus(viewModelSignIn, dialog);
        if (status) {
            sendUserToWelcomeScreen(result);
        }
    }

    private boolean observeViewModelSaveUserStatus(SaveUserViewModel viewModelSaveUserStatus, final DialogInterface dialog) {
        viewModelSaveUserStatus.isSavedStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if (result) {
                    Log.e("USER", "SAVED SUCCESSFULLY");
                    dialog.cancel();
                    dialog.dismiss();
                } else
                    Log.e(getResources().getString(R.string.key_user), getResources().getString(R.string.not_saved_successful_msg));
                dialog.cancel();
                dialog.dismiss();
            }
        });

        return true;
    }
}
