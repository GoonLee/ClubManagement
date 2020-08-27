package com.suri5.clubmngmt.Schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class SchedulePopUp extends Activity {

    ScheduleDB scheduleDB;
    StringBuilder message = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_pop_up);
        //고뇌포인트 : 애초에 Intent로 이날에 해당하는 모든 스케쥴리스트를 가져올까, 아니면 여기서 가져올까.
        Intent intent = getIntent();
        String date = intent.getStringExtra("date");

        scheduleDB = new ScheduleDB(new DatabaseHelper(getApplicationContext()));
        ArrayList<Schedule> items = scheduleDB.getSchedule(date);

        TextView textView = findViewById(R.id.textView);
        message.append(date+" 의 일정\n");

        if(items != null){
            for(int i=0; i<items.size(); i++){
                Schedule schedule = items.get(i);
                message.append(schedule.getTitle()+ " " + schedule.getStartDate()+schedule.getStartTime());
                message.append("~"+schedule.getEndDate()+schedule.getEndTime()+"\n");
            }
        }
        textView.setText(message);
    }
}