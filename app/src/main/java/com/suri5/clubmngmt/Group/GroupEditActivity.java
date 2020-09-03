package com.suri5.clubmngmt.Group;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.Person;
import com.suri5.clubmngmt.Person.PersonAdapter_short;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupEditActivity extends Activity {

    EditText editText_name;
    TextView textView_number;
    RecyclerView recyclerView;
    PersonAdapter_short personAdapter_short = new PersonAdapter_short();
    ArrayList<Person> personlist_short;
    ArrayList<Person> personlist_short_n;
    int totalNum = 0;
    GroupDB groupDB;

    StringBuilder member = new StringBuilder();

    Group g;
    int pk = -1;
    boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_group);

        editText_name = findViewById(R.id.editText_name);


        //인원목록 나타낼 리사이클러뷰 생성
        recyclerView = findViewById(R.id.recyclerView_person_summary);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personAdapter_short);

        textView_number = findViewById(R.id.textView_number);
        groupDB = new GroupDB(new DatabaseHelper(this));
        g = getIntent().getParcelableExtra("group");


        //사람 추가 버튼
        final Button button_addPerson = findViewById(R.id.button_addPerson);

        // 확인 버튼
        final Button button_add = findViewById(R.id.button_addgroup);

        //삭제 버튼
        final Button button_delete = findViewById(R.id.button_deletegroup);


        try{
            //인원 추가
            if(g == null){
                isEdit = true;
                g = new Group();
                personlist_short = new ArrayList<>();
                personlist_short_n = groupDB.lookUpMember();
            }

            //인원 수정
            else {
                pk = g.getKey();
                if (pk != -1) {

                     //첨엔 수정 못하게
                    button_addPerson.setEnabled(isEdit);
                    button_add.setText("수정");
                    editText_name.setEnabled(isEdit);
                    button_delete.setVisibility(View.VISIBLE);

                    editText_name.setText(g.getName());

                    personlist_short = groupDB.findGroupmember(pk);
                    personlist_short_n = groupDB.lookUpMemberExcept(pk);

                    personAdapter_short.setItems(personlist_short);
                    personAdapter_short.notifyDataSetChanged();
                    totalNum = g.getTotalNum();
                    textView_number.setText(Integer.toString(totalNum));
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"그룹을 불러오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
            finish();
        }


    //사람 추가하기
        button_addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupMemberEditActivity.class);
                intent.putExtra("pk", pk);

                intent.putParcelableArrayListExtra("pastList", personlist_short);
                intent.putParcelableArrayListExtra("pastList_n", personlist_short_n);
                startActivityForResult(intent, Constant.RESULT_SAVE);
            }
        });

        //확인 하기
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isEdit == false){
                    isEdit = true;
                    editText_name.setEnabled(isEdit);
                    button_addPerson.setEnabled(isEdit);
                }

                else{
                    g.setName(editText_name.getText().toString());
                    println(editText_name.toString());
                    g.setTotalNum(totalNum);

                    if(pk != -1){
                        groupDB.updateRecord(g);
                        //비우기
                        groupDB.deleteMemberAllGroup(pk);

                    }
                    else{
                       pk =  groupDB.insertRecord(g);
                    }

                    //새로 넣기
                    for (Person p: personlist_short) {
                        println(p.getName());
                        groupDB.insertMemberFromGroup(p.getPk(), pk);
                    }

                    Intent intent=new Intent();
                    //intent.putExtra("group",g);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
        });

    //삭제
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pk != -1){
                    groupDB.deleteRecord(g);
                    Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다.",Toast.LENGTH_LONG).show();

                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"그룹이 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("GroupEditActivity", "Result");

        if (requestCode == Constant.RESULT_SAVE && resultCode == Constant.RESULT_OK_FROM_GROUPMEMBEREDITACTIVITY) {

            personlist_short = data.getParcelableArrayListExtra("newList");
            personlist_short_n = data.getParcelableArrayListExtra("newList_n");
            personAdapter_short.setItems(personlist_short);
            totalNum = personlist_short.size();
            textView_number.setText(Integer.toString(totalNum));

            personAdapter_short.setIndex(0);

            personAdapter_short.notifyDataSetChanged();
        }
    }

}
