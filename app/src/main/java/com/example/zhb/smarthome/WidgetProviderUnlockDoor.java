package com.example.zhb.smarthome;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WidgetProviderUnlockDoor extends AppWidgetProvider {
    public static final String ACTION_UNLOCK = "action_unlock";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_unlock_door);
            Intent intent = new Intent(context, WidgetProviderUnlockDoor.class);
            intent.setAction(ACTION_UNLOCK);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btnDugmeWidget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_UNLOCK)) {
            Intent intent1 = new Intent(context,MyService.class);
            context.startService(intent1);
        }
    }
}