package com.example.capstoneproject;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.capstoneproject.service.model.Article;
import com.example.capstoneproject.viewmodel.GetArticleListViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {
    static private DatabaseReference mDatabase;
    static private List<Article> articleList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);


    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Instantiate the RemoteViews object//

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);

//Update your app’s text, using the setTextViewText method of the RemoteViews class//


//        new getDetailsAsyncTask(views, appWidgetId, appWidgetManager).execute();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Article article = postSnapshot.getValue(Article.class);
                    articleList.add(article);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (!articleList.isEmpty())
        {
            views.setTextViewText(R.id.id_value, articleList.get(0).getArticleDescription());
        }
//        views.setTextViewText(R.id.id_value, String.valueOf(articleList.size()));

//Register the OnClickListener//

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://code.tutsplus.com/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.editWeight, pendingIntent);
//        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        //Update all instances of this widget//

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static class getDetailsAsyncTask extends AsyncTask<Context, Void, List<Article>> {
        private RemoteViews views;
        private int widgetID;
        private AppWidgetManager widgetManager;

        public getDetailsAsyncTask(RemoteViews views, int widgetID, AppWidgetManager widgetManager) {
            this.views = views;
            this.widgetID = widgetID;
            this.widgetManager = widgetManager;
        }

        @Override
        protected List<Article> doInBackground(Context... contexts) {
            final List<Article> articleList = new ArrayList<>();

            mDatabase = FirebaseDatabase.getInstance().getReference().child("ARTICLES");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Article article = postSnapshot.getValue(Article.class);
                        articleList.add(article);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            return articleList;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
            if (!articles.isEmpty()) {
                views.setTextViewText(R.id.id_value, articles.get(0).getArticleDescription());
            }
        }
    }
}

