package com.example.capstoneproject.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.capstoneproject.R;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.example.capstoneproject.viewmodel.UploadImageViewModel;
import com.example.capstoneproject.viewmodel.UploadImageViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleCreateFragment extends Fragment {


    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private Uri filePath;
     ProgressDialog progressDialog;

    private final int PICK_IMAGE_REQUEST = 71;

    public ArticleCreateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btnChoose = (Button) view.findViewById(R.id.btnChoose);
        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        imageView = (ImageView) view.findViewById(R.id.imgView);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListeners();
    }

    private void initListeners() {
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (filePath != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            checkUserLoggedInStatus(filePath);

        }
    }

    private void checkUserLoggedInStatus(Uri filePath) {
        final CheckUserLoggedInViewModel viewModelLoggedInStatus =
                ViewModelProviders.of(this)
                        .get(CheckUserLoggedInViewModel.class);
        observeViewModelLoggedInStatus(viewModelLoggedInStatus, filePath);
    }

    private void observeViewModelLoggedInStatus(CheckUserLoggedInViewModel viewModelLoggedInStatus, final Uri filePath) {
        viewModelLoggedInStatus.isAlreadyLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser result) {
                if (result != null) {
                    Log.e("USER", "USER ALREADY LOGGED IN" + result.getEmail().toString());
                    uploadImagetoStorage(filePath,result);
                } else {
                    Log.e("USER", "USER NOT LOGGED IN");
                }
            }
        });
    }


    private void uploadImagetoStorage(Uri filePath, FirebaseUser result) {

        final UploadImageViewModel viewModelSignIn =
                ViewModelProviders.of(this, new UploadImageViewModelFactory(getActivity().getApplication(), filePath,result))
                        .get(UploadImageViewModel.class);
        observeViewModelUpload(viewModelSignIn);
    }

    private void observeViewModelUpload(UploadImageViewModel viewModelLoggedInStatus) {
        viewModelLoggedInStatus.isImageUploaded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if (result) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    Log.e("USER", " UPLOADED IMAGE  SUCCESSFUL" );
                } else {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    Log.e("USER", " UPLOADED IMAGE  UNSUCCESSFUL");
                }
            }
        });
    }

}