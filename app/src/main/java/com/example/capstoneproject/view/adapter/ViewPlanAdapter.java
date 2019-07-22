package com.example.capstoneproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.PlanDetail;
import com.example.capstoneproject.view.activity.MainActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import sun.bob.mcalendarview.vo.DateData;

public class ViewPlanAdapter extends RecyclerView.Adapter<ViewPlanAdapter.ViewHolder>{

    private final List<PlanDetail> items;
    private Context context;
    java.sql.Date date;

    private OnItemClicked listener;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public ViewPlanAdapter(List<PlanDetail> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_plan, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        PlanDetail model = items.get(position);

        holder.planName.setText(model.getPlanname());
        holder.joiningDate.setText(model.getJoiningdate());
        holder.renewDate.setText(model.getRenewdate());
        holder.fees.setText(model.getFees());

        long millis=System.currentTimeMillis();
       date=new java.sql.Date(millis);

        holder.compareDates(String.valueOf(date),model.getRenewdate());

        holder.btnrenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(context,MainActivity.class);
                //newIntent.putExtra("name",holder.userName.getText().toString());
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

        private TextView planName, joiningDate,renewDate,fees;
        private ImageView UserImage;
        private CardView usercard;
        private Button btnrenew;

        public ViewHolder(View itemView) {
            super(itemView);
            planName = (TextView) itemView.findViewById(R.id.plan_name);
            joiningDate = (TextView) itemView.findViewById(R.id.joining_date);
            renewDate = (TextView) itemView.findViewById(R.id.renew_date);
            fees = (TextView) itemView.findViewById(R.id.fees);
            btnrenew = (Button) itemView.findViewById(R.id.btn_renew);


        }

        public  int compareDates(String date, String date2) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(date).compareTo(sdf.format(date2));
        }
//        private String converttoDateData(String date) {
//            ArrayList<String> values = new ArrayList<>(Arrays.asList(date.split("-")));
//            String day=values.get(0);
//            //   List<String> values = (ArrayList<String>) Arrays.asList(key.split("-"));
//           // DateData newDateData = new DateData(Integer.valueOf(values.get(0)),Integer.valueOf(values.get(1)),Integer.valueOf(values.get(2)));
//            return day;
//        }
    }

    public void setOnClick(OnItemClicked listener) {
        this.listener = listener;
    }

}
