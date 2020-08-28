package com.suri5.clubmngmt.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupEditActivity extends Activity {
    EditText editText_name, editText_findperson;
    TextView textView_number,textView_namelist;
    int totalNum = 0;
    GroupDB groupDB;
    //해당 그룹원 이름 출력용
    ArrayList<String> members;
    StringBuilder member = new StringBuilder();
    Group g;
    int pk = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_group_pop_up);
        editText_name = findViewById(R.id.editText_name);
        editText_findperson = findViewById(R.id.editText_findPerson);
        textView_number = findViewById(R.id.textView_number);
        textView_namelist = findViewById(R.id.textView_namelist);
        groupDB = new GroupDB(new DatabaseHelper(this));

        g = getIntent().getParcelableExtra("group");
        pk = g.getKey();

        //정상적으로 옴
        if(pk != -1){
            editText_name.setText(g.getName());


            members = groupDB.findGroupmember(g.getKey());
            for(int i=0; i<members.size(); i++){
                member.append(members.get(i) + " ");
            }
            textView_namelist.setText(member);
            totalNum = members.size();
            textView_number.setText(Integer.toString(totalNum));
        }
        else if(g != null){
            g = new Group();
        }

        //그룹에 사람 추가하는법 구현해야함
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

                g.setName(editText_name.getText().toString());
                println(editText_name.toString());
                g.setTotalNum(totalNum);

                if(pk != -1){
                    groupDB.updateRecord(g);
                }
                else{
                    groupDB.insertRecord(g);
                }

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
