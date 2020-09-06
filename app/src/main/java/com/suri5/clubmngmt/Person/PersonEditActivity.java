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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Group.Group;
import com.suri5.clubmngmt.Group.GroupAdapter_short;
import com.suri5.clubmngmt.R;

import java.util.ArrayList;

public class PersonEditActivity extends AppCompatActivity {
    //Todo : Group setting, Date picker, fancier xml, Version matching
    ImageView imageView;
    EditText editText_Name,editText_IdNum,editText_Major,editText_Birthday,editText_Mobile,editText_Email;
    RadioGroup radioGroup_Sex;
    Bitmap picture;
    PersonDB personDB;
    Person p;

    //추가/수정 판단
    int pk = -1;
    boolean isEdit = false;

    //소속 그룹 출력용
    RecyclerView recyclerView;
    ArrayList<Group> groups = new ArrayList<Group>();
    GroupAdapter_short groupAdapter_short = new GroupAdapter_short();

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

        recyclerView = findViewById(R.id.recyclerView_group_short);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(groupAdapter_short);

        personDB = new PersonDB(new DatabaseHelper(getApplicationContext()));
        final Button button_save = findViewById(R.id.button_OK);
        final Button button_delete = findViewById(R.id.button_delete);

        try{
            Intent received_intent = getIntent();
            pk = received_intent.getIntExtra("pk",-1);

            //인원 수정임
            if(pk !=-1) {
                button_save.setText("수정");


                p = personDB.findMember(Constant.PERSON_COLUMN_PK, String.valueOf(received_intent.getIntExtra("pk", 0))).get(0);
                picture = p.getPicture();
                editText_Email.setText(p.getEmail());
                editText_Major.setText(p.getMajor());
                editText_Mobile.setText(p.getMobile());
                editText_Birthday.setText(p.getBirthday());
                editText_Name.setText(p.getName());
                editText_IdNum.setText(String.valueOf(p.getId_num()));

                groups = personDB.lookupGroup(p.getPk());
                groupAdapter_short.setItems(groups);
                groupAdapter_short.notifyDataSetChanged();
            }
            else{
                p = new Person();
                button_delete.setVisibility(View.GONE);
            }
            //사진 파일 세팅
            if(picture == null){
                picture = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.avatar_empty);
            }
            imageView.setImageBitmap(picture);

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"사람을 불러오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
            finish();
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

        button_save.setOnClickListener(new View.OnClickListener() {
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

                if(pk != -1){
                    personDB.updateRecord(new_p);
                    personDB.deleteGroupALLFromMember(p.getPk());
                }
                else{
                    personDB.insertRecord(new_p);
                }
                //새로 그룹정보 넣기
                for(Group g : groups){
                    personDB.insertGroupFromMember(g.getKey(),pk);
                }

                Intent intent=new Intent();
                //intent.putExtra("group",g);
                setResult(RESULT_OK,intent);
                Log.d("PersonManageActivity","onCL");
                finish();
            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pk != -1){
                    personDB.deletePerson(pk);
                    Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent();
                    setResult(RESULT_OK,intent);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(),"그룹이 없습니다.",Toast.LENGTH_LONG).show();
                    finish();
                }
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