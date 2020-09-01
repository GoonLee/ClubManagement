package com.suri5.clubmngmt.Schedule;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

public class SetSchedule extends AppCompatActivity {
    EditText editTextSchedule,  editTextPlace, editTextComment;
    Button button,buttonDeleteSchedule;
    DatePicker datePickerS,datePickerE;
    TimePicker timePickerS, timePickerE;
    String yearS, yearE;
    String monthS, dayS, monthE, dayE;
    String hourS, minuteS, hourE, minuteE;
    String startDate, endDate, startTime, endTime;
    ScheduleDB scheduleDB;
    TextView textViewSetPlace;
    LinearLayout buttonDeleteScheduleLayout;
    boolean isEdit = false;
    int pk = 0;

    //구글맵 끝나면 주소 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            String str=data.getStringExtra("place");
            Toast.makeText(getApplicationContext(),"됨",Toast.LENGTH_SHORT).show();
            editTextPlace.setText(str);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //      기본 구현
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_schedule);
        scheduleDB = new ScheduleDB(new DatabaseHelper(getApplicationContext()));
        editTextSchedule=findViewById(R.id.editTextSchedule);
        editTextPlace=findViewById(R.id.editTextPlace);
        editTextComment=findViewById(R.id.editTextComment);
        button=findViewById(R.id.buttonAddSchedule);
        buttonDeleteSchedule=findViewById(R.id.buttonDeleteSchedule);

        //구글 맵 지도 키는 버튼
        textViewSetPlace=findViewById(R.id.textViewSetPlace);
        textViewSetPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent=new Intent(getApplicationContext(), GoogleMapActivity.class);
                startActivityForResult(mapIntent,101);
            }
        });

        datePickerS=(DatePicker)findViewById(R.id.DatePickerS);
        datePickerE=(DatePicker)findViewById(R.id.DatePickerE);

        timePickerS=(TimePicker)findViewById(R.id.timePickerS);
        timePickerE=(TimePicker)findViewById(R.id.timePickerE);

        //빌드 버전에 따른 분 00으로 초기화
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            timePickerS.setMinute(0);
            timePickerE.setMinute(0);
        }else{
            timePickerS.setCurrentMinute(0);
            timePickerE.setCurrentMinute(0);
        }

        //      수정하기 위해 넘어온 부분
        Intent intent = getIntent();
        if(intent.hasExtra("schedule")){
            Schedule needChange = (Schedule) intent.getParcelableExtra("schedule");
            isEdit = true;
            setActivity(needChange);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //년도 가져오기
                yearS=String.valueOf(datePickerS.getYear());
                yearE=String.valueOf(datePickerE.getYear());

                //월 가져오기
                if(datePickerS.getMonth()+1<10){
                    monthS="0"+(datePickerS.getMonth()+1);
                }else{
                    monthS=(datePickerS.getMonth()+1)+"";
                }
                if(datePickerE.getMonth()+1<10){
                    monthE="0"+(datePickerE.getMonth()+1);
                }else{
                    monthE=(datePickerE.getMonth()+1)+"";
                }

                //일 가져오기
                if(datePickerS.getDayOfMonth()<10){
                    dayS="0"+datePickerS.getDayOfMonth();
                }else{
                    dayS=datePickerS.getDayOfMonth()+"";
                }
                if(datePickerE.getDayOfMonth()<10){
                    dayE="0"+datePickerE.getDayOfMonth();
                }else{
                    dayE=datePickerE.getDayOfMonth()+"";
                }

                //빌드 버전에 따른 시간 얻어오기
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //시간 가져오기
                    if(timePickerS.getHour()<10){
                        hourS="0"+timePickerS.getHour();
                    }else{
                        hourS=timePickerS.getHour()+"";
                    }
                    if(timePickerE.getHour()<10){
                        hourE="0"+timePickerE.getHour();
                    }else{
                        hourE=timePickerE.getHour()+"";
                    }

                    //분 가져오기
                    if(timePickerS.getMinute()<10){
                        minuteS="0"+timePickerS.getMinute();
                    }else{
                        minuteS=timePickerS.getMinute()+"";
                    }
                    if(timePickerE.getMinute()<10){
                        minuteE="0"+timePickerE.getMinute();
                    }else{
                        minuteE=timePickerE.getMinute()+"";
                    }
                }else{
                    //시간 가져오기
                    if(timePickerS.getCurrentHour()<10){
                        hourS="0"+timePickerS.getCurrentHour();
                    }else{
                        hourS=timePickerS.getCurrentHour()+"";
                    }
                    if(timePickerE.getCurrentHour()<10){
                        hourE="0"+timePickerE.getCurrentHour();
                    }else{
                        hourE=timePickerE.getCurrentHour()+"";
                    }

                    //분 가져오기
                    if(timePickerE.getCurrentMinute()<10){
                        hourE="0"+timePickerE.getCurrentMinute();
                    }else{
                        hourE=timePickerE.getCurrentMinute()+"";
                    }
                    if(timePickerE.getCurrentMinute()<10){
                        minuteE="0"+timePickerE.getCurrentMinute();
                    }else{
                        minuteE=timePickerE.getCurrentMinute()+"";
                    }
                }
                startDate=yearS+monthS+dayS;
                startTime = hourS+minuteS;
                endDate=yearE+monthE+dayE;
                endTime = hourE+minuteE;

                if(Integer.parseInt(startDate)<=Integer.parseInt(endDate)&&Integer.parseInt(startTime)<=Integer.parseInt(endTime)){
                    Schedule tempSchedule = new Schedule(
                            pk,
                            editTextSchedule.getText().toString(),
                            startDate,
                            startTime,
                            endDate,
                            endTime,
                            editTextPlace.getText().toString(),
                            editTextComment.getText().toString()
                    );

                    if(isEdit == false){
                        scheduleDB.insertRecord(tempSchedule);
                    }
                    else{
                        scheduleDB.updateRecord(tempSchedule);
                    }
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"시간이 올바르지 않습니다",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //수정 부분 기존 세팅
    public void setActivity(final Schedule needChange){
        int startYear,startMonth, startDay, startHour, startMinute;
        int endYear,endMonth, endDay, endHour, endMinute;
        pk = needChange.getKey();
        startDate=needChange.startDate;
        startTime=needChange.startTime;
        endDate=needChange.endDate;
        endTime=needChange.endTime;

        buttonDeleteScheduleLayout=findViewById(R.id.buttonDeleteScheduleLayout);
        buttonDeleteScheduleLayout.setVisibility(View.VISIBLE);

        editTextSchedule.setText(needChange.title);
        editTextPlace.setText(needChange.place);
        editTextComment.setText(needChange.comment);

        startYear=Integer.parseInt(startDate.substring(0,4));
        startMonth=Integer.parseInt(startDate.substring(4,6));
        startDay=Integer.parseInt(startDate.substring(6,8));
        startHour=Integer.parseInt(startTime.substring(0,2));
        startMinute=Integer.parseInt(startTime.substring(2,4));

        endYear=Integer.parseInt(endDate.substring(0,4));
        endMonth=Integer.parseInt(endDate.substring(4,6));
        endDay=Integer.parseInt(endDate.substring(6,8));
        endHour=Integer.parseInt(endTime.substring(0,2));
        endMinute=Integer.parseInt(endTime.substring(2,4));

        datePickerS.init(startYear,startMonth-1,startDay,null);
        datePickerE.init(endYear,endMonth-1,endDay,null);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            timePickerS.setHour(startHour);
            timePickerS.setMinute(startMinute);

            timePickerE.setHour(endHour);
            timePickerE.setMinute(endMinute);
        }else{
            timePickerS.setCurrentHour(startHour);
            timePickerS.setCurrentMinute(startMinute);

            timePickerE.setCurrentHour(endHour);
            timePickerE.setCurrentMinute(endMinute);
        }

        //삭제 버튼 구현
        buttonDeleteSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleDB.deleteRecord(needChange);
                finish();
            }
        });

        button.setText("수정");
    }
}