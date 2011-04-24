package com.exiashio.BatteryStatusText;

import android.content.Context;
import android.content.Intent;

public class WidgetProvider1x2 extends AbstractWidgetProvider {
    static private Intent mIntent;

    @Override
    protected String getLogTag() {
        return "Widget1x2";
    }

    @Override
    protected Intent createServiceIntent(Context context) {
        if (mIntent == null) {
            mIntent = new Intent(context, UpdateService1x2.class);
        }
        return mIntent;
    }
}
