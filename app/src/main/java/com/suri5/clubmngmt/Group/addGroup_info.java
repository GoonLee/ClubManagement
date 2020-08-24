package com.suri5.clubmngmt.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class addGroup_info extends Activity {
    EditText editText_name, editText_findperson;
    TextView textView_number;
    int totalNum = 0;
    GroupDB groupDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_group_pop_up);
        editText_name = findViewById(R.id.editText_name);
        editText_findperson = findViewById(R.id.editText_findPerson);
        textView_number = findViewById(R.id.textView_number);
        groupDB = new GroupDB(new DatabaseHelper(this));

        //인원 검색해서 그룹에 넣을거에 추가하기. ArrayList에 미리 PersonKey + 그룹Key를 넣어야 할까
        Button button_addPerson = findViewById(R.id.button_addPerson);
        button_addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalNum++;
                textView_number.setText(Integer.toString(totalNum));
            }
        });

        Button button_add = findViewById(R.id.button_addgroup);
        //totalNum은 인원 추가할때마다 하나씩 늘게
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Group g  = new Group();

                g.setName(editText_name.getText().toString());
                println(editText_name.toString());

                g.setTotalNum(totalNum);
                groupDB.insertRecord(g);

                /*그리고 이 그룹에 추가한 인원들 넣기 시작
                인원검색에서 이름, key 받아오기 -> 중간 DB에 하나씩 추가 (사람 KEY랑 그룹 KEY)
                */

                Intent intent=new Intent();
                //intent.putExtra("group",g);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

}
