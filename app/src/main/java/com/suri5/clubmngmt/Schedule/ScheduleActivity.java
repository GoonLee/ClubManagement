package com.suri5.clubmngmt.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;


public class ScheduleActivity extends AppCompatActivity {
    public static final int RESULT_SCHEDULE_SAVE=103;
    CalendarView calendarView;
    TextView textView;
    String year;
    String month;
    String day;
    ScheduleDB scheduleDB;
    ScheduleAdapter scheduleAdapter = new ScheduleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        textView=findViewById(R.id.textView);

        scheduleDB = new ScheduleDB(new DatabaseHelper(this));
        scheduleDB.createTable();


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                year =Integer.toString(i);
                month= Integer.toString(i1+1);
                if(month.length() < 2){
                    month = "0"+month;
                }
                day = Integer.toString(i2);
                if(day.length()<2){
                    day = "0"+day;
                }
                Intent intent = new Intent(getApplicationContext(), SchedulePopUp.class);
                intent.putExtra("date",year+month+day);
                startActivity(intent);
            }
        });

        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SetSchedule.class);
                startActivityForResult(intent,RESULT_SCHEDULE_SAVE);
            }
        });
    }

    //인텐트 반응
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_SCHEDULE_SAVE&&resultCode==RESULT_OK){
        }
    }
}