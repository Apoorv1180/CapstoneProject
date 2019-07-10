package com.example.capstoneproject.view.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.capstoneproject.R;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.example.capstoneproject.viewmodel.SaveUserViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class MainFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUserLoggedInStatus();

    }

    private void checkUserLoggedInStatus() {
        final CheckUserLoggedInViewModel viewModelLoggedInStatus =
                ViewModelProviders.of(this)
                        .get(CheckUserLoggedInViewModel.class);
        observeViewModelLoggedInStatus(viewModelLoggedInStatus);
    }

    private void observeViewModelLoggedInStatus(CheckUserLoggedInViewModel viewModelLoggedInStatus) {
        viewModelLoggedInStatus.isAlreadyLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser result) {
                if(result.getDisplayName()==null){
                    fetchInformationInProfileDialog();
                }
            }
        });
    }

    private void fetchInformationInProfileDialog() {
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

                                saveUserValues();

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

    private void saveUserValues() {
        final SaveUserViewModel viewModelSaveUserStatus =
                ViewModelProviders.of(this)
                        .get(SaveUserViewModel.class);
        observeViewModelSaveUserStatus(viewModelSaveUserStatus);
    }

    private void observeViewModelSaveUserStatus(SaveUserViewModel viewModelSaveUserStatus) {
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

    }
}
