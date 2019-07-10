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
import com.example.capstoneproject.viewmodel.SignUpListViewModel;
import com.example.capstoneproject.viewmodel.SignUpListViewModelFactory;

import static com.example.capstoneproject.util.Util.checkPassword;
import static com.example.capstoneproject.util.Util.checkUsername;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    EditText etPassword;
    Button btSignUp;
    Button btLogin;

    String userName="";
    String password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setViewOnclickListerners();


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

                sendCredentialsForVerification(userName,password);
            }
        });
    }

    private void sendCredentialsForVerification(String userName, String password) {
        final SignUpListViewModel viewModel =
                ViewModelProviders.of(this, new SignUpListViewModelFactory(getApplication(), userName,password))
                        .get(SignUpListViewModel.class);
        observeViewModel(viewModel);
    }

    private void observeViewModel(SignUpListViewModel viewModel) {
        viewModel.isRegisteredStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean result) {
                if(result!=null){
                    if(result)
                    Log.e("CREATE_USER","USER CREATED");
                    else
                        Log.e("CREATE_USER","USER NOT CREATED");
                }

            }
        });
    }

    private void initView() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btLogin=findViewById(R.id.bt_login);
        btSignUp=findViewById(R.id.bt_register);
    }
}
