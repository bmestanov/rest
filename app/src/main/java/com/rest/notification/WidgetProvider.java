package com.rest.notification;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.rest.R;
import com.rest.activity.WidgetActivity;

/**
 * Created on 15/02/2017
 */

public class WidgetProvider extends AppWidgetProvider {
    public static final String WIDGET_CLICKED = "com.rest.widgetprovider.WIDGET_CLICKED";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        remoteViews.setOnClickPendingIntent(R.id.widget_layout, getSelfPendingIntent(context));
        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(WIDGET_CLICKED)) {
            Intent widgetActivityIntent = new Intent(context, WidgetActivity.class);
            widgetActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(widgetActivityIntent);
        }
    }

    private PendingIntent getSelfPendingIntent(Context context) {
        Intent intent = new Intent(context, WidgetProvider.class);
        intent.setAction(WIDGET_CLICKED);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
