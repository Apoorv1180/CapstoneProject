package com.example.capstoneproject.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;

import java.util.List;

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Action item);
    }
    private final List<Action> items;
    private final OnItemClickListener listener;

    public ActionAdapter(List<Action> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_action_view, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(items.get(position), listener);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView actionTitle,actionDescription;
        private ImageView actionImage;

        public ViewHolder(View itemView) {
            super(itemView);
            actionTitle = (TextView) itemView.findViewById(R.id.action_title);
            actionDescription = (TextView) itemView.findViewById(R.id.action_description);
            actionImage = (ImageView) itemView.findViewById(R.id.action_image);
        }

        public void bind(final Action item, final OnItemClickListener listener) {
            actionTitle.setText(item.getActionName());
            actionDescription.setText(item.getActionDescription());
            actionImage.setBackgroundResource(item.getActionPhotoId());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}