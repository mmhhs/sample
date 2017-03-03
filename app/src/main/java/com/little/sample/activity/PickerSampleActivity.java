package com.little.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.little.picker.DatePickerPopup;
import com.little.picture.util.ToastUtil;
import com.little.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PickerSampleActivity extends Activity {

    @InjectView(R.id.activity_picker_sample_date)
    TextView activityPickerSampleDate;
    @InjectView(R.id.activity_picker_sample_hour)
    TextView activityPickerSampleHour;
    private DatePickerPopup datePickerPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker_sample);
        ButterKnife.inject(this);
    }

    DatePickerPopup.OnDatePickedListener onDatePickedListener = new DatePickerPopup.OnDatePickedListener() {
        @Override
        public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
            ToastUtil.addToast(PickerSampleActivity.this,""+dateDesc);
        }

        @Override
        public void onDatePickCompleted(int year, int month, int day, String hour, String dateDesc) {
            ToastUtil.addToast(PickerSampleActivity.this,""+dateDesc+" "+hour);
            datePickerPopup.dismissPopWin();
        }
    };

    @OnClick({R.id.activity_picker_sample_date, R.id.activity_picker_sample_hour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_picker_sample_date:
                datePickerPopup = new DatePickerPopup.Builder(PickerSampleActivity.this, onDatePickedListener)
                        .btnTextSize(16)
                        .viewTextSize(24)
                        .minYear(1950)
                        .maxYear(2017)
                        .autoDismiss(true)
                        .build();
                datePickerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
            case R.id.activity_picker_sample_hour:
                datePickerPopup = new DatePickerPopup.Builder(PickerSampleActivity.this, onDatePickedListener)
                        .btnTextSize(16)
                        .viewTextSize(24)
                        .minYear(1950)
                        .maxYear(2017)
                        .showHour(true)
                        .autoDismiss(false)
                        .build();
                datePickerPopup.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
        }
    }
}
