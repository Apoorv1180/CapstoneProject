package com.example.capstoneproject.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.util.ZoomOutPageTransformer;
import com.example.capstoneproject.view.adapter.CustomPageAdapter;
import com.example.capstoneproject.viewmodel.GetArticleListViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleReadActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.pager)
    ViewPager mPager;
    @BindView(R.id.next)
    Button next;
    @BindView(R.id.previous)
    Button previous;
    private PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
        initView();
        initToolbar();
        getArticleList();
        setOnclickListeners();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(R.string.read_articles);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void setOnclickListeners() {
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
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(ArticleReadActivity.this, MainActivity.class);
        startActivity(i);
        finish();
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
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_data), Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
