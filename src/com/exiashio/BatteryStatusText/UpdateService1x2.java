package com.exiashio.BatteryStatusText;

import android.content.ComponentName;

public class UpdateService1x2 extends AbstractUpdateService {

    @Override
    protected String getLogTag() {
        return "UpdateService1x2";
    }

    @Override
    protected ComponentName createComponentName() {
        return new ComponentName(this, WidgetProvider1x2.class);
    }

    @Override
    protected void setRunningState(Boolean running) {
        // inform status to setting activity.
        BatteryStatusTextPreference.mRunning1x2 = running;
    }
}
