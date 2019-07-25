package com.example.capstoneproject.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.PlanDetail;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.view.adapter.ViewPlanAdapter;
import com.example.capstoneproject.viewmodel.GetUserPlanViewModel;
import com.example.capstoneproject.viewmodel.SaveUserDetailViewModel;
import com.example.capstoneproject.viewmodel.SaveUserDetailViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sun.bob.mcalendarview.vo.DateData;

import static com.example.capstoneproject.view.activity.LoginActivity.USER_UUID;

public class PlanViewActivity extends AppCompatActivity implements ViewPlanAdapter.onItemDialog {

    RecyclerView viewplanuser;

    List<PlanDetail> planList = new ArrayList<>();
    ViewPlanAdapter viewPlanAdapter;

    LinearLayoutManager layoutManager;
    TextView emptyView;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    View dialogView;
    String uuid;
    Toolbar mToolbar;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    ArrayList<DateData> dateArray = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_view);
        initToolbar();
        setSupportActionBar(mToolbar);
        if (getIntent() != null) {
            uuid = getIntent().getStringExtra(USER_UUID);
        }
        initViews();

    }

    private void initToolbar() {
        mToolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.my_plan);
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }


    }

    private void initViews() {
        viewplanuser = (RecyclerView) findViewById(R.id.viewplan_user);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        viewplanuser.setLayoutManager(layoutManager);
        viewplanuser.setItemAnimator(new DefaultItemAnimator());
        emptyView =findViewById(R.id.emptyView);
        viewplanuser.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        getPlanList();
        viewPlanAdapter = new ViewPlanAdapter(planList, getApplicationContext(), this);


    }

    private void getPlanList() {
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
        setAlreadyMarkedDates();

        final GetUserPlanViewModel myModel =
                ViewModelProviders.of(this)
                        .get(GetUserPlanViewModel.class);
        ObserveGetUserPlanViewModel(myModel);

    }


    private LiveData<List<PlanDetail>> setAlreadyMarkedDates() {
        final MutableLiveData<List<PlanDetail>> articleData = new MutableLiveData<>();
        final List<PlanDetail> userList = new ArrayList<>();
        String userIdChild = "";
        if (auth.getCurrentUser() != null) {
            userIdChild = auth.getCurrentUser().getUid();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child(getResources().getString(R.string.user_details_child)).child(userIdChild);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    emptyView.setVisibility(View.GONE);
                    viewplanuser.setVisibility(View.VISIBLE);
                    PlanDetail planDetail = dataSnapshot.getValue(PlanDetail.class);
                    userList.add(planDetail);
                    articleData.setValue(userList);
                    viewPlanAdapter = new ViewPlanAdapter(userList, getApplicationContext(), PlanViewActivity.this);
                    //recyclerView.setItemsCanFocus(true);
                    int resId = R.anim.list_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                            getApplicationContext(), resId);
                    viewplanuser.setLayoutAnimation(animation);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return articleData;
    }

    private void ObserveGetUserPlanViewModel(GetUserPlanViewModel myModel) {
        myModel.getUserPlanData().observe(this, new Observer<List<PlanDetail>>() {
            @Override
            public void onChanged(List<PlanDetail> User) {

                if (!User.isEmpty()) {
                   // viewPlanAdapter = new ViewPlanAdapter(User, getApplicationContext(), PlanViewActivity.this);
                    viewplanuser.setAdapter(viewPlanAdapter);
                    //recyclerView.setItemsCanFocus(true);
                    int resId = R.anim.list_fall_down;
                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                            getApplicationContext(), resId);
                    viewplanuser.setLayoutAnimation(animation);
                }
            }

        });
        if (planList.size() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            viewplanuser.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(int position, Date date, PlanDetail planDetail) {
        builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog))
                .setMessage(getResources().getString(R.string.enter_fee));

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
                    UpdatePlanDetails(planDetail.getUname(), planDetail.getJoiningdate(), newDate, fees, planDetail.getPlanname(), uuid);
                }
            }
        });

    }

    private void UpdatePlanDetails(String uname, String joiningdate, String newDate, String fees, String planname, String uuid) {
        final SaveUserDetailViewModel saveUserDetailViewModel =
                ViewModelProviders.of(this, new SaveUserDetailViewModelFactory(getApplication(), uname, joiningdate, newDate, fees, planname, uuid))
                        .get(SaveUserDetailViewModel.class);
        observeViewModelSaveUserStatu(saveUserDetailViewModel);
    }

    private void observeViewModelSaveUserStatu(SaveUserDetailViewModel saveUserDetailViewModel) {
        saveUserDetailViewModel.isUserRecordSave().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {
                    Util.displaySnackBar(emptyView, getResources().getString(R.string.record_success));
                    alertDialog.dismiss();
                    alertDialog.cancel();
                } else
                    Util.displaySnackBar(emptyView, getResources().getString(R.string.record_unsuccess));


            }
        });
    }

    public static String addOneMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        SimpleDateFormat input = new SimpleDateFormat("dd-MMM-yy h:mm a");
        String strs = input.format(cal.getTime());
        return strs;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent newIntent = new Intent(this,MainActivity.class);
        startActivity(newIntent);
        finish();
    }

}
