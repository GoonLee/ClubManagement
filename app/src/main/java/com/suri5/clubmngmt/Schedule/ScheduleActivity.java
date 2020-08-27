package com.suri5.clubmngmt.Schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suri5.clubmngmt.R;


public class ScheduleActivity extends AppCompatActivity {
    public static final int RESULT_SCHEDULE_SAVE=103;
    CalendarView calendarView;
    TextView textView;
    int year;
    int month;
    int day;

    ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
    //ScheduleDB scheduleDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        textView=findViewById(R.id.textView);

        //scheduleDB = new ScheduleDB(new DatabaseHelper(this));
        //scheduleDB.createTable();


        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        calendarView=(CalendarView)findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                year =i;
                month=i1+1;
                day = i2;
                Intent intent = new Intent(getApplicationContext(), SchedulePopUp.class);
                intent.putExtra("month", month);
                intent.putExtra("day",day);
                startActivity(intent);
            }
        });

        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddSchedule.class);
                startActivityForResult(intent,RESULT_SCHEDULE_SAVE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_SCHEDULE_SAVE&&resultCode==RESULT_OK){
            String[] ScheduleInfor = data.getStringArrayExtra("ScheduleInfor");
            Schedule temp = new Schedule(0, ScheduleInfor[0], ScheduleInfor[1],ScheduleInfor[2],ScheduleInfor[3],ScheduleInfor[4]);
            Log.d("setData",ScheduleInfor[0]+"\n"+ScheduleInfor[1]+"\n"+ScheduleInfor[2]+"\n"+ScheduleInfor[3]+"\n"+ScheduleInfor[4]);
        }
    }
}