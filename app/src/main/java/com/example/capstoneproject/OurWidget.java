package com.example.capstoneproject;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

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

// Created by akshata on 18/7/19.
public class OurWidget extends AppWidgetProvider {
    static private DatabaseReference mDatabase;
    static private List<Article> articleList = new ArrayList<>();


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
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);

        Intent intentOnClick = new Intent(context, ProgressReadActivity.class);
        intentOnClick.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        intentOnClick.setAction("APPWIDGET_ONCLICK_UPDATE");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentOnClick, 0);
        views.setOnClickPendingIntent(R.id.editWeight,pendingIntent);
        context.startService(intentOnClick);

        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putExtra(EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setAction("APPWIDGET_REGULAR_UPDATE");
        context.startService(intent);

    }

    /**
     * Called in response to the ACTION_APPWIDGET_UPDATE broadcast when this
     * AppWidget provider is being asked to provide RemoteViews for a set of
     * AppWidgets. Override this method to implement your own AppWidget
     * functionality.
     */



    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        RemoteViews views = new RemoteViews(context.getPackageName(),
                R.layout.new_app_widget);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES");

        if(intent.getAction().equals("APPWIDGET_REGULAR_UPDATE")){
            Log.e("intent-filter","do nothing");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Article article = postSnapshot.getValue(Article.class);
                        articleList.add(article);
                    }
                    if (!articleList.isEmpty())
                    {
                        views.setTextViewText(R.id.id_value, articleList.get(0).getArticleDescription());
                    }
//                    appWidgetManager.updateAppWidget(appWidgetId, views);//4. Update the App Widget to reflect the changes
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else if (intent.getAction().equals("APPWIDGET_ONCLICK_UPDATE")){
            Log.e("intent-filter","APPWIDGET_UPDATE");

        }
        else {
            Log.e("intent-filter","ACTION_NOT_FOUND");
        }
    }

    /**
     * static class does not need instantiation UpdateWidgetService is a Service
     * that identifies the App Widgets , instantiates AppWidgetManager and calls
     * updateAppWidget() to update the widget values
     */
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
                updateOneAppWidget(appWidgetManager, incomingAppWidgetId,
                        intent.getAction());
            }
        }
        /**
         * For the random passcode app widget with the provided ID, updates its
         * display with a new passcode, and registers click handling for its
         * buttons.
         */
        private void updateOneAppWidget(AppWidgetManager appWidgetManager,
                                        int appWidgetId, String action) {
            RemoteViews views = new RemoteViews(this.getPackageName(),
                    R.layout.new_app_widget);

            mDatabase = FirebaseDatabase.getInstance().getReference();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES");

            if(action.equals("APPWIDGET_REGULAR_UPDATE")){
                Log.e("intent-filter","do nothing");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Article article = postSnapshot.getValue(Article.class);
                            articleList.add(article);
                        }
                        if (!articleList.isEmpty())
                        {
                            views.setTextViewText(R.id.id_value, articleList.get(0).getArticleDescription());
                        }
                        appWidgetManager.updateAppWidget(appWidgetId, views);//4. Update the App Widget to reflect the changes
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            else if (action.equals("APPWIDGET_ONCLICK_UPDATE")){
                Log.e("intent-filter","APPWIDGET_UPDATE");

            }
            else {
                Log.e("intent-filter","ACTION_NOT_FOUND");
            }
        }
    }
}
