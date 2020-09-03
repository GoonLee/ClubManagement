package com.suri5.clubmngmt.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.Person;
import com.suri5.clubmngmt.Person.PersonAdapter_check;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class GroupMemberEditActivity extends Activity {

    //검색기능 구현해야함
    EditText editText_findperson;

    RecyclerView recyclerView;
    PersonAdapter_check personAdapter_check = new PersonAdapter_check();
    GroupDB groupDB;
    int pk = -1;

    ArrayList<Person> personlist;
    ArrayList<Person> personlist_n;
    ArrayList<Person> newList = new ArrayList<Person>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_editperson_pop_up);

        //DB
        groupDB = new GroupDB(new DatabaseHelper(this));
        pk = getIntent().getIntExtra("pk", -1);

        if (pk != -1) {
            personlist = getIntent().getParcelableArrayListExtra("pastList");
            for(Person p : personlist){
                p.setChecked(true);
            }
            personlist_n = getIntent().getParcelableArrayListExtra("pastList_n");

            personAdapter_check.setLists(personlist,personlist_n);
            newList.addAll(personlist); //단지 보여주기용
            newList.addAll(personlist_n);
        }

        else{
            Toast.makeText(getApplicationContext(),"그룹 인원을 불러오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
            finish();
        }

        //리사이클러뷰 생성
        recyclerView = findViewById(R.id.recyclerView_editmember);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personAdapter_check);
        personAdapter_check.setItems(newList);


        //수정된 리스트 전달.
        Button button_ok = findViewById(R.id.button_ok);
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.putParcelableArrayListExtra("newList", personlist);  //바뀌어 있는 그룹 리스트 전송
                intent.putParcelableArrayListExtra("newList_n",personlist_n);
                setResult(Constant.RESULT_OK_FROM_GROUPMEMBEREDITACTIVITY,intent);
                finish();
            }
        });



    }

}
