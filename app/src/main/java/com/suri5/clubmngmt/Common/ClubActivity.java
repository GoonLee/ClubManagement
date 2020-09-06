package com.suri5.clubmngmt.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.suri5.clubmngmt.R;

public class ClubActivity extends AppCompatActivity {
    Button buttonSetClubInfor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        buttonSetClubInfor=findViewById(R.id.buttonSetClubInfor);
        buttonSetClubInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}