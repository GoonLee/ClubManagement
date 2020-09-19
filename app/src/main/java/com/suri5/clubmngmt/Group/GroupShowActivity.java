package com.suri5.clubmngmt.Group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.PersonEditActivity;
import com.suri5.clubmngmt.Person.PersonShowActivity;
import com.suri5.clubmngmt.R;
import com.suri5.clubmngmt.Schedule.ScheduleActivity;

public class GroupShowActivity extends AppCompatActivity {
    public static final int RESULT_SAVE = 102;
    AutoCompleteTextView editText;
    RecyclerView recyclerView;
    GroupAdapter groupAdapter = new GroupAdapter();
    GroupDB groupDB;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage);

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
                        break;
                    case R.id.menu_second:
                        break;
                    case R.id.menu_third:
                        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                        startActivity(intent);
                        break;
                }

                drawerLayout.closeDrawer(navigationView); //아이템 선택후 네비게이션뷰 닫힘
                return false;
            }
        });

        //인원목록 나타낼 리사이클러뷰 생성
        recyclerView = findViewById(R.id.recyclerView_group);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter);

        //Insert into DB
        groupDB = new GroupDB(new DatabaseHelper(this));
        editText = findViewById(R.id.editText_searchgroup);
        editText.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,groupDB.getAllGroupsName()));

        groupAdapter.setItems(groupDB.lookupGroup());
        groupAdapter.notifyDataSetChanged();

        //인원 추가 버튼
        FloatingActionButton button_addGroup = findViewById(R.id.button_addgroup);
        button_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GroupEditActivity.class);
                startActivityForResult(intent, RESULT_SAVE);
            }
        });
        //검색 버튼
        Button button_search = findViewById(R.id.button);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupAdapter.setItems(groupDB.findGroups(editText.getText().toString()));
                groupAdapter.notifyDataSetChanged();
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
