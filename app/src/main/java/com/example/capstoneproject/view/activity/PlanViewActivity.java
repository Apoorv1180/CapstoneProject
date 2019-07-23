package com.example.capstoneproject.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.PlanDetail;
import com.example.capstoneproject.service.model.UserDetail;
import com.example.capstoneproject.view.adapter.ViewPlanAdapter;
import com.example.capstoneproject.viewmodel.GetUserPlanViewModel;
import com.example.capstoneproject.viewmodel.SaveUserDetailViewModel;
import com.example.capstoneproject.viewmodel.SaveUserDetailViewModelFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.capstoneproject.view.activity.LoginActivity.USER_CREDENTIAL;
import static com.example.capstoneproject.view.activity.LoginActivity.USER_UUID;

public class PlanViewActivity extends AppCompatActivity implements ViewPlanAdapter.onItemDialog {

    RecyclerView viewplanuser;

    List<PlanDetail> planList = new ArrayList<>();
    ViewPlanAdapter viewPlanAdapter;

    LinearLayoutManager layoutManager;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    View dialogView;
    PlanDetail planDetail=new PlanDetail();
    String uuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_view);

        if (getIntent() != null) {
            uuid = getIntent().getStringExtra(USER_UUID);
        }
        //viewplanuser=findViewById(R.id.viewplan_user);
        initViews();

    }

    private void initViews() {
        viewplanuser = (RecyclerView) findViewById(R.id.viewplan_user);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        viewplanuser.setLayoutManager(layoutManager);
        viewplanuser.setItemAnimator(new DefaultItemAnimator());
        viewplanuser.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        getPlanList();
        viewPlanAdapter=new ViewPlanAdapter(planList,getApplicationContext(),this);
    }

    private void getPlanList() {
            final GetUserPlanViewModel myModel =
                    ViewModelProviders.of(this)
                            .get(GetUserPlanViewModel.class);
            ObserveGetUserPlanViewModel(myModel);
    }

    private void ObserveGetUserPlanViewModel(GetUserPlanViewModel myModel) {
        myModel.getUserPlanData().observe(this, new Observer<List<PlanDetail>>() {
            @Override
            public void onChanged(List<PlanDetail> User) {
                if (!User.isEmpty()) {
                    viewPlanAdapter=new ViewPlanAdapter(User,getApplicationContext(),PlanViewActivity.this);
                    viewplanuser.setAdapter(viewPlanAdapter);
                    //recyclerView.setItemsCanFocus(true);
                    int resId = R.anim.list_fall_down;

                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                            getApplicationContext(), resId);
                    viewplanuser.setLayoutAnimation(animation);
                }
            }

        });
    }

    @Override
    public void onClick(int position, Date date) {
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setMessage("Please enter your Fees");

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dialogView = inflater.inflate(R.layout.dialog_renew_plan, null);
        builder.setView(dialogView).create();
        alertDialog = builder.show();

        Button save;
        EditText planfees;
        TextView newrenewDate;

        save = dialogView.findViewById(R.id.saveInfo);
        planfees = dialogView.findViewById(R.id.edt_plan_fee);
        newrenewDate = dialogView.findViewById(R.id.tv_renew_date);
        newrenewDate.setText(addOneMonth(date));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fees = planfees.getText().toString();
                String newDate = newrenewDate.getText().toString();
                if (!TextUtils.isEmpty(fees)) {
                    UpdatePlanDetails(planDetail.getUname() , planDetail.getJoiningdate(),newDate,fees,planDetail.getPlanname(),uuid);
                    //  saveWeightDetails(weight, date);
                }
            }
        });

    }

    private void UpdatePlanDetails(String uname,String joiningdate,String newDate, String fees,String planname,String uuid) {
        final SaveUserDetailViewModel saveUserDetailViewModel =
                ViewModelProviders.of(this, new SaveUserDetailViewModelFactory(getApplication(),uname , joiningdate,newDate,fees,planname,uuid))
                        .get(SaveUserDetailViewModel.class);
        observeViewModelSaveUserStatu(saveUserDetailViewModel);
    }

    private void observeViewModelSaveUserStatu(SaveUserDetailViewModel saveUserDetailViewModel) {
        saveUserDetailViewModel.isUserRecordSave().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    Log.e("USER","UserRecord Update Successfully");
                    alertDialog.dismiss();
                    alertDialog.cancel();
                }else
                    Log.e("USER","UserRecord Update UnSuccessful");

            }
        });
    }

    public static String addOneMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        SimpleDateFormat input = new SimpleDateFormat("dd-MMM-yy h:mm a");
        String strs = input.format(cal.getTime());
        return strs;
    }
}
