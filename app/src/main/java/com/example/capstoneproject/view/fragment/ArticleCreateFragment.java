package com.example.capstoneproject.view.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.viewmodel.CheckUserLoggedInViewModel;
import com.example.capstoneproject.viewmodel.SaveArticleViewModel;
import com.example.capstoneproject.viewmodel.SaveArticleViewModelFactory;
import com.example.capstoneproject.viewmodel.SaveImageUrlViewModel;
import com.example.capstoneproject.viewmodel.SaveImageUrlViewModelFactory;
import com.example.capstoneproject.viewmodel.UploadImageViewModel;
import com.example.capstoneproject.viewmodel.UploadImageViewModelFactory;
import com.google.firebase.auth.FirebaseUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleCreateFragment extends Fragment {


    private Button btnChoose, btnUpload,btnSaveArticle,btnReset;
    private EditText articleDesc;
    private ImageView imageView;
    private Uri filePath;
    private byte[] byteData;
    ProgressDialog progressDialog;
    CoordinatorLayout coordinatorLayout;
    private String mChildPath;
    private Article article;

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
        article= new Article();
        return view;
    }

    private void initView(View view) {
        btnUpload = (Button) view.findViewById(R.id.btnUpload);
        btnSaveArticle=(Button)view.findViewById(R.id.SaveArticle);
        btnReset=(Button)view.findViewById(R.id.RefreshPage);
        imageView = (ImageView) view.findViewById(R.id.imgView);
        articleDesc=(EditText)view.findViewById(R.id.articleDescription);
        articleDesc.setEnabled(false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initListeners();
    }

    private void initListeners() {
        imageView.setOnClickListener(new View.OnClickListener() {
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

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPage();
            }
        });
        btnSaveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = articleDesc.getText().toString();
                if(!TextUtils.isEmpty(desc))
                    article.setArticleDescription(desc);


                if(!TextUtils.isEmpty(articleDesc.getText()))
                    saveArticleInDatabase(article);
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_picture)), PICK_IMAGE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            articleDesc.setEnabled(true);
            articleDesc.requestFocus();
            btnUpload.setVisibility(View.VISIBLE);
            btnSaveArticle.setVisibility(View.GONE);
            btnReset.setVisibility(View.GONE);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
                imageView.setDrawingCacheEnabled(true);
                imageView.buildDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byteData = baos.toByteArray();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if (byteData != null) {

            checkUserLoggedInStatus(byteData);
        }
    }

    private void checkUserLoggedInStatus(byte[] byteData) {
        final CheckUserLoggedInViewModel viewModelLoggedInStatus =
                ViewModelProviders.of(this)
                        .get(CheckUserLoggedInViewModel.class);
        observeViewModelLoggedInStatus(viewModelLoggedInStatus, byteData);
    }

    private void observeViewModelLoggedInStatus(CheckUserLoggedInViewModel viewModelLoggedInStatus, final byte[] byteData) {
        viewModelLoggedInStatus.isAlreadyLoggedInStatus().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser result) {
                if (result != null) {
                    uploadImagetoStorage(byteData,result);
                } else {
                    Log.e(getString(R.string.key_user), getString(R.string.not_logged_in));
                }
            }
        });
    }


    private void uploadImagetoStorage(byte[] byteData, FirebaseUser result) {
        if (!TextUtils.isEmpty(articleDesc.getText().toString())) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCancelable(false);
            progressDialog.setTitle(getResources().getString(R.string.Upload_));
            progressDialog.show();
            mChildPath = Util.makePath();
            final UploadImageViewModel uploadImageViewModel =
                    ViewModelProviders.of(this, new UploadImageViewModelFactory(getActivity().getApplication(), byteData, result, mChildPath))
                            .get(UploadImageViewModel.class);
            observeViewModelUpload(uploadImageViewModel, mChildPath);
        } else {
            articleDesc.setError(getResources().getString(R.string.desc_article));

        }
    }

    private void observeViewModelUpload(UploadImageViewModel viewModelUploadStatus, final String mChildPath) {
        viewModelUploadStatus.isImageUploaded().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean result) {
                if (result) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    fetchImageUrlsAndSaveInDatabase(mChildPath,filePath);
                } else {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                }
            }
        });
    }

    private void fetchImageUrlsAndSaveInDatabase(String mChildPath,Uri filePath) {
        progressDialog.setTitle(getResources().getString(R.string.url_fetch));
        progressDialog.show();
        final SaveImageUrlViewModel saveImageUrlViewModel =
                ViewModelProviders.of(this, new SaveImageUrlViewModelFactory(getActivity().getApplication(), mChildPath,filePath))
                        .get(SaveImageUrlViewModel.class);
        observeViewModelSaveImageUrl(saveImageUrlViewModel);
    }

    private void observeViewModelSaveImageUrl(SaveImageUrlViewModel saveImageUrlViewModel) {
        saveImageUrlViewModel.isImageUrlSaved().observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri result) {
                if (result != null) {
                    //        showAddMoreSnackBar();
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    btnSaveArticle.setVisibility(View.VISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                    btnUpload.setVisibility(View.GONE);
                    btnSaveArticle.setEnabled(true);
                    btnReset.setEnabled(true);
                    article.setImageUrl(result.toString());

                } else {
                    Log.e("USER", "retry");
                    //       showRetrySnackBar();
                    imageView.setImageResource(android.R.color.transparent);
                    Util.displaySnackBar(btnReset, getResources().getString(R.string.retry_msg));
                }
            }
        });
    }

    private void saveArticleInDatabase(Article article) {
        final SaveArticleViewModel articleViewModel =
                ViewModelProviders.of(this, new SaveArticleViewModelFactory(getActivity().getApplication(), article.getImageUrl(), article.getArticleDescription()))
                        .get(SaveArticleViewModel.class);
        observeViewModelSaveArticleStatus(articleViewModel);

    }

    private void observeViewModelSaveArticleStatus(SaveArticleViewModel articleViewModel) {
        articleViewModel.isArticleSaved().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    Util.displaySnackBar(imageView, "Image Saved Successfully");
                    resetPage();
                } else {
                    Util.displaySnackBar(imageView, "Image Could Not be Saved Successfully");
                }
            }
        });
    }

    private void resetPage() {
        imageView.setImageResource(android.R.color.transparent);
        articleDesc.setText(null);
    }
}