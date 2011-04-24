package com.exiashio.BatteryStatusText;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class AbstractWidgetProvider extends AppWidgetProvider {
    private static final boolean DEBUG = false;

    /**
     * Get logcat tag string.
     * @return logcat tag.
     */
    abstract protected String getLogTag();

    /**
     * Create intent that start service corresponding widget.
     * @param context widget provider context.
     * @return intent.
     */
    abstract protected Intent createServiceIntent(Context context);

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        if (DEBUG) Log.v(getLogTag(), "onUpdate");

        context.startService(createServiceIntent(context));
    }

    @Override
    public void onDisabled(Context context) {
        if (DEBUG) Log.v(getLogTag(), "onDisabled");

        context.stopService(createServiceIntent(context));
    }
}

