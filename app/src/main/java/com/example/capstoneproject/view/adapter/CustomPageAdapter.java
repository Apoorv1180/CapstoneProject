package com.example.capstoneproject.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomPageAdapter extends PagerAdapter {
    private Context context;
    private List<Article> dataObjectList;
    private LayoutInflater layoutInflater;


    public CustomPageAdapter(Context context, List<Article> dataObjectList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) this.context.getSystemService(this.context.LAYOUT_INFLATER_SERVICE);
        this.dataObjectList = dataObjectList;
    }

    @Override
    public int getCount() {
        return dataObjectList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = this.layoutInflater.inflate(R.layout.pager_list_item, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        ImageView displayImage = (ImageView) view.findViewById(R.id.large_image);
        TextView imageText = (TextView) view.findViewById(R.id.image_name);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(context)
                .load(this.dataObjectList.get(position).getImageUrl())
                .into(displayImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
        imageText.setText(this.dataObjectList.get(position).getArticleDescription());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}