package com.example.capstoneproject.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstoneproject.CustomDateTimePicker;
import com.example.capstoneproject.R;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.viewmodel.LogoutViewModel;
import com.example.capstoneproject.viewmodel.SaveUserDetailViewModel;
import com.example.capstoneproject.viewmodel.SaveUserDetailViewModelFactory;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class UserDetailActivity extends AppCompatActivity {
    TextView uname;
    EditText joining_date, renew_date, fees,plan_name;
    Button save;
    String Uname,joiningdate,renewdate,fee,planname,uid;
    CustomDateTimePicker dateTimePicker,dateTimePickerrenew;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        if(getIntent()!=null){
            Uname=getIntent().getStringExtra("name");
            uid=getIntent().getStringExtra("uid");
        }
        setSupportActionBar(mToolbar);
        initToolbar();
        initViews();
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(R.string.user_plan_creation);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }


    }

    private void initViews() {
        uname = findViewById(R.id.uname);
        joining_date = findViewById(R.id.ed_join_date);
        renew_date = findViewById(R.id.et_renew_date);
        plan_name = findViewById(R.id.et_plan_name);
        fees=findViewById(R.id.et_fees);
        save = findViewById(R.id.save);


        uname.setText(Uname);

        joining_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (joining_date.getRight() - joining_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        dateTimePicker.showDialog();
                        return true;
                    }
                }
                return false;
            }
        });

        renew_date.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (renew_date.getRight() - renew_date.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        dateTimePickerrenew.showDialog();
                        return true;
                    }
                }
                return false;
            }
        });

        dateTimePicker = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {
                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {

                        if (joining_date.requestFocus()) {
                            joining_date.setText(Util.parseDateToddMMyyyy(year
                                    + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                    + " " + hour24 + ":" + min
                                    + ":" + sec));
                            joining_date.setError(null);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });

        dateTimePicker.set24HourFormat(true);

        dateTimePicker.setDate(Calendar.getInstance());



        dateTimePickerrenew = new CustomDateTimePicker(this,
                new CustomDateTimePicker.ICustomDateTimeListener() {
                    @Override
                    public void onSet(Dialog dialog, Calendar calendarSelected,
                                      Date dateSelected, int year, String monthFullName,
                                      String monthShortName, int monthNumber, int date,
                                      String weekDayFullName, String weekDayShortName,
                                      int hour24, int hour12, int min, int sec,
                                      String AM_PM) {
                            renew_date.setText(Util.parseDateToddMMyyyy(year
                                    + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                                    + " " + hour24 + ":" + min
                                    + ":" + sec));
                            renew_date.setError(null);

                    }

                    @Override
                    public void onCancel() {

                    }
                });

        dateTimePickerrenew.set24HourFormat(true);

        dateTimePickerrenew.setDate(Calendar.getInstance());




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joiningdate=joining_date.getText().toString();
                renewdate=renew_date.getText().toString();
                fee=fees.getText().toString();
                planname=plan_name.getText().toString();

                saveUserDetailsinDB();

                joining_date.setText("");
                renew_date.setText("");
                fees.setText("");
                plan_name.setText("");

            }
        });


    }


    private void saveUserDetailsinDB() {
        final SaveUserDetailViewModel saveUserDetailViewModel =
                ViewModelProviders.of(this, new SaveUserDetailViewModelFactory(getApplication(), Uname, joiningdate,renewdate,fee,planname,uid))
                        .get(SaveUserDetailViewModel.class);
        observeViewModelSaveUserStatu(saveUserDetailViewModel);

    }

    private void observeViewModelSaveUserStatu(SaveUserDetailViewModel saveUserDetailViewModel) {
        saveUserDetailViewModel.isUserRecordSave().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if(aBoolean){
                    Log.e("USER","UserRecord Saved Successfully");
                }else
                    Log.e("USER","UserRecord Saved UnSuccessful");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.logout:
                logout();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        final LogoutViewModel viewModelLogout =
                ViewModelProviders.of(this)
                        .get(LogoutViewModel.class);
        observeViewModelLogout(viewModelLogout);
    }


    private void observeViewModelLogout(LogoutViewModel viewModelSignIn) {
        viewModelSignIn.isLoggedOutStatus().observe(this, new Observer<Boolean>() {
            public void onChanged(@Nullable Boolean result) {
                if(result){
                    Intent loginPageIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    loginPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginPageIntent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
