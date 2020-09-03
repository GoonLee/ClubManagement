package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Group.GroupShowActivity;
import com.suri5.clubmngmt.R;

public class PersonShowActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinner;
    EditText editText;
    PersonAdapter personAdapter = new PersonAdapter();
    PersonDB personDB;
    BottomNavigationView bottomNavigationViewPerson;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_show);

        recyclerView = findViewById(R.id.recyclerviewPerson);
        editText = findViewById(R.id.editText);
        spinner = findViewById(R.id.spinner);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(personAdapter);

        personDB = new PersonDB(new DatabaseHelper(getApplicationContext()));
        personDB.createTable();

        personAdapter.setItems(personDB.lookUpMember());
        personAdapter.notifyDataSetChanged();

        Button button_add = findViewById(R.id.button);
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonAddActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton floatingActionButtonPerson=findViewById(R.id.floatingActionButtonPerson);
        floatingActionButtonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonAddActivity.class);
                startActivity(intent);
            }
        });

        Button button_search = findViewById(R.id.button3);
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

        bottomNavigationViewPerson=(BottomNavigationView)findViewById(R.id.bottomNavigationViewPerson);
        bottomNavigationViewPerson.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.person:
                        //Intent personIntent=new Intent(getApplicationContext(), PersonShowActivity.class);
                        //startActivity(personIntent);
                        break;
                    case R.id.group:
                        Intent groupIntent = new Intent(getApplicationContext(), GroupShowActivity.class);
                        startActivity(groupIntent);
                        break;
                }
                return false;
            }
        });
    }
}