package com.example.capstoneproject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.capstoneproject.util.Util;
import com.example.capstoneproject.view.activity.ProgressReadActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class OurWidget extends AppWidgetProvider {
    static private DatabaseReference mDatabase;
    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";

    private PendingIntent pendingIntent;
    private static FirebaseAuth auth;
    static String weightForToday = "";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        int appWidgetId = INVALID_APPWIDGET_ID;
        if (appWidgetIds != null) {
            int N = appWidgetIds.length;
            if (N == 1) {
                appWidgetId = appWidgetIds[0];
            }
        }
        RemoteViews remoteViews;
        ComponentName watchWidget;

        remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        watchWidget = new ComponentName(context, OurWidget.class);

        remoteViews.setOnClickPendingIntent(R.id.editWeight, getPendingSelfIntent(context, SYNC_CLICKED));
        remoteViews.setTextViewText(R.id.id_date, Util.getTodayDateInString());
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        final Intent i = new Intent(context, UpdateWidgetService.class);
        i.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        if (pendingIntent == null) {
            pendingIntent = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 60000, pendingIntent);

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


        if (SYNC_CLICKED.equals(intent.getAction())) {
            if (auth != null) {

                Intent intentOne = new Intent(context, ProgressReadActivity.class);
                intentOne.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (!TextUtils.isEmpty(weightForToday)) {
                    intentOne.putExtra(context.getResources().getString(R.string.weight_key), weightForToday);
                }
                context.startActivity(intentOne);
            } else {
                Toast.makeText(context, context.getString(R.string.updateWeightErrorMessage), Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }


    public static class UpdateWidgetService extends IntentService {
        public UpdateWidgetService() {
            super("UpdateWidgetService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(this);
            int incomingAppWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);
            if (incomingAppWidgetId != INVALID_APPWIDGET_ID) {
                updateOneAppWidget(appWidgetManager, incomingAppWidgetId);
            }
        }

        private void updateOneAppWidget(AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
            RemoteViews views = new RemoteViews(this.getPackageName(),
                    R.layout.new_app_widget);

            auth = FirebaseAuth.getInstance();

            String userIdChild = "";
            if (auth.getCurrentUser() != null) {
                userIdChild = auth.getCurrentUser().getUid();
            }
            mDatabase = FirebaseDatabase.getInstance().getReference().child(getString(R.string.user_progress_child)).child(userIdChild);

            String toDate = Util.getTodayDateInString();
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                        Map<String, Map<String, String>> dateKey = (Map<String, Map<String, String>>) dataSnapshot.getValue();

                        if (dateKey.containsKey(toDate)) {
                            Map<String, String> weightMap = dateKey.get(toDate);
                            weightForToday = weightMap.get(getString(R.string.weight_key));

                            if (!TextUtils.isEmpty(weightForToday)) {
                                views.setTextViewText(R.id.id_value, weightForToday);
                                appWidgetManager.updateAppWidget(appWidgetId, views);
                            }
                        } else {
                            views.setTextViewText(R.id.id_value, getString(R.string.no_data));
                            appWidgetManager.updateAppWidget(appWidgetId, views);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    views.setTextViewText(R.id.id_value, getString(R.string.no_data));
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                }

            });
        }
    }
}