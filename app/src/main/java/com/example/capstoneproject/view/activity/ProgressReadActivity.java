package com.example.capstoneproject.view.activity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.DateCellView;
import com.example.capstoneproject.service.repository.DataRepository;
import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.viewmodel.SaveUserProgressViewModel;
import com.example.capstoneproject.viewmodel.SaveUserProgressViewModelFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.bob.mcalendarview.CellConfig;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnExpDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthScrollListener;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;
import sun.bob.mcalendarview.vo.DayData;


public class ProgressReadActivity extends AppCompatActivity {

    private TextView YearMonthTv;
    private ExpCalendarView expCalendarView;
    private DateData selectedDate;
    private AlertDialog alertDialog;
    private boolean ifExpand = true;
    private AlertDialog.Builder builder;
    Toolbar mToolbar;

    private static DataRepository dataRepository;
    private static Context context;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    ArrayList<DateData> dateArray = new ArrayList<>();
    static String weightForToday = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_read);

        setSupportActionBar(mToolbar);
        initToolbar();
        getExtras();
        initViews();
        imageInit();
        initDatabase();
        setAlreadyMarkedDates();
        setUpListeners();


        Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        expCalendarView.unMarkDate(selectedDate);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.progress_read_title);
        setSupportActionBar(mToolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {

            //  supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowTitleEnabled(true);
        }
    }

    private void getExtras() {
        if (getIntent().getExtras() != null) {
            weightForToday = getIntent().getExtras().getString("weight");
            if (!TextUtils.isEmpty(weightForToday)) {
                addInfoOnSelectedDate(converttoDateData(Util.getTodayDateInString()));
            }
        }
    }

    private void initDatabase() {
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.keepSynced(true);
    }

    private void setAlreadyMarkedDates() {
        //Fetch details from database of dates for particular user and display
        int year;
        int month;
        int day;

        final MutableLiveData<List<String>> articleData = new MutableLiveData<>();
        final List<DateData> articleList = new ArrayList<>();
        String userIdChild = "";
        if (auth.getCurrentUser() != null) {
            userIdChild = auth.getCurrentUser().getUid();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS_PROGRESS").child(userIdChild);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    Map<String, String> newUserMap = new HashMap<>();
                    Map<String, Map<String, String>> dateKey = (Map<String, Map<String, String>>) dataSnapshot.getValue();
                    Log.e("date", dateKey.toString());
                    dateArray.clear();
                    for (Map.Entry<String, Map<String, String>> entry : dateKey.entrySet()) {
                        dateArray.add(converttoDateData(entry.getKey()));
                    }

                    for (int i = 0; i < dateArray.size(); i++) {
                        expCalendarView.markDate(dateArray.get(i));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private DateData converttoDateData(String key) {
        ArrayList<String> values = new ArrayList<>(Arrays.asList(key.split("-")));
        //   List<String> values = (ArrayList<String>) Arrays.asList(key.split("-"));
        DateData newDateData = new DateData(Integer.valueOf(values.get(2)), Integer.valueOf(values.get(1)), Integer.valueOf(values.get(0)));
        return newDateData;
    }

    private void setUpListeners() {
        // Set up listeners.
        expCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(String.format("%dY%dM", year, month));

            }

            @Override
            public void onMonthScroll(float positionOffset) {
//                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        expCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                //  expCalendarView.getMarkedDates().removeAdd();
                // expCalendarView.markDate(date);
                // selectedDate = date;
                addInfoOnSelectedDate(date);
            }
        });
    }

    private void addInfoOnSelectedDate(DateData date) {
        builder = new AlertDialog.Builder(ProgressReadActivity.this)
                .setMessage("Please enter your calculated weight");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_weight, null);
        builder.setView(dialogView).create();

        alertDialog = builder.show();

        Button save;
        EditText editWeight;

        save = dialogView.findViewById(R.id.saveInfo);
        editWeight = dialogView.findViewById(R.id.weightInfo);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = editWeight.getText().toString();
                if (!TextUtils.isEmpty(weight)) {
                    saveWeightDetails(weight, date);
                }
            }
        });
    }


    private void initViews() {
        //      Get instance.
        expCalendarView = ((ExpCalendarView) findViewById(R.id.calendar_exp));
        expCalendarView.setMarkedStyle(MarkStyle.DOT);
        YearMonthTv = (TextView) findViewById(R.id.main_YYMM_Tv);
        YearMonthTv.setText(Calendar.getInstance().get(Calendar.YEAR) + "--Year--" + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "--Month");
    }


    private void imageInit() {
        final ImageView expandIV = (ImageView) findViewById(R.id.main_expandIV);
        expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifExpand) {
                    CellConfig.Month2WeekPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = false;
                    expandIV.setImageResource(R.drawable.ic_arrow_down);
                    expCalendarView.shrink();
                } else {
                    CellConfig.Week2MonthPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = true;
                    expandIV.setImageResource(R.drawable.ic_arrow_up);
                    expCalendarView.expand();
                }
                ifExpand = !ifExpand;
            }
        });
    }

    public void TravelToClick(View v) {
        Calendar calendar = Calendar.getInstance();

        expCalendarView.travelTo(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
    }

    private void saveWeightDetails(String weight, DateData eventDay) {

        String userIdChild = "";
        if (auth.getCurrentUser() != null) {
            userIdChild = auth.getCurrentUser().getUid();
        }

        Date date = null;
        String dateString = eventDay.getDay() + "-" + eventDay.getMonth() + "-" + eventDay.getYear();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("USERS_PROGRESS").child(userIdChild).child(simpleDateFormat.format(date));

        Map newUser = new HashMap();
        newUser.put("weight", weight);
        mDatabase.setValue(newUser, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Log.e("DATABASE", "Data could not be saved " + databaseError.getMessage());


                } else {
                    Log.e("DATABASE", "Data saved successfully.");
                    Toast.makeText(getApplicationContext(), "Info Saved Successfully", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                    alertDialog.cancel();

                }
            }
        });
    }
}