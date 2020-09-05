package com.suri5.clubmngmt.Schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class SchedulePopUp extends Activity {
    ScheduleDB scheduleDB;
    RecyclerView recyclerViewSchedule;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_schedule_pop_up);
        //고뇌포인트 : 애초에 Intent로 이날에 해당하는 모든 스케쥴리스트를 가져올까, 아니면 여기서 가져올까.

        recyclerViewSchedule=findViewById(R.id.recyclerviewSchedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewSchedule.setLayoutManager(layoutManager);
        ScheduleAdapter adapter = new ScheduleAdapter();

        Intent intent = getIntent();
        date = intent.getStringExtra("date");

        scheduleDB = new ScheduleDB(new DatabaseHelper(getApplicationContext()));
        //20200807 형태의 string을 인자로 삼아서 그날이 포함된 스케쥴을 arraylist로 가져오는 함수
        ArrayList<Schedule> items = scheduleDB.getSchedule(date);

        TextView textView = findViewById(R.id.textView);
        textView.setText(date+"의 일정");

        if(items != null){
            for(int i=0; i<items.size(); i++){
                adapter.addItem(items.get(i));
            }
        }
        recyclerViewSchedule.setAdapter(adapter);
    }

    //수정, 추가 후 다시 어댑터 만들어 새로고침
    @Override
    protected void onResume() {
        super.onResume();

        ScheduleAdapter adapter = new ScheduleAdapter();
        scheduleDB = new ScheduleDB(new DatabaseHelper(getApplicationContext()));
        ArrayList<Schedule> items = scheduleDB.getSchedule(date);

        if(items != null){
            for(int i=0; i<items.size(); i++){
                adapter.addItem(items.get(i));
            }
        }
        recyclerViewSchedule.setAdapter(adapter);
    }
}