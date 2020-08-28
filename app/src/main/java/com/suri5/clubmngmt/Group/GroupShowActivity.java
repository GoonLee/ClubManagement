package com.suri5.clubmngmt.Group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

public class GroupShowActivity extends AppCompatActivity {
    public static final int RESULT_SAVE = 102;
    RecyclerView recyclerView;
    GroupAdapter groupAdapter = new GroupAdapter();
    GroupDB groupDB;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage);


        //인원목록 나타낼 리사이클러뷰 생성
        recyclerView = findViewById(R.id.recyclerView_group);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        //Insert into DB
        groupDB = new GroupDB(new DatabaseHelper(this));
        groupDB.createTable();
        groupAdapter.setItems(groupDB.lookupGroup());
        groupAdapter.notifyDataSetChanged();

        Button button_addGroup = findViewById(R.id.button_addgroup);
        button_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addGroup_info.class);
                startActivityForResult(intent, RESULT_SAVE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("GroupManageActivity", "onAc");

        if (requestCode == RESULT_SAVE && resultCode == RESULT_OK) {

            /*Group g = (Group)data.getParcelableExtra("group");
            if(g!= null){
            groupAdapter.addItem(g);
            groupAdapter.notifyDataSetChanged();
            }*/

            groupAdapter.setItems(groupDB.lookupGroup());
            groupAdapter.notifyDataSetChanged();
        }
    }
}
