package com.example.capstoneproject.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.capstoneproject.R;
import com.example.capstoneproject.viewmodel.SaveUserProgressViewModel;
import com.example.capstoneproject.viewmodel.SaveUserProgressViewModelFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProgressReadActivity extends AppCompatActivity {

    CalendarView calendarView;
    CardView cardView;
    final MutableLiveData<Boolean> returnEvent = new MutableLiveData<>();
    AlertDialog alertDialog;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_read);
        calendarView = findViewById(R.id.calendarView);
        cardView = findViewById(R.id.infoCardView);


    }

    private void saveWeightDetails(String weight, EventDay eventDay) {
        final SaveUserProgressViewModel viewModel =
                ViewModelProviders.of(this, new SaveUserProgressViewModelFactory(this.getApplication(), weight, convertDate(eventDay.getCalendar().getTime())))
                        .get(SaveUserProgressViewModel.class);
        observeViewModelSaveUserProgressStatus(viewModel, eventDay);
    }

    private void observeViewModelSaveUserProgressStatus(SaveUserProgressViewModel viewModel, EventDay eventDay) {

        viewModel.isSavedProgressStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    createEvent(eventDay);
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ProgressReadActivity.this)
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
                        selectedDate=eventDay.getCalendar().getTime().toString();
                        if (!TextUtils.isEmpty(weight)) {
                            saveWeightDetails(weight, eventDay);
                        }
                    }
                });
            }
        });

    }

    private void createEvent(EventDay eventDay) {
        List<EventDay> events = new ArrayList<>();
        events.add(new EventDay(eventDay.getCalendar(), R.drawable.circle_indicator));
        calendarView.setEvents(events);
        alertDialog.dismiss();
        alertDialog.cancel();
    }

    private String convertDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd ");
        String strDate = dateFormat.format(date);
        return strDate;
    }
}





