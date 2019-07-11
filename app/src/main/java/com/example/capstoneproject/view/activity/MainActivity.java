package com.example.capstoneproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.capstoneproject.R;
import com.example.capstoneproject.view.fragment.MainFragment;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

import static com.example.capstoneproject.view.activity.LoginActivity.USER_CREDENTIAL;
import static com.example.capstoneproject.view.activity.LoginActivity.USER_UUID;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_FRAG = "TAG_MAIN_FRAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        openMainFragment();

    }

    private void openMainFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragment_container,new MainFragment());
        ft.commit();
    }
}
