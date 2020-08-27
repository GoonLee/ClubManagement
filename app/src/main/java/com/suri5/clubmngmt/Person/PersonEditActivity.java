package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

public class PersonEditActivity extends AppCompatActivity {
    //Todo : Group setting, Date picker, fancier xml, Version matching
    ImageView imageView;
    EditText editText_Name,editText_IdNum,editText_Major,editText_Birthday,editText_Mobile,editText_Email;
    RadioGroup radioGroup_Sex;
    Bitmap picture;
    PersonDB personDB;
    Person p;
    int pk = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_edit);
        imageView = findViewById(R.id.imageView);
        editText_Email=findViewById(R.id.editPersonEmail);
        editText_Major=findViewById(R.id.editPersonMajor);
        editText_IdNum=findViewById(R.id.editPersonIDNum);
        editText_Mobile=findViewById(R.id.editPersonMobile);
        editText_Birthday=findViewById(R.id.editPersonDate);
        editText_Name=findViewById(R.id.editPersonName);
        radioGroup_Sex=findViewById(R.id.radioGroupGender);
        personDB = new PersonDB(new DatabaseHelper(getApplicationContext()));
        Intent intent = getIntent();
        pk = intent.getIntExtra("pk",-1);

        if(pk!=-1){
            p = personDB.findMember(Constant.PERSON_COLUMN_PK,String.valueOf(intent.getIntExtra("pk",0))).get(0);
            imageView.setImageBitmap(p.getPicture());
            editText_Email.setText(p.getEmail());
            editText_Major.setText(p.getMajor());
            editText_Mobile.setText(p.getMobile());
            editText_Birthday.setText(p.getBirthday());
            editText_Name.setText(p.getName());
            editText_IdNum.setText(String.valueOf(p.getId_num()));
        }
        else{
            p = new Person();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, Constant.REQUEST_CODE_GET_IMAGE);
            }
        });
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=radioGroup_Sex.getCheckedRadioButtonId();
                //성별 라디오버튼에서 성별 string 에 저장
                RadioButton radioButton=findViewById(id);

                //추가한 person 객체를 넘겨줌
                p.setName(editText_Name.getText().toString());
                p.setId_num(Integer.parseInt(editText_IdNum.getText().toString()));
                p.setMajor(editText_Major.getText().toString());
                p.setEmail(editText_Email.getText().toString());
                p.setMobile(editText_Mobile.getText().toString());
                // Todo: M/F로 구분하게 할 예정
                p.setGender(radioButton.getText().toString());
                p.setPicture(picture);
                p.setBirthday(editText_Birthday.getText().toString());
                //-1이아니 업데이트
                if(pk != -1){
                    personDB.updateRecord(p);
                }
                //-1이었으면 삽입
                else{
                    personDB.insertRecord(p);
                }
                Log.d("PersonManageActivity","onCL");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == Constant.REQUEST_CODE_GET_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                //Todo: 빨간줄 무시 버전 체크하는 if문 넣을 예정
                picture = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImageUri));
                imageView.setImageBitmap(picture);
            } catch (Exception e) {}
        }
    }
}