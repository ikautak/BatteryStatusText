package com.exiashio.BatteryStatusText;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class TextSizePreference extends DialogPreference
        implements SeekBar.OnSeekBarChangeListener {
    private SeekBar mSeekBar;
    private TextView mText;
    private String mSampleText;

    private static final int MIN_TEXT_SIZE = 10;
    private static final int MAX_TEXT_SIZE = 50;

    public TextSizePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.size_preference_dialog);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        int current = BatteryStatusTextPreference.getWidgetTextSize(getContext());

        mSampleText = view.getResources().getString(R.string.setting_text_size_sample);
        mText = (TextView)view.findViewById(R.id.sample_text);
        mText.setTextSize(current);
        mText.setText(mSampleText + " " + current);

        mSeekBar = (SeekBar)view.findViewById(R.id.seekbar);
        mSeekBar.setMax(MAX_TEXT_SIZE - MIN_TEXT_SIZE);
        mSeekBar.setProgress(current - MIN_TEXT_SIZE);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromTouch) {
        int size = progress + MIN_TEXT_SIZE;
        mText.setText(mSampleText + " " + size);
        mText.setTextSize(size);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // NA
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // NA
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            persistString(Integer.toString(mSeekBar.getProgress() + MIN_TEXT_SIZE));
        }
    }
}
