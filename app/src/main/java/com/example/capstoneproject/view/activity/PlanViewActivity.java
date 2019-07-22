package com.example.capstoneproject.view.activity;

import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.PlanDetail;
import com.example.capstoneproject.view.adapter.ViewPlanAdapter;
import com.example.capstoneproject.viewmodel.GetUserPlanViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PlanViewActivity extends AppCompatActivity {

    RecyclerView viewplanuser;

    List<PlanDetail> planList = new ArrayList<>();
    ViewPlanAdapter viewPlanAdapter;

    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_view);
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
        viewPlanAdapter=new ViewPlanAdapter(planList,getApplicationContext());
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
                    viewPlanAdapter=new ViewPlanAdapter(User,getApplicationContext());
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
}
