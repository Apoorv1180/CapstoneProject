package com.example.capstoneproject.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;
import com.example.capstoneproject.service.model.UserDetail;
import com.example.capstoneproject.view.activity.UserDetailActivity;
import com.example.capstoneproject.view.adapter.ActionAdapter;
import com.example.capstoneproject.view.adapter.UserDataAdapter;
import com.example.capstoneproject.viewmodel.GetUserListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;


public class PlanCreation extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView plancreationUserRecyclerview;

    LinearLayoutManager layoutManager;

    private List<Action> actionList = new ArrayList<>();
    List<UserDetail> userList = new ArrayList<>();
    UserDataAdapter userDataAdapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PlanCreation() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlanCreation.
     */
    // TODO: Rename and change types and number of parameters
    public static PlanCreation newInstance(String param1, String param2) {
        PlanCreation fragment = new PlanCreation();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_plan_creation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        plancreationUserRecyclerview = (RecyclerView) view.findViewById(R.id.plancreation_user_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        plancreationUserRecyclerview.setLayoutManager(layoutManager);
        plancreationUserRecyclerview.setItemAnimator(new DefaultItemAnimator());
        plancreationUserRecyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        getUserList();
        userDataAdapter=new UserDataAdapter(userList,getActivity());
    }
    private void getUserList() {
        final GetUserListViewModel myModel =
                ViewModelProviders.of(this)
                        .get(GetUserListViewModel.class);
        ObserveGetUserListViewModel(myModel);
    }

    private void ObserveGetUserListViewModel(GetUserListViewModel getUserListViewModel) {
        getUserListViewModel.getUserData().observe(this, new Observer<List<UserDetail>>() {
            @Override
            public void onChanged(List<UserDetail> User) {
                if (!User.isEmpty()) {
                    userDataAdapter=new UserDataAdapter(User,getActivity());
                    plancreationUserRecyclerview.setAdapter(userDataAdapter);
                    userDataAdapter.notifyDataSetChanged();
                    //recyclerView.setItemsCanFocus(true);
                    int resId = R.anim.list_fall_down;

                    LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                            getContext(), resId);
                    plancreationUserRecyclerview.setLayoutAnimation(animation);
                }
            }

        });
    }
//    @Override
//    public void onItemClick(int position) {
//        UserDetail userDetail = userList.get(position);
//        Intent newIntent = new Intent(getActivity(),UserDetailActivity.class);
//        newIntent.putExtra("name",userDetail.getName());
//        startActivity(newIntent);
//    }

}
