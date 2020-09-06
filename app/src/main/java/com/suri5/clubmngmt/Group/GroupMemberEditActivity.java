package com.suri5.clubmngmt.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.Person;
import com.suri5.clubmngmt.Person.PersonAdapter_check;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupMemberEditActivity extends Activity {

    //검색기능 구현해야함
    AutoCompleteTextView editText_findperson;

    RecyclerView recyclerViewup;
    RecyclerView recyclerViewdown;
    PersonAdapter_check personAdapter_check_up = new PersonAdapter_check();
    PersonAdapter_check personAdapter_check_down = new PersonAdapter_check();
    GroupDB groupDB;
    int pk = -1;

    ArrayList<Person> personlist;
    ArrayList<Person> personlist_n;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_group_editperson_pop_up);

        //DB
        groupDB = new GroupDB(new DatabaseHelper(this));
        pk = getIntent().getIntExtra("pk", -1);
        // 자동완성 설정
        editText_findperson = findViewById(R.id.editText_findPerson);
        editText_findperson.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, groupDB.getAllMembersName()));

        try{
            personlist = getIntent().getParcelableArrayListExtra("pastList");
            personAdapter_check_up.setItems(personlist);
            personlist_n = getIntent().getParcelableArrayListExtra("pastList_n");
            personAdapter_check_down.setItems(personlist_n);

        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"그룹 인원을 불러오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
            finish();
        }


        //리사이클러뷰 생성
        recyclerViewup = findViewById(R.id.recyclerView_checkedmember);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewup.setLayoutManager(layoutManager);
        recyclerViewup.setAdapter(personAdapter_check_up);

        //리사이클러뷰 생성
        recyclerViewdown = findViewById(R.id.recyclerView_uncheckedmember);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewdown.setLayoutManager(layoutManager2);
        recyclerViewdown.setAdapter(personAdapter_check_down);

        ImageButton button_up = findViewById(R.id.button_up);
        button_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_check(personAdapter_check_down.getItems());
                personAdapter_check_down.notifyDataSetChanged();

                personlist.addAll(personAdapter_check_down.getCheckedlist());
                personAdapter_check_up.notifyDataSetChanged();



                personAdapter_check_down.setCheckedlist();

            }
        });

        ImageButton button_down = findViewById(R.id.button_down);
        button_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //체크 안된애 삭제
                check_check(personAdapter_check_up.getItems());
                personAdapter_check_up.notifyDataSetChanged();

                //체크된애 추가
                personlist_n.addAll(personAdapter_check_up.getCheckedlist());
                personAdapter_check_down.notifyDataSetChanged();

                //체크리스트도 비워주기
                personAdapter_check_up.setCheckedlist();

            }
        });


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

        //검색
        Button button_search = findViewById(R.id.button_searchPerson);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    //체크표시 되어있는애들 제거하기
    public void check_check(ArrayList<Person> checklist){
        int size = checklist.size();
        int index = 0;

        for(int i=0; i<size; i++){
            println("--"+checklist.get(index).getName() + " "+checklist.get(index).getChecked());
            if(checklist.get(index).getChecked()){
                checklist.get(index).setChecked(false);
                checklist.remove(index--);
            }
            index++;
        }

    }

}
