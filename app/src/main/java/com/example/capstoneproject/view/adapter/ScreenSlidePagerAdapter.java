package com.example.capstoneproject.view.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.capstoneproject.view.fragment.ScreenSlidePageFragment;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    int NUM_PAGES;

    public ScreenSlidePagerAdapter(FragmentManager fm, int NUM_PAGES) {
        super(fm);
        this.NUM_PAGES = NUM_PAGES;
    }

    @Override
    public Fragment getItem(int position) {
        return new ScreenSlidePageFragment();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }
}