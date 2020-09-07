package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

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

        FloatingActionButton button_add = findViewById(R.id.button_OK);
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