package com.example.capstoneproject;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.capstoneproject.R;
import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.view.activity.ProgressReadActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_ID;
import static android.appwidget.AppWidgetManager.INVALID_APPWIDGET_ID;

public class OurWidget extends AppWidgetProvider {
    static private DatabaseReference mDatabase;
    static private List<Article> articleList = new ArrayList<>();
    private static final String SYNC_CLICKED = "automaticWidgetSyncButtonClick";

    private PendingIntent pendingIntent;


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
        super.onReceive(context, intent);

        if (SYNC_CLICKED.equals(intent.getAction())) {

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews remoteViews;
            ComponentName watchWidget;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
            watchWidget = new ComponentName(context, OurWidget.class);
            remoteViews.setTextViewText(R.id.id_value, "TESTING");
            appWidgetManager.updateAppWidget(watchWidget, remoteViews);

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
            Log.e("intent-filter", "do nothing");
            AppWidgetManager appWidgetManager = AppWidgetManager
                    .getInstance(this);
            int incomingAppWidgetId = intent.getIntExtra(EXTRA_APPWIDGET_ID,
                    INVALID_APPWIDGET_ID);
            if (incomingAppWidgetId != INVALID_APPWIDGET_ID) {
                Log.e("intent-filter", "do nothing");
                updateOneAppWidget(appWidgetManager,incomingAppWidgetId);
            }
        }

        /**
         * For the random passcode app widget with the provided ID, updates its
         * display with a new passcode, and registers click handling for its
         * buttons.
         */
        private void updateOneAppWidget(AppWidgetManager appWidgetManager,
                                        int appWidgetId) {
            RemoteViews views = new RemoteViews(this.getPackageName(),
                    R.layout.new_app_widget);

            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES");

                Log.e("intent-filter", "do nothing");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Article article = postSnapshot.getValue(Article.class);
                            articleList.add(article);
                        }
                        if (!articleList.isEmpty()) {
                            views.setTextViewText(R.id.id_value, articleList.get(0).getArticleDescription());
                           appWidgetManager.updateAppWidget(appWidgetId,views);
                        }
                     //   appWidgetManager.updateAppWidget(appWidgetId, views);//4. Update the App Widget to reflect the changes

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        private void getValuesFromDataBase(UpdateWidgetService updateService) {
            Log.e("VALUES","YOYOYOYOYOYO");
            MutableLiveData<String> data= new MutableLiveData<>();
            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES");

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Article article = postSnapshot.getValue(Article.class);
                        articleList.add(article);
                    }
                    if (!articleList.isEmpty())
                    {
                        data.setValue( articleList.get(0).getArticleDescription());
                        Log.e("VALUES","LOLOLOLO");


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    data.setValue("NA");
                }
            });
            Log.e("VALUES",data.getValue());
        }

    }
}