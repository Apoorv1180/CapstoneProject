package com.example.capstoneproject.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.capstoneproject.R;
import com.example.capstoneproject.viewmodel.SaveUserProgressViewModel;
import com.example.capstoneproject.viewmodel.SaveUserProgressViewModelFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.model.CalendarEvent;
import devs.mulham.horizontalcalendar.utils.CalendarEventsPredicate;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class ProgressReadActivity extends AppCompatActivity {

    HorizontalCalendar horizontalCalendar;
    HorizontalCalendar.Builder builder;
    FloatingActionButton fab;
    CardView cardView;
    Button save;
    EditText editWeight;
    Calendar selectedDate,startDate,endDate,defaultSelectedDate;
    String selectedDateStr;
    final MutableLiveData<Boolean> returnEvent = new MutableLiveData<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_read);
        cardView = findViewById(R.id.infoCardView);
        save=findViewById(R.id.saveInfo);
        editWeight=findViewById(R.id.weightInfo);
        /** end after 1 month from now */ endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);
        /** start before 1 month from now */ startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        // Default Date set to Today.
        defaultSelectedDate = Calendar.getInstance();
        builder= new HorizontalCalendar.Builder(this,R.id.calendarView);
        horizontalCalendar = builder
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showTopText(true)
                .showBottomText(true)
                .textColor(Color.LTGRAY, Color.WHITE)
                .colorTextMiddle(Color.LTGRAY, Color.parseColor("#ffd54f"))
                .end()
                .defaultSelectedDate(defaultSelectedDate).build();
//                .addEvents(new CalendarEventsPredicate() {
//
//                    Random rnd = new Random();
//
//                    @Override
//                    public List<CalendarEvent> events(Calendar date) {
//                        List<CalendarEvent> events = new ArrayList<>();
//                        int count = rnd.nextInt(6);
//
//                        for (int i = 0; i <= count; i++) {
//                            events.add(new CalendarEvent(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), "event"));
//                        }
//
//                        return events;
//                    }
//                })
//                .build();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String weight = editWeight.getText().toString();

                if(!TextUtils.isEmpty(weight)){
                    saveWeightDetails(weight,selectedDateStr);
                }
            }
        });




        Log.i("Default Date", DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate).toString());

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                selectedDate =date;
                selectedDateStr = DateFormat.format("EEE, MMM d, yyyy", date).toString();
                Toast.makeText(ProgressReadActivity.this, selectedDateStr + " selected!", Toast.LENGTH_SHORT).show();
                Log.i("onDateSelected", selectedDateStr + " - Position = " + position);
                cardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCalendarScroll(HorizontalCalendarView calendarView, int dx, int dy) {
                super.onCalendarScroll(calendarView, dx, dy);
                cardView.setVisibility(View.GONE);
            }

        });

    }

    private void saveWeightDetails(String weight,String selectedDateStr) {
        final SaveUserProgressViewModel viewModel =
                ViewModelProviders.of(this, new SaveUserProgressViewModelFactory(this.getApplication(),weight,selectedDateStr))
                        .get(SaveUserProgressViewModel.class);
        observeViewModelSaveUserProgressStatus(viewModel);
    }

    private void observeViewModelSaveUserProgressStatus(SaveUserProgressViewModel viewModel) {

        viewModel.isSavedProgressStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    createEvent();
                //    returnEvent.setValue(aBoolean);
//                    builder.addEvents(new CalendarEventsPredicate() {
////                                @Override
////                                public List<CalendarEvent> events(Calendar date) {
////                                    if(date==selectedDate) {
////                                        Log.e("ENTER","PPPPPPPPPP");
////                                        List<CalendarEvent> events = new ArrayList<>();
////                                        events.add(new CalendarEvent(Color.GREEN, "event"));
////                                        return events;
////                                    }
////                                    return null;
////                                }
////                            })  ;
                }
            }
        });


    }

    private void createEvent() {
             CalendarEventsPredicate eventsPredicate = new CalendarEventsPredicate() {

                    Random rnd = new Random();

                    @Override
                    public List<CalendarEvent> events(Calendar date) {
                        List<CalendarEvent> events = new ArrayList<>();
                        int count = rnd.nextInt(6);
                        Log.e("YOYOYOOY","YPYPOYOYOY");
                        for (int i = 0; i <= count; i++) {
                            events.add(new CalendarEvent(Color.rgb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)), "event"));
                        }

                        return events;
                    }
                };
             builder.addEvents(eventsPredicate);
            // horizontalCalendar = builder.build();
             horizontalCalendar.refresh();
    }
    }





