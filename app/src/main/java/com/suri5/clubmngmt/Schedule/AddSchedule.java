package com.suri5.clubmngmt.Schedule;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.suri5.clubmngmt.Person.PersonShowActivity;
import com.suri5.clubmngmt.R;

public class AddSchedule extends AppCompatActivity {
    EditText editTextSchedule, editTextMonthS, editTextDayS, editTextMonthE, editTextDayE, editTextComment;
    Button button,button_setPlace;
    TimePicker timePickerS, timePickerE;
    String monthS, dayS, monthE, dayE;
    String hourS, minuteS, hourE, minuteE;

    //구글맵 끝나면 주소 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101 && resultCode==RESULT_OK){
            String str=data.getStringExtra("place");
            Toast.makeText(getApplicationContext(),"됨",Toast.LENGTH_SHORT).show();
            button_setPlace.setText(str);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        editTextSchedule=findViewById(R.id.editTextSchedule);

        //시작 월
        editTextMonthS=findViewById(R.id.editTextMonthS);
        editTextMonthS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().equals("")){
                    int month=Integer.parseInt(s.toString());
                    if(month>12){
                        Toast.makeText(getApplicationContext(), "잘못된 달입니다", Toast.LENGTH_SHORT).show();
                        editTextMonthS.setText("12");
                        month=12;
                    }
                    else if(month==0){
                        Toast.makeText(getApplicationContext(), "잘못된 달입니다", Toast.LENGTH_SHORT).show();
                        editTextMonthS.setText("1");
                        month=1;
                    }
                    if(month<10)
                        monthS="0"+String.valueOf(month);
                    else
                        monthS=String.valueOf(month);
                }
                editTextMonthE.setText(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextMonthS.setSelection(editTextMonthS.getText().length());
            }
        });

        //시작 일
        editTextDayS=findViewById(R.id.editTextDayS);
        editTextDayS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().equals("")){
                    int day=Integer.parseInt(s.toString());
                    if(day==0){
                        Toast.makeText(getApplicationContext(), "잘못된 날짜입니다", Toast.LENGTH_SHORT).show();
                        editTextDayS.setText("1");
                        day=1;
                    }
                    else if(day>31){
                        Toast.makeText(getApplicationContext(), "잘못된 날짜입니다", Toast.LENGTH_SHORT).show();
                        editTextDayS.setText("31");
                        day=31;
                    }
                    if(day<10)
                        dayS="0"+String.valueOf(day);
                    else
                        dayS=String.valueOf(day);
                }
                editTextDayE.setText(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextDayS.setSelection(editTextDayS.getText().length());
            }
        });

        //종료 달
        editTextMonthE=findViewById(R.id.editTextMonthE);
        editTextMonthE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().equals("")){
                    int month=Integer.parseInt(s.toString());
                    if(month>12){
                        Toast.makeText(getApplicationContext(), "잘못된 달입니다", Toast.LENGTH_SHORT).show();
                        editTextMonthE.setText("12");
                        month=12;
                    }
                    else if(month==0){
                        Toast.makeText(getApplicationContext(), "잘못된 달입니다", Toast.LENGTH_SHORT).show();
                        editTextMonthE.setText("1");
                        month=1;
                    }
                    if(month<10)
                        monthE="0"+String.valueOf(month);
                    else
                        monthE=String.valueOf(month);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextMonthE.setSelection(editTextMonthE.getText().length());
            }
        });

        //종료 일
        editTextDayE=findViewById(R.id.editTextDayE);
        editTextDayE.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if(!s.toString().equals("")){
                    int day=Integer.parseInt(s.toString());
                    if(day==0){
                        Toast.makeText(getApplicationContext(), "잘못된 날짜입니다", Toast.LENGTH_SHORT).show();
                        editTextDayE.setText("1");
                        day=1;
                    }
                    else if(day>31){
                        Toast.makeText(getApplicationContext(), "잘못된 날짜입니다", Toast.LENGTH_SHORT).show();
                        editTextDayE.setText("31");
                        day=31;
                    }
                    if(day<10)
                        dayE="0"+String.valueOf(day);
                    else
                        dayE=String.valueOf(day);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextDayE.setSelection(editTextDayE.getText().length());
            }
        });

        editTextComment=findViewById(R.id.editTextComment);

        //구글 맵 지도 키는 버튼
        button_setPlace=findViewById(R.id.button_setPlace);
        button_setPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent=new Intent(getApplicationContext(), GoogleMapActivity.class);
                startActivityForResult(mapIntent,101);
            }
        });


        timePickerS=(TimePicker)findViewById(R.id.timePickerS);
        timePickerE=(TimePicker)findViewById(R.id.timePickerE);

        //빌드 버전에 따른 시간 얻어오기
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            hourS=timePickerS.getHour()+"";
            minuteS=timePickerS.getMinute()+"";
            hourE=timePickerE.getHour()+"";
            minuteE=timePickerE.getMinute()+"";
        }else{
            hourS=timePickerS.getCurrentHour()+"";
            minuteS=timePickerS.getCurrentMinute()+"";
            hourE=timePickerE.getCurrentHour()+"";
            minuteE=timePickerE.getCurrentMinute()+"";
        }

        //시작 날짜의 세부 시간
        timePickerS.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                if(hour<10){
                    hourS="0"+String.valueOf(hour);
                }else{
                    hourS=String.valueOf(hour);
                }

                if(min<10){
                    minuteS="0"+String.valueOf(min);
                }else{
                    minuteS=String.valueOf(min);
                }
            }
        });

        //종료 날짜의 세부 시간
        timePickerE.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int min) {
                if(hour<10){
                    hourE="0"+String.valueOf(hour);
                }else{
                    hourE=String.valueOf(hour);
                }

                if(min<10){
                    minuteE="0"+String.valueOf(min);
                }else{
                    minuteE=String.valueOf(min);
                }
            }
        });

        button=findViewById(R.id.buttonAddSchedule);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] ScheduleInfor=new String[5];
                ScheduleInfor[0]=editTextSchedule.getText().toString();
                ScheduleInfor[1]=monthS+dayS;
                ScheduleInfor[2]=monthE+dayE;
                ScheduleInfor[3]=button_setPlace.getText().toString();
                ScheduleInfor[4]=editTextComment.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("ScheduleInfor",ScheduleInfor);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}