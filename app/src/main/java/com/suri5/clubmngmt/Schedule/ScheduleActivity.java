package com.suri5.clubmngmt.Schedule;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Group.GroupEditActivity;
import com.suri5.clubmngmt.Group.GroupShowActivity;
import com.suri5.clubmngmt.Person.PersonEditActivity;
import com.suri5.clubmngmt.Person.PersonShowActivity;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;
import java.util.Calendar;


public class ScheduleActivity extends AppCompatActivity implements OnDateSelectedListener {

    public static final int RESULT_SCHEDULE_SAVE = 103;
    MaterialCalendarView calendarView;
    TextView textView;
    ScheduleAdapter adapter;
    ScheduleDB scheduleDB;
    ScheduleAdapter scheduleAdapter = new ScheduleAdapter();
    RecyclerView recyclerViewSchedule;
    String day;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    @Override
    protected void onResume() {//일정 내용 변경/추가/삭제 후 기존 창 새로고침
        super.onResume();

        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new DotDecorator()
        );

        ArrayList<Schedule> schedules = scheduleDB.getSchedule(day);
        if(schedules != null){
            adapter.setItems(schedules);
            recyclerViewSchedule.setAdapter(adapter);
        }
        else{
            recyclerViewSchedule.setAdapter(null);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        textView = findViewById(R.id.textView);

        scheduleDB = new ScheduleDB(new DatabaseHelper(this));

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.sideMenu);
        drawerLayout=findViewById(R.id.drawer);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        //네비게이션뷰 아이템 클릭 리스너
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){//각 아이템 클릭에 대한 반응
                    case R.id.personAdd:
                        Intent personAddIntent=new Intent(getApplicationContext(), PersonEditActivity.class);
                        startActivity(personAddIntent);
                        break;
                    case R.id.personShow:
                        Intent personShowIntent=new Intent(getApplicationContext(), PersonShowActivity.class);
                        startActivity(personShowIntent);
                        break;
                    case R.id.groupAdd:
                        Intent groupAddIntent = new Intent(getApplicationContext(), GroupEditActivity.class);
                        startActivity(groupAddIntent);
                        break;
                    case R.id.groupShow:
                        Intent groupShowIntent=new Intent(getApplicationContext(), GroupShowActivity.class);
                        startActivity(groupShowIntent);
                        break;
                    case R.id.menu_second:
                        break;
                    case R.id.menu_third:
                        break;
                }

                drawerLayout.closeDrawer(navigationView); //아이템 선택후 네비게이션뷰 닫힘
                return false;
            }
        });

        /**
         * 캘린더 기본 설정
         */
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(this);
        calendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019, 0, 1))
                .setMaximumDate(CalendarDay.from(2040,11,31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendarView.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                new DotDecorator()
        );

        /**
         * 해당 날짜 아래 스케줄 띄우는 리사이클러뷰 설정
         */
        recyclerViewSchedule=findViewById(R.id.recyclerviewSchedule);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewSchedule.setLayoutManager(layoutManager);
        adapter = new ScheduleAdapter();

        FloatingActionButton button = findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScheduleEditActivity.class);
                startActivityForResult(intent, RESULT_SCHEDULE_SAVE);
            }
        });
    }

    //인텐트 반응
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_SCHEDULE_SAVE && resultCode == RESULT_OK) {
        }
    }

    /**
     * 달력 해당 날짜 선택될 때
     */
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        day=Integer.toString(date.getYear());
        if(date.getMonth()+1<10){
            day+= '0';
        }
        day+= (Integer.toString(date.getMonth()+1));
        if(date.getDay()<10){
            day+='0';
        }
        day+=Integer.toString(date.getDay());
        //날짜20000101 형식으로 전달해서 해당 스케줄들 다 받아옴
        /**
         * 이 부분이 현재 저장해도 실행이 안되는중
         */
        ArrayList<Schedule> schedules = scheduleDB.getSchedule(day);
        if(schedules != null){
            adapter.setItems(schedules);
            recyclerViewSchedule.setAdapter(adapter);
        }
        else{
            recyclerViewSchedule.setAdapter(null);
        }
    }

    /**
     * 일정 있는 날짜 점 표시
     */
    private class DotDecorator implements DayViewDecorator{
        private final Calendar calendar=Calendar.getInstance();
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            String date=changeDayString(day.toString());

            return scheduleDB.getSchedule(date).size()>0; //일정이 1개라도 있는 날만 true
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5,Color.RED));
        }
    }

    /**
     * CalendarDay{2020-8-1} string 형식을 20200801로 변환하는 함수
     */
    private String changeDayString(String date) {
        date=date.substring(12,date.length()-1);
        String[] arrDay=date.split("-");

        //짜증나게 달만 0부터 시작하는 구조
        arrDay[1]=Integer.toString(Integer.parseInt(arrDay[1])+1);
        if(arrDay[1].length()==1){
            arrDay[1]="0"+arrDay[1];
        }
        if(arrDay[2].length()==1){
            arrDay[2]="0"+arrDay[2];
        }
        return arrDay[0]+arrDay[1]+arrDay[2];
    }

    /**
     * 주말 색깔 표시
     */
    private class SundayDecorator implements DayViewDecorator {
        private final Calendar calendar=Calendar.getInstance();
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay==Calendar.SUNDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.RED));
        }
    }

    private class SaturdayDecorator implements DayViewDecorator {
        private final Calendar calendar=Calendar.getInstance();
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekDay=calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay==Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }
}