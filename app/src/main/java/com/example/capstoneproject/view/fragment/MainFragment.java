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
        initView(view);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkUserLoggedInStatus(view,savedInstanceState);
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

    }

    private void prepareActionData() {
        Action action = new Action("My Plans", "Schedule and view My Plans", R.drawable.ic_my_plan);
        actionList.add(action);

        action= new Action("My Programs", "View my enrollements", R.drawable.ic_my_program);
        actionList.add(action);

        action = new Action("Articles", "Read and be fit", R.drawable.ic_my_article);
        actionList.add(action);
    }

    private void checkUserLoggedInStatus(View view, Bundle savedInstanceState) {
        final CheckUserLoggedInViewModel viewModelLoggedInStatus =
                ViewModelProviders.of(this)
                        .get(CheckUserLoggedInViewModel.class);
        observeViewModelLoggedInStatus(viewModelLoggedInStatus,view,savedInstanceState);
    }

    private void observeViewModelLoggedInStatus(CheckUserLoggedInViewModel viewModelLoggedInStatus, final View view, Bundle savedInstanceState) {

        viewModelLoggedInStatus.isAlreadyLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser result) {
                if(result!=null) {
                    if (result.getDisplayName() == null) {
                        fetchInformationInProfileDialog(result,view);
                    }
                    else {
                        prepareActionData();
                        initView(view);
                    }

                }
            }

        });
    }

    private void fetchInformationInProfileDialog(final FirebaseUser result, final View view) {
        // get prompts.xml view
        final  EditText userName,userPhoneNumber;

        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.custom_profile_input_dialog_box, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        userName = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInputName);
        userPhoneNumber = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserPhoneNumber);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text
                                Util.checkUsername(userName.getText().toString().trim());
                                Util.checkPhoneNumber(userPhoneNumber.getText().toString().trim());

                                saveUserValues(result.getUid(),userName.getText().toString().trim(),userPhoneNumber.getText().toString().trim(),view);
                            }

                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    private void saveUserValues(String userId, String Uname, String Password, View view) {
        final SaveUserViewModel viewModelSignIn =
                ViewModelProviders.of(getActivity(), new SaveUserViewModelFactory(getActivity().getApplication(),userId, Uname, Password))
                        .get(SaveUserViewModel.class);
        observeViewModelSaveUserStatus(viewModelSignIn,view);
    }

    private void observeViewModelSaveUserStatus(SaveUserViewModel viewModelSaveUserStatus,  View view) {
    viewModelSaveUserStatus.isSavedStatus().observe(getActivity(), new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean result) {
            if(result){
                Log.e("USER","SAVED SUCCESSFULLY");

            }
            else
                Log.e("USER","NOT SAVED ");
        }
    });
        initView(view);
    }

}
