package com.example.capstoneproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;
import com.example.capstoneproject.service.model.UserDetail;
import com.example.capstoneproject.view.activity.UserDetailActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    private final List<UserDetail> items;
    private Context context;

    private OnItemClicked listener;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public UserDataAdapter(List<UserDetail> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_data, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UserDataAdapter.ViewHolder holder, final int position) {

        UserDetail model = items.get(position);

        holder.userName.setText(model.getName());
        holder.UserPhno.setText(model.getPhone());

        holder.usercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context,UserDetailActivity.class);
                newIntent.putExtra("name",holder.userName.getText().toString());
                context.startActivity(newIntent);
              //  listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView userName, UserPhno;
        private ImageView UserImage;
        private CardView usercard;

        public ViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            UserPhno = (TextView) itemView.findViewById(R.id.user_phno);
            UserImage = (ImageView) itemView.findViewById(R.id.user_image);
            usercard = (CardView) itemView.findViewById(R.id.card_user);

        }
    }

    public void setOnClick(OnItemClicked listener) {
        this.listener = listener;
    }
}