package com.suri5.clubmngmt.Schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.suri5.clubmngmt.R;

public class SchedulePopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_pop_up);

        Intent intent = getIntent();
        int month = intent.getIntExtra("month", 1);
        int day = intent.getIntExtra("day", 1);

        TextView textView = findViewById(R.id.textView);
        textView.setText(String.valueOf(month)+"-"+String.valueOf(day)+" 일정");
    }
}