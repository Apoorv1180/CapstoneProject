package com.example.capstoneproject.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;
import com.example.capstoneproject.view.callbacks.OnItemClickListener;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.MyViewHolder> {

    private List<Action> actionList;
    private final OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView actionTitle, actionDescription;
        public ImageView actionImage;

        public MyViewHolder(View view) {
            super(view);
            actionTitle = (TextView) view.findViewById(R.id.action_title);
            actionDescription = (TextView) view.findViewById(R.id.action_description);
            actionImage = (ImageView) view.findViewById(R.id.action_image);
        }
    }


    public ActionAdapter(List<Action> actionList, OnItemClickListener listener) {
        this.actionList = actionList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_action_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Action action = actionList.get(position);
        holder.actionTitle.setText(action.getActionName());
        holder.actionDescription.setText(action.getActionDescription());
        holder.actionImage.setBackgroundResource(action.getActionPhotoId());
    }

    @Override
    public int getItemCount() {
        return actionList.size();
    }
}