package com.exiashio.BatteryStatusText;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class BatteryStatusTextPreference extends PreferenceActivity
        implements OnSharedPreferenceChangeListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "Preference";

    // preference
    private static final String TEXT_FORMAT = "text_format";
    private static final String TEXT_COLOR = "text_color";
    private static final String TEXT_SIZE = "text_size";
    private static final String DEFAULT_TEXT_FORMAT = "Battery %level%%";
    private static final String DEFAULT_TEXT_COLOR = "black";
    private static final String DEFAULT_TEXT_SIZE = "14";

    // view
    private EditTextPreference mTextFormat;
    private ListPreference mTextColor;
    private TextSizePreference mTextSize;

    // service state
    static boolean mRunning1x1 = false;
    static boolean mRunning1x2 = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        mTextFormat = (EditTextPreference)getPreferenceScreen().findPreference(TEXT_FORMAT);
        mTextFormat.setSummary(mTextFormat.getText());

        mTextColor = (ListPreference)getPreferenceScreen().findPreference(TEXT_COLOR);
        mTextColor.setSummary(mTextColor.getEntry());

        mTextSize = (TextSizePreference)getPreferenceScreen().findPreference(TEXT_SIZE);
        mTextSize.setSummary(PreferenceManager.getDefaultSharedPreferences(this).
                getString(TEXT_SIZE, DEFAULT_TEXT_SIZE));
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.v(TAG, "onStop");
        super.onStop();

        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);

        if (mRunning1x1) {
            Intent intent = new Intent(this, UpdateService1x1.class);
            startService(intent);
        }

        if (mRunning1x2) {
            Intent intent = new Intent(this, UpdateService1x2.class);
            startService(intent);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (DEBUG) Log.v(TAG, "onSharedPreferenceChanged");

        // update summary text
        if (key.equals(TEXT_FORMAT)) {
            mTextFormat.setSummary(mTextFormat.getText());
        } else if (key.equals(TEXT_COLOR)) {
            mTextColor.setSummary(mTextColor.getEntry());
        } else if (key.equals(TEXT_SIZE)) {
            mTextSize.setSummary(Integer.toString(getWidgetTextSize(this)));
        }
    }

    public static String getWidgetText(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getString(TEXT_FORMAT, DEFAULT_TEXT_FORMAT);
    }

    public static int getWidgetTextColor(Context context) {
        String c = PreferenceManager.getDefaultSharedPreferences(context).
                getString(TEXT_COLOR, DEFAULT_TEXT_COLOR);
        return Color.parseColor(c);
    }

    public static int getWidgetTextSize(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).
                getString(TEXT_SIZE, DEFAULT_TEXT_SIZE));
    }
}
