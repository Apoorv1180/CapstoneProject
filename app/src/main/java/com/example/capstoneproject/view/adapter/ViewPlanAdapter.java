package com.example.capstoneproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.PlanDetail;
import com.example.capstoneproject.view.activity.MainActivity;
import com.example.capstoneproject.view.activity.ProgressReadActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import sun.bob.mcalendarview.vo.DateData;

public class ViewPlanAdapter extends RecyclerView.Adapter<ViewPlanAdapter.ViewHolder> {

    private final List<PlanDetail> items;
    private Context context;
    Date date, Cdates;

    private boolean ifExpand = true;

    private OnItemClicked listener;
    private onItemDialog onItemDialog;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }

    public interface onItemDialog {
        void onClick(int position,Date date,PlanDetail model);
    }

    public ViewPlanAdapter(List<PlanDetail> items, Context context, onItemDialog onItemDialog) {
        this.items = items;
        this.context = context;
        this.onItemDialog = onItemDialog;
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


        String date2 = model.getRenewdate();
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat input = new SimpleDateFormat("dd-MMM-yy h:mm a");
        SimpleDateFormat output = new SimpleDateFormat("dd-MM-YYYY");
        String currentdate = input.format(cal.getTime());
        try {
            date = input.parse(date2);
            Cdates = input.parse(currentdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String str = output.format(date);
        System.out.println("hiii" + str);

        if (Cdates.compareTo(date) >= 0) {
            holder.btnrenew.setVisibility(View.VISIBLE);
        }else {
            holder.btnrenew.setVisibility(View.GONE);
        }
        holder.btnrenew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemDialog.onClick(position,date,model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView planName, joiningDate, renewDate, fees;
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

    }

}
