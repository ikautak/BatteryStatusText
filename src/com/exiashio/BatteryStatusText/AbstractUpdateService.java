package com.exiashio.BatteryStatusText;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public abstract class AbstractUpdateService extends Service {
    private static final boolean DEBUG = false;

    private int mBatteryLevel;
    private ComponentName mComponentName;
    private AppWidgetManager mAppWidgetManager;
    private PendingIntent mPendingIntent;

    /**
     * Get logcat tag string.
     * @return logcat tag.
     */
    abstract protected String getLogTag();

    /**
     * create ComponentName instance using widget class.
     * @return ComponentName.
     */
    abstract protected ComponentName createComponentName();

    /**
     * save service state each widget.
     * @param running service state.
     */
    abstract protected void setRunningState(Boolean running);

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DEBUG) Log.v(getLogTag(), "onReceive");
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED) == false) {
                // nothing to do.
                return;
            }

            mBatteryLevel = intent.getIntExtra("level", 0);

            // update text only.
            updateWidget(false);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        if (DEBUG) Log.v(getLogTag(), "onCreate");

        setRunningState(true);

        mComponentName = createComponentName();
        mAppWidgetManager = AppWidgetManager.getInstance(this);

        // receive ACTION_BATTERY_CHANGED.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);

        // set setting activity intent.
        Intent clickIntent = new Intent(this, BatteryStatusTextPreference.class);
        mPendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (DEBUG) Log.v(getLogTag(), "onStart");

        startFunction(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.v(getLogTag(), "onStartCommand");

        startFunction(intent);

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    private void startFunction(Intent intent) {
        // update all
        updateWidget(true);
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.v(getLogTag(), "onDestroy");

        unregisterReceiver(mBroadcastReceiver);

        setRunningState(false);
    }

    private void updateWidget(boolean updateAll) {
        if (DEBUG) Log.v(getLogTag(), "updateWidget updateAll:" + updateAll);

        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget);
        remoteViews.setOnClickPendingIntent(R.id.text, mPendingIntent);

        if (updateAll) {
            // update color
            int color = BatteryStatusTextPreference.getWidgetTextColor2(this);
            if (color == 0x01000000) {
                // assume old setting.
                color = BatteryStatusTextPreference.getWidgetTextColor(this);
            }
            if (DEBUG) Log.v(getLogTag(), "Color change:" + Integer.toHexString(color));
            remoteViews.setTextColor(R.id.text, color);

            // update size
            final int size = BatteryStatusTextPreference.getWidgetTextSize(this);
            if (DEBUG) Log.v(getLogTag(), "Size change:" + size);
            remoteViews.setFloat(R.id.text, "setTextSize", size);
        }

        // update text
        String text = BatteryStatusTextPreference.getWidgetText(this);
        if (text.length() <= 0) {
            text = "%level%";
        } else {
            text = text.replace("%level%", Integer.toString(mBatteryLevel));
        }
        remoteViews.setTextViewText(R.id.text, text);

        mAppWidgetManager.updateAppWidget(mComponentName, remoteViews);
    }
}
