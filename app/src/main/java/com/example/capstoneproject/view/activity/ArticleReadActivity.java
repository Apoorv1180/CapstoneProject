package com.example.capstoneproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.util.ZoomOutPageTransformer;
import com.example.capstoneproject.view.adapter.CustomPageAdapter;
import com.example.capstoneproject.view.adapter.ScreenSlidePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ArticleReadActivity extends AppCompatActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
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
    Button next,previous;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_read);
        next = findViewById(R.id.next);
        previous=findViewById(R.id.previous);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        List<Article> getData = dataSource();
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        pagerAdapter = new CustomPageAdapter(this, getData);
        mPager.setAdapter(pagerAdapter);

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

    private List<Article> dataSource() {
        List<Article> data = new ArrayList<Article>();
        data.add(new Article(R.drawable.ic_my_article, getString(R.string.lorem_ipsum)));
        data.add(new Article(R.drawable.ic_my_plan, getString(R.string.lorem_ipsum)));
        data.add(new Article(R.drawable.ic_my_program, getString(R.string.lorem_ipsum)));
        return data;
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
}
