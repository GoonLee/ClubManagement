package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

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

        Intent received_intent = getIntent();
        if(received_intent.getIntExtra("pk",-1)!=-1){
            p = personDB.findMember(Constant.PERSON_COLUMN_PK,String.valueOf(received_intent.getIntExtra("pk",0))).get(0);
            picture = p.getPicture();
            imageView.setImageBitmap(picture);
            editText_Email.setText(p.getEmail());
            editText_Major.setText(p.getMajor());
            editText_Mobile.setText(p.getMobile());
            editText_Birthday.setText(p.getBirthday());
            editText_Name.setText(p.getName());
            editText_IdNum.setText(String.valueOf(p.getId_num()));
            if(p.getGender().equals("남성")){
                RadioButton radioButton = findViewById(R.id.radioButton_Male);
                radioButton.setChecked(true);
            }
            else if(p.getGender().equals("여성")){
                RadioButton radioButton = findViewById(R.id.radioButton_Female);
                radioButton.setChecked(true);
            }
            else{
                RadioButton radioButton = findViewById(R.id.radioButton_Unknown);
                radioButton.setChecked(true);
            }
            /*
            Case 1
                for(그룹 개수만큼){
                    텍스트뷰 세팅 (그룹 이름이 내용으로 가게)
                    최상위 리니어 레이아웃에 addView
                }
            Case 2 - 이게 더 나울듯?
                p의 pk를 통해서 그룹명 전체가 있는 String Array(List) groups를 받아옴
                for(String s : groups){
                    TextView 만들어서 setText(s)
                    최상위 (리니어) 레이아웃에 addView()
                    보여주기만 할거면 여기서 끝
                    그룹이 많아질 수 있으니 스크롤뷰로 가는게 나을수도 있겠음
             */
        }
        else{
            p = new Person();
        }

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
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=radioGroup_Sex.getCheckedRadioButtonId();
                if(id==-1){
                    Toast.makeText(getApplicationContext(),"성별을 선택해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }

                Person new_p = new Person();
                new_p.setPk(p.getPk());
                new_p.setName(editText_Name.getText().toString());
                new_p.setId_num(Integer.parseInt(editText_IdNum.getText().toString()));
                new_p.setMajor(editText_Major.getText().toString());
                new_p.setEmail(editText_Email.getText().toString());
                new_p.setMobile(editText_Mobile.getText().toString());
                if(id==R.id.radioButton_Male) new_p.setGender("남성");
                else if(id==R.id.radioButton_Female) new_p.setGender("여성");
                else new_p.setGender("알수없음");
                new_p.setPicture(picture);
                new_p.setBirthday(editText_Birthday.getText().toString());

                personDB.updateRecord(new_p);


                Log.d("PersonManageActivity","onCL");
                Intent intent = new Intent(getApplicationContext(),PersonShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        Button button_delete = findViewById(R.id.button5);
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                personDB.deletePerson(p.getPk());
                Intent intent = new Intent(getApplicationContext(),PersonShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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