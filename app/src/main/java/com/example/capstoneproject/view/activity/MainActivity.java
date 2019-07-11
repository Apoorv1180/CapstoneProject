package com.example.capstoneproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;
import com.example.capstoneproject.view.fragment.AdminDashboardFragment;
import com.example.capstoneproject.view.fragment.ArticleFragment;
import com.example.capstoneproject.view.fragment.MainFragment;

import static com.example.capstoneproject.util.Util.getRole;
import static com.example.capstoneproject.view.activity.LoginActivity.USER_CREDENTIAL;

public class MainActivity extends AppCompatActivity implements MainFragment.SendMessages {

    private static final String MAIN_FRAG = "TAG_MAIN_FRAG";
    private static final String ARTICLE_FRAG = "TAG_ARTICLE_FRAG";
    String credential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (getIntent() != null) {
            credential = getIntent().getStringExtra(USER_CREDENTIAL);
        }
        openMainFragment();

    }

    private void openMainFragment() {
        if(credential.equalsIgnoreCase("ekta@gmail.com")) {
           // if (getRole(MainActivity.this).equalsIgnoreCase("Admin")) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.add(R.id.fragment_container, new AdminDashboardFragment());
                ft.commit();
        }else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragment_container, new MainFragment());
            ft.commit();
        }
    }


    @Override
    public void sendAction(Action actionItem) {
        switch (actionItem.getActionName()) {
            case "My Plans":
                break;
            case "My Programs":
                break;
            case "Articles":
                openArticleFragment();
                break;
        }
    }

    private void openArticleFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, new ArticleFragment());
        ft.addToBackStack(ARTICLE_FRAG);
        ft.commit();
    }
}
