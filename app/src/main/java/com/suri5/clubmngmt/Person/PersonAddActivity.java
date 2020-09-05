package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class PersonAddActivity extends AppCompatActivity {
    //Todo : Group setting, Date picker, fancier xml, Version matching
    LinearLayout container;
    ImageView imageView;
    EditText editText_Name,editText_IdNum,editText_Major,editText_Birthday,editText_Mobile,editText_Email;
    RadioGroup radioGroup_Sex;
    Bitmap picture;
    PersonDB personDB;
    ArrayList<EditText> groups = new ArrayList<>();

    String emailPattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    boolean emailCheck =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_add);
        imageView = findViewById(R.id.imageView);
        editText_Email=findViewById(R.id.editPersonEmail);
        editText_Major=findViewById(R.id.editPersonMajor);
        editText_IdNum=findViewById(R.id.editPersonIDNum);
        editText_Mobile=findViewById(R.id.editPersonMobile);
        editText_Birthday=findViewById(R.id.editPersonDate);
        editText_Name=findViewById(R.id.editPersonName);
        radioGroup_Sex=findViewById(R.id.radioGroupGender);
        container = findViewById(R.id.container);
        personDB = new PersonDB(new DatabaseHelper(getApplicationContext()));


        picture = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.avatar_empty);
        Button button_picture = findViewById(R.id.button_picture);
        button_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, Constant.REQUEST_CODE_GET_IMAGE);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picture = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.avatar_empty);
                imageView.setImageBitmap(picture);
            }
        });
        Button button_save = findViewById(R.id.button_save);
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Todo : Group 처리
                for(EditText et : groups){
                    //여기서 그룹 DB 처리
                }
                //Todo : 빈 필드 확인해서 처리
                int id=radioGroup_Sex.getCheckedRadioButtonId();
                //성별 라디오버튼에서 성별 string 에 저장
                RadioButton radioButton=findViewById(id);

                //추가한 person 객체를 넘겨줌
                Person p = new Person();
                p.setName(editText_Name.getText().toString());
                p.setId_num(Integer.parseInt(editText_IdNum.getText().toString()));
                p.setMajor(editText_Major.getText().toString());

                //이메일 형식 확인 표현
                String input = editText_Email.getText().toString().trim();
                if(input.matches(emailPattern)&& toString().length()>0){
                    emailCheck=true;

                }else{
                    emailCheck=false;
                }

                p.setMobile(editText_Mobile.getText().toString());
                // Todo: M/F로 구분하게 할 예정
                p.setGender(radioButton.getText().toString());
                p.setPicture(picture);
                p.setBirthday(editText_Birthday.getText().toString());

                //이메일 형식에 따른 결과
                if(emailCheck==true){
                    p.setEmail(editText_Email.getText().toString());

                    personDB.insertRecord(p);
                    Log.d("PersonManageActivity","onCL");
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"잘못된 이메일입니다",Toast.LENGTH_SHORT).show();
                }

            }
        });
        Button button_add_group = findViewById(R.id.button4);
        button_add_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = new EditText(getApplicationContext());
                et.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));
                groups.add(et);
                container.addView(et, container.getChildCount()-2);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == Constant.REQUEST_CODE_GET_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            try {
                //Todo: 빨간줄 무시. 버전 체크하는 if문 넣을 예정
                picture = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), selectedImageUri));
                imageView.setImageBitmap(picture);
            } catch (Exception e) {}
        }
    }
}