package com.suri5.clubmngmt.Person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Common.EdiImages;
import com.suri5.clubmngmt.Group.Group;
import com.suri5.clubmngmt.Group.GroupAdapter_short;
import com.suri5.clubmngmt.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;
import static com.suri5.clubmngmt.Common.EdiImages.resize_filesize;

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

    //이메일 확인용
    String emailPattern="^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    boolean emailCheck =false;
    String input;

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
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        recyclerView.setLayoutManager(flowLayoutManager);
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

                p = personDB.findMember(Constant.PERSON_COLUMN_PK, String.valueOf(pk)).get(0);
                picture = p.getPicture();
                editText_Email.setText(p.getEmail());
                editText_Major.setText(p.getMajor());
                editText_Mobile.setText(p.getMobile());
                editText_Birthday.setText(p.getBirthday());
                editText_Name.setText(p.getName());
                editText_IdNum.setText(String.valueOf(p.getId_num()));

                if(p.getGender().equals("남성")){
                    RadioButton radioButton = findViewById(R.id.radioButton_Male);
                    radioButton.setChecked(true);
                } else if(p.getGender().equals("여성")){
                    RadioButton radioButton = findViewById(R.id.radioButton_Female);
                    radioButton.setChecked(true);
                } else{
                    RadioButton radioButton = findViewById(R.id.radioButton_Unknown);
                    radioButton.setChecked(true);
                }

                groups = personDB.lookupGroup(p.getPk());
                groupAdapter_short.setItems(groups);


            }
            else{
                p = new Person();
                button_delete.setVisibility(View.GONE);
            }


            //사진 파일 세팅
            Glide.with(this).load(picture).error(R.drawable.avatar_empty).into(imageView);


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"사람을 불러오는데 오류가 발생했습니다.",Toast.LENGTH_LONG).show();
            finish();
        }

        /*Button button_picture = findViewById(R.id.button_picture);
        button_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, Constant.REQUEST_CODE_GET_IMAGE);
            }
        });*/

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, Constant.REQUEST_CODE_GET_IMAGE);
                //picture = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.avatar_empty);
                //Glide.with(view).load(picture).error(R.drawable.avatar_empty).into(imageView);
            }
        });


        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup_Sex.getCheckedRadioButtonId();
                if (id == -1) {
                    Toast.makeText(getApplicationContext(), "성별을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                //이메일 확인
                input = editText_Email.getText().toString().trim();
                if ((input.matches(emailPattern) && input.length() > 0) || input.length() == 0) {
                    emailCheck = true;
                } else {
                    emailCheck = false;
                }

                Person new_p = new Person();
                new_p.setPk(p.getPk());
                new_p.setName(editText_Name.getText().toString());
                new_p.setId_num(Integer.parseInt(editText_IdNum.getText().toString()));
                new_p.setMajor(editText_Major.getText().toString());

                new_p.setMobile(editText_Mobile.getText().toString());
                if (id == R.id.radioButton_Male) new_p.setGender("남성");
                else if (id == R.id.radioButton_Female) new_p.setGender("여성");
                else new_p.setGender("알수없음");
                new_p.setPicture(picture);
                new_p.setBirthday(editText_Birthday.getText().toString());

                if (emailCheck == true) {//이메일 확인
                    new_p.setEmail(editText_Email.getText().toString());

                    if (pk != -1) {
                        personDB.updateRecord(new_p);
                        personDB.deleteGroupALLFromMember(p.getPk());
                    } else {
                        personDB.insertRecord(new_p);
                    }
                    //새로 그룹정보 넣기
                    for (Group g : groups) {
                        println(g.getName());
                        personDB.insertGroupFromMember(pk, g.getKey());
                    }

                    Intent intent = new Intent(getApplicationContext(), PersonShowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    //intent.putExtra("group",g);
                    //setResult(RESULT_OK,intent);
                    Log.d("PersonManageActivity", "onCL");
                    //finish();
                } else {
                    Toast.makeText(getApplicationContext(), "잘못된 이메일입니다", Toast.LENGTH_SHORT).show();
                }
            }
        });


    //삭제
        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pk != -1){
                    personDB.deletePerson(pk);
                    Toast.makeText(getApplicationContext(),"삭제가 완료되었습니다.",Toast.LENGTH_LONG).show();
                    //Intent intent=new Intent();
                    //setResult(RESULT_OK,intent);
                    Intent intent=new Intent(getApplicationContext(),PersonShowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(),"그룹이 없습니다.",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(getApplicationContext(),PersonShowActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }

    public void ImageChange(View v){
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if (requestCode == Constant.REQUEST_CODE_GET_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            try {
                UCrop.of(selectedImageUri, destination).start(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            picture = resize_filesize(getApplicationContext(), resultUri, 107200);
            Glide.with(this).load(picture).error(R.drawable.avatar_empty).into(imageView);

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            try{
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = result.getUri();
                    picture = resize_filesize(getApplicationContext(), selectedImageUri, 107200);
                    Glide.with(this).load(picture).error(R.drawable.avatar_empty).into(imageView);
                }
            }
            catch (Exception e){
                println("에러 발생");
            }

        }
    }


}