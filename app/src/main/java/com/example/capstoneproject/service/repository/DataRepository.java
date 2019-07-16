package com.example.capstoneproject.service.repository;


import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DataRepository {

    private static DataRepository dataRepository;
    private static Context context;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    UploadTask uploadTask;

    public DataRepository(Application application) {
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
     //   mDatabase.keepSynced(true);
      //  FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        storageReference = storage.getReference();
    }

    public synchronized static DataRepository getInstance(Application application) {
        if (dataRepository == null) {
            dataRepository = new DataRepository(application);
            context = application.getApplicationContext();
        }
        return dataRepository;
    }

    public LiveData<FirebaseUser> registerUser(String mUserName, String mPassword) {
        final MutableLiveData<FirebaseUser> userValues = new MutableLiveData<>();
        auth.createUserWithEmailAndPassword(mUserName, mPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            userValues.setValue(null);
                        } else {
                            if (auth.getCurrentUser() != null) {
                                userValues.setValue(auth.getCurrentUser());
                            } else
                                userValues.setValue(null);
                        }
                    }
                });
        return userValues;
    }

    public LiveData<FirebaseUser> signInUser(String mUserName, String mPassword) {
        final MutableLiveData<FirebaseUser> userValuesSignIn = new MutableLiveData<>();
        auth.signInWithEmailAndPassword(mUserName, mPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            userValuesSignIn.setValue(null);
                        } else {
                            if (auth.getCurrentUser() != null) {
                                userValuesSignIn.setValue(auth.getCurrentUser());
                            } else
                                userValuesSignIn.setValue(null);
                        }
                    }
                });
        return userValuesSignIn;
    }

    public LiveData<FirebaseUser> checkIfUserIsLoggedIn() {
        final MutableLiveData<FirebaseUser> userValues = new MutableLiveData<>();

        if (auth.getCurrentUser() != null) {
            userValues.setValue(auth.getCurrentUser());
        } else
            userValues.setValue(null);

        return userValues;
    }

    public LiveData<Boolean> saveUser(String userId, String mUserName, String mPhoneNumber) {
        final MutableLiveData<Boolean> status = new MutableLiveData<>();
        String userIdChild = userId;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS").child(userIdChild);
        Map newUser = new HashMap();
        newUser.put("name", mUserName);
        newUser.put("phone", mPhoneNumber);
        FirebaseUser user =  auth.getCurrentUser();

        mDatabase.setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    status.setValue(Boolean.FALSE);
                } else
                    status.setValue(Boolean.TRUE);
            }
        });
        return status;
    }


    public LiveData<Boolean> logout() {
        final MutableLiveData<Boolean> status = new MutableLiveData<>();

        auth.signOut();

        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    status.setValue(Boolean.TRUE);
//                    auth = null;
//                    mDatabase = null;
//                    storage = null;
//                    storageReference = null;
                } else {
                    status.setValue(Boolean.FALSE);
//                    auth = null;
//                    auth = null;
//                    mDatabase = null;
//                    storage = null;
//                    storageReference = null;
                }
            }
        });

        return status;
    }
    //"ARTICLE_IMAGES/"+ UUID.randomUUID().toString()

    public LiveData<Boolean> saveImage(byte[] byteData, FirebaseUser mUser, String childPath) {
        final MutableLiveData<Boolean> status = new MutableLiveData<>();

        uploadTask = storageReference.child(childPath).putBytes(byteData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                status.setValue(Boolean.FALSE);

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                status.setValue(Boolean.TRUE);
            }
        });
        return status;
    }


    public LiveData<Uri> saveImageUrl(String mChildPath, Uri filePath) {
        final StorageReference ref = storageReference.child(mChildPath);
        final MutableLiveData<Uri> uri = new MutableLiveData<>();

        uploadTask = ref.putFile(filePath);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    uri.setValue(downloadUri);
                } else {
                    // Handle failures
                    // ...
                    uri.setValue(null);
                }
            }
        });

//        uri.setValue(urlTask.getResult());
        return uri;
    }

    public LiveData<Boolean> saveArticle(String mImageUrl, String mArticleDescripton) {
        final MutableLiveData<Boolean>  status = new MutableLiveData<>();
        String userIdChild = UUID.randomUUID().toString();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES").child(userIdChild);
        Map newUser = new HashMap();
        newUser.put("imageUrl", mImageUrl);
        newUser.put("articleDescription", mArticleDescripton);
        mDatabase.setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    status.setValue(Boolean.FALSE);
                } else
                    status.setValue(Boolean.TRUE);
            }
        });
        return status;
    }

    public LiveData<Boolean> saveUserProgress(String mWeight,String selectedDate) {
        final MutableLiveData<Boolean>  status = new MutableLiveData<>();
        String userIdChild="";
        if(auth.getCurrentUser()!=null) {
             userIdChild = auth.getCurrentUser().getUid();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS_PROGRESS").child(selectedDate).child(userIdChild);
        Map newUser = new HashMap();
        newUser.put("weight", mWeight);

        mDatabase.setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    status.setValue(Boolean.FALSE);
                } else
                    status.setValue(Boolean.TRUE);
            }
        });
        return status;
    }

    public LiveData<List<Article>> getArticles() {
        final MutableLiveData<List<Article>> articleData = new MutableLiveData<>();
        final List<Article> articleList =new ArrayList<>();

        mDatabase=FirebaseDatabase.getInstance().getReference().child("ARTICLES");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Article article = postSnapshot.getValue(Article.class);
                    articleList.add(article);
                }
                articleData.setValue(articleList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return articleData;
    }
}
