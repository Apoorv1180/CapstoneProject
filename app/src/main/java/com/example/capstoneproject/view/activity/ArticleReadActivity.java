package com.example.capstoneproject.view.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.util.ZoomOutPageTransformer;
import com.example.capstoneproject.view.adapter.CustomPageAdapter;
import com.example.capstoneproject.view.adapter.ScreenSlidePagerAdapter;
import com.example.capstoneproject.viewmodel.GetArticleListViewModel;

import java.util.ArrayList;
import java.util.List;

public class ArticleReadActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    Toolbar mToolbar;
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;
    Button next, previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.blink,R.anim.fade_in_activity);
        setContentView(R.layout.activity_article_read);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);

        getArticleList();

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() != 0)
                    mPager.setCurrentItem(mPager.getCurrentItem() - 1);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPager.getCurrentItem() < mPager.getAdapter().getCount())
                    mPager.setCurrentItem(mPager.getCurrentItem() + 1);
            }
        });
        setSupportActionBar(mToolbar);
        initToolbar();
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }


    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void getArticleList() {
        final GetArticleListViewModel myModel =
                ViewModelProviders.of(this)
                        .get(GetArticleListViewModel.class);
        ObserveGetArticleListViewModel(myModel);
    }

    private void ObserveGetArticleListViewModel(GetArticleListViewModel getArticleListViewModel) {
        getArticleListViewModel.getArticleList().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (!articles.isEmpty()) {
                    mPager.setPageTransformer(true, new ZoomOutPageTransformer());
                    pagerAdapter = new CustomPageAdapter(ArticleReadActivity.this, articles);
                    mPager.setAdapter(pagerAdapter);
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
