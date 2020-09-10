package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Group.GroupEditActivity;
import com.suri5.clubmngmt.Group.GroupShowActivity;
import com.suri5.clubmngmt.R;
import com.suri5.clubmngmt.Schedule.ScheduleActivity;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.Constant.RESULT_SAVE;

public class PersonShowActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinner;
    AutoCompleteTextView editText;
    PersonAdapter personAdapter = new PersonAdapter();
    PersonDB personDB;
    ArrayList<String> names;
    ArrayList<String> idNums;
    ArrayList<String> majors;
    FloatingActionButton floatingActionButtonPerson;

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_show);

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
                        Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                        startActivity(intent);
                        break;
                }

                drawerLayout.closeDrawer(navigationView); //아이템 선택후 네비게이션뷰 닫힘
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerviewPerson);
        editText = findViewById(R.id.editText);
        spinner = findViewById(R.id.spinner);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personAdapter);

        personDB = new PersonDB(new DatabaseHelper(getApplicationContext()));
        names = personDB.getAllMembersName();
        idNums = personDB.getAllMembersIdNum();
        majors = personDB.getAllMembersMajor();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:{
                        editText.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, names));
                        break;
                    }
                    case 1:{
                        editText.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, idNums));
                        break;
                    }
                    case 2:{
                        editText.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, majors));
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        personAdapter.setItems(personDB.lookUpMember());
        personAdapter.notifyDataSetChanged();

        //추가
        floatingActionButtonPerson=findViewById(R.id.button_OK);
        floatingActionButtonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonEditActivity.class);
                startActivityForResult(intent, RESULT_SAVE);
            }
        });

        ImageButton button_search = findViewById(R.id.button3);
        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (spinner.getSelectedItemPosition()){
                    case 0: {
                        personAdapter.setItems(personDB.findMember(Constant.PERSON_COLUMN_NAME, editText.getText().toString()));
                        break;
                    }
                    case 1: {
                        personAdapter.setItems(personDB.findMember(Constant.PERSON_COLUMN_IDNUM, editText.getText().toString()));
                        break;
                    }
                    case 2: {
                        personAdapter.setItems(personDB.findMember(Constant.PERSON_COLUMN_MAJOR, editText.getText().toString()));
                        break;
                    }
                }
                personAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("GroupManageActivity", "onAc");

        if (resultCode == RESULT_OK) {

            personAdapter.setItems(personDB.lookUpMember());
            personAdapter.notifyDataSetChanged();
        }
    }
}
