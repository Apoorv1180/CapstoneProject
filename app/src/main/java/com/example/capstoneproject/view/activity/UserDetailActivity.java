package com.example.capstoneproject.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.capstoneproject.CustomDateTimePicker;
import com.example.capstoneproject.R;
import com.example.capstoneproject.util.Util;

import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

public class UserDetailActivity extends AppCompatActivity {
    TextView uname;
    EditText joining_date, renew_date, fees;
    Button save;
    CustomDateTimePicker dateTimePicker,dateTimePickerrenew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        initViews();
    }

    private void initViews() {
        uname = findViewById(R.id.uname);
        joining_date = findViewById(R.id.ed_join_date);
        renew_date = findViewById(R.id.et_renew_date);
        save = findViewById(R.id.save);


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


    }

}
