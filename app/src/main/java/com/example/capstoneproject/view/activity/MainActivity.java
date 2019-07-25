package com.example.capstoneproject.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.view.fragment.AdminDashboardFragment;
import com.example.capstoneproject.view.fragment.ArticleCreateFragment;
import com.example.capstoneproject.view.fragment.MainFragment;
import com.example.capstoneproject.view.fragment.PlanCreation;
import com.example.capstoneproject.viewmodel.LogoutViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.capstoneproject.view.activity.LoginActivity.USER_CREDENTIAL;
import static com.example.capstoneproject.view.activity.LoginActivity.USER_UUID;

public class MainActivity extends AppCompatActivity implements MainFragment.SendMessages, AdminDashboardFragment.SendMessages {

    private static final String ARTICLE_FRAG_CREATE = "TAG_ARTICLE_CREATE_FRAG";
    private static final String PLAN_FRAG_CREATE = "TAG_PLAN_CREATE_FRAG";
    public static final String MY_PLAN = "My Plans";
    public static final String MY_PROGRESS = "My Progress";
    public static final String MY_ARTICLES = "Articles";
    String credential, uuid;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        initToolbar();
        setSupportActionBar(mToolbar);
        if (getIntent() != null) {
            credential = getIntent().getStringExtra(USER_CREDENTIAL);
            Util.setCredential(this, credential);
            uuid = getIntent().getStringExtra(USER_UUID);
        }
        openMainFragment();

    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }


    }

    private void openMainFragment() {
        if (Util.getCredential(this).equalsIgnoreCase(getString(R.string.admin_email))) {
            // if (getRole(MainActivity.this).equalsIgnoreCase("Admin")) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mToolbar.setTitle(R.string.admin_panel);
            ft.add(R.id.fragment_container, new AdminDashboardFragment());
            ft.commit();
        } else {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mToolbar.setTitle(R.string.user_panel);
            ft.add(R.id.fragment_container, new MainFragment());
            ft.commit();
        }
    }


    @Override
    public void sendAction(Action actionItem) {
        switch (actionItem.getActionName()) {
            case MY_PLAN:
                openViePlanActivity();
                break;
            case MY_PROGRESS:
                openProgressReadActivity();
                break;
            case MY_ARTICLES:
                openArticleReadActivity();
                break;
        }
    }

    private void openViePlanActivity() {
        Intent newIntent = new Intent(this, PlanViewActivity.class);
        newIntent.putExtra(USER_UUID, uuid);
        startActivity(newIntent);
        finish();
    }

    private void openProgressReadActivity() {
        Intent newIntent = new Intent(this, ProgressReadActivity.class);
        startActivity(newIntent);
        finish();
    }

    private void openArticleReadActivity() {
        Intent newIntent = new Intent(this, ArticleReadActivity.class);
        startActivity(newIntent);
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.logout:
                logout();
                finish();
                return true;
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                    if (Util.getCredential(this).equalsIgnoreCase("ekta@gmail.com")) {
                        mToolbar.setTitle(R.string.admin_panel);
                    } else
                        mToolbar.setTitle(R.string.user_panel);
                }
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void logout() {
        final LogoutViewModel viewModelLogout =
                ViewModelProviders.of(this)
                        .get(LogoutViewModel.class);
        observeViewModelLogout(viewModelLogout);
    }


    private void observeViewModelLogout(LogoutViewModel viewModelSignIn) {
        viewModelSignIn.isLoggedOutStatus().observe(this, new Observer<Boolean>() {
            public void onChanged(@Nullable Boolean result) {
                if (result) {
                    Intent loginPageIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginPageIntent);
                }
            }
        });
    }

    @Override
    public void sendAction(int actionItem) {

        switch (actionItem) {
            case 0:
                FragmentManager frm = getSupportFragmentManager();
                mToolbar.setTitle(R.string.create_plan);
                FragmentTransaction frt = frm.beginTransaction();
                frt.replace(R.id.fragment_container, new PlanCreation());
                frt.addToBackStack(PLAN_FRAG_CREATE);
                frt.commit();
                break;
            case 1:
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                mToolbar.setTitle(R.string.create_article);
                ft.replace(R.id.fragment_container, new ArticleCreateFragment());
                ft.addToBackStack(ARTICLE_FRAG_CREATE);
                ft.commit();
                break;
        }
    }

}
