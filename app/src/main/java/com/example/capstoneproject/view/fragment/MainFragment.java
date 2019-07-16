package com.example.capstoneproject.view.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Action;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.view.adapter.ActionAdapter;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.example.capstoneproject.viewmodel.SaveUserViewModel;
import com.example.capstoneproject.viewmodel.SaveUserViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private List<Action> actionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SendMessages sendMessages;



    public MainFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sendMessages = (SendMessages) context;
    }
    // Interface - fragment to activity
    public interface SendMessages {
        void sendAction(Action actionItem);
    }

    //Receive message - activity to fragment
    public void receiveAction(Action actionItem) {
        //DO SOMETHING
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main,container,false);
        // bind your data here.
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prepareActionData();
        initView(view);
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.actions_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new ActionAdapter(actionList, new ActionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Action item) {
                sendMessages.sendAction(item);
            }
        }));
        int resId = R.anim.list_fall_down;

        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(
                getContext(), resId);
        recyclerView.setLayoutAnimation(animation);

    }

    private void prepareActionData() {
        Action action = new Action("My Plans", "Schedule and view My Plans", R.drawable.ic_my_plan);
        actionList.add(action);

        action= new Action("My Progress", "View my enrollements", R.drawable.ic_my_program);
        actionList.add(action);

        action = new Action("Articles", "Read and be fit", R.drawable.ic_my_article);
        actionList.add(action);
    }


}
