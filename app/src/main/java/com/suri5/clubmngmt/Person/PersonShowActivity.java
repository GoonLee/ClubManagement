package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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
    PersonAdapter personAdapter = new PersonAdapter();
    PersonDB personDB;
    EditText editText;
    FloatingActionButton floatingActionButtonPerson;
    SearchHandler handler;

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personAdapter);

        personDB = new PersonDB(new DatabaseHelper(getApplicationContext()));
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
        handler = new SearchHandler(Looper.getMainLooper());
        editText = findViewById(R.id.search_people);
        editText.addTextChangedListener(new TextWatcher() {
            Thread thread;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(thread!=null&&thread.isAlive()){
                    thread.interrupt();
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                final String data = charSequence.toString();
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            //Todo: Set proper timing
                            Thread.sleep(150);
                            ArrayList<Person> found = personDB.findMember(Constant.PERSON_COLUMN_NAME,data);
                            Message msg = handler.obtainMessage();
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("found",found);
                            msg.setData(bundle);

                            handler.sendMessage(msg);
                        } catch (InterruptedException e){
                            //e.printStackTrace();
                        } finally{
                            ;
                        }
                    }
                });
                thread.start();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

    class SearchHandler extends Handler{
        public SearchHandler(@NonNull Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Bundle bundle = msg.getData();
            ArrayList found = bundle.getParcelableArrayList("found");
            personAdapter.setItems(found);
            personAdapter.notifyDataSetChanged();
        }
    }
}
