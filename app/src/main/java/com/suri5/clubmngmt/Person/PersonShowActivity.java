package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

import static com.suri5.clubmngmt.Common.Constant.RESULT_SAVE;

public class PersonShowActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinner;
    EditText editText;
    PersonAdapter personAdapter = new PersonAdapter();
    PersonDB personDB;
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

        personAdapter.setItems(personDB.lookUpMember());
        personAdapter.notifyDataSetChanged();

        //추가
        floatingActionButtonPerson=findViewById(R.id.floatingActionButtonPerson);
        floatingActionButtonPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PersonEditActivity.class);
                startActivityForResult(intent, RESULT_SAVE);
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