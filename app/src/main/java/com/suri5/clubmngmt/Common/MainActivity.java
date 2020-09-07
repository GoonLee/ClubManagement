package com.suri5.clubmngmt.Common;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.suri5.clubmngmt.Group.GroupShowActivity;
import com.suri5.clubmngmt.Person.PersonDB;
import com.suri5.clubmngmt.Person.PersonShowActivity;
import com.suri5.clubmngmt.R;
import com.suri5.clubmngmt.Schedule.ScheduleActivity;

public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    NavigationView navigationView; //사이드 메뉴
    public ActionBarDrawerToggle drawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView)findViewById(R.id.sideMenu);
        //테이블 미리 다 만들어놓기
        PersonDB pd = new PersonDB(new DatabaseHelper(getApplicationContext()));
        pd.createTable();

        drawerLayout=findViewById(R.id.drawer);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ClubActivity.class);
                startActivity(intent);
            }
        });

        //네비게이션뷰 아이템 클릭 리스너
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){//각 아이템 클릭에 대한 반응
                    case R.id.menu_first:
                        Intent personIntent=new Intent(getApplicationContext(), PersonShowActivity.class);
                        startActivity(personIntent);
                        break;
                    case R.id.menu_first_2:
                        Intent groupIntent = new Intent(getApplicationContext(), GroupShowActivity.class);
                        startActivity(groupIntent);
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

        //메인화면 카드뷰
        CardView cardViewPeople = findViewById(R.id.cardViewPeople);
        CardView cardViewBudget = findViewById(R.id.cardViewBudget);
        CardView cardViewSchedule = findViewById(R.id.cardViewSchedule);

        //각 카드뷰 클릭 리스너
        cardViewPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent personIntent=new Intent(getApplicationContext(),PersonShowActivity.class);
                startActivity(personIntent);
            }
        });
        cardViewBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        cardViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
            }
        });

    }
}