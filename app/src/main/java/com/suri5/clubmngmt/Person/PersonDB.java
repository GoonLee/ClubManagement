package com.suri5.clubmngmt.Person;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PersonDB {
    // 무슨 속성 가져올건지 -> 다가져올꺼면 null
    private String[] projection = {};
    //columns for WHERE
    private String selelction = "";
    private String[] selectionArgs = {};
    private String sortOrder = Constant.PERSON_COLUMN_PK + " ASC";


    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public PersonDB(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void createTable(){
        if(database == null){
            DatabaseHelper.println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        database.execSQL(Constant.PERSON_CREATE_TABLE);
    }

    public void insertRecord(Person person){
        if(database == null){
            DatabaseHelper.println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        ContentValues val = new ContentValues();
        val.put(Constant.PERSON_COLUMN_NAME, person.getName());
        val.put(Constant.PERSON_COLUMN_IDNUM, person.getId_num());
        val.put(Constant.PERSON_COLUMN_MAJOR, person.getMajor());
        val.put(Constant.PERSON_COLUMN_GENDER, person.getGender());
        val.put(Constant.PERSON_COLUMN_BIRTHDAY, person.getBirthday());
        val.put(Constant.PERSON_COLUMN_MOBILE, person.getMobile());
        val.put(Constant.PERSON_COLUMN_EMAIL, person.getEmail());
        val.put(Constant.PERSON_COLUMN_PICTURE, getByteArrayFromDrawable(person.getPicture()));

        //insert시 Primary key return
        database.insert(Constant.PERSON_TABLE_TITLE, null, val);
    }



    public void deleteRecord(Person person){
        if(database == null){
            DatabaseHelper.println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        String selection = Constant.PERSON_COLUMN_PK + " LIKE ?";
        String[] selectionArgs = {Integer.toString(person.getPk())};
        int deleteRows = database.delete(Constant.PERSON_TABLE_TITLE, selection, selectionArgs);
    }

    public void updateRecord(Person person){
        if(database == null){
            DatabaseHelper.println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        ContentValues val = new ContentValues();

        val.put(Constant.PERSON_COLUMN_NAME, person.getName());
        val.put(Constant.PERSON_COLUMN_IDNUM, person.getId_num());
        val.put(Constant.PERSON_COLUMN_MAJOR, person.getMajor());
        val.put(Constant.PERSON_COLUMN_GENDER, person.getGender());
        val.put(Constant.PERSON_COLUMN_BIRTHDAY, person.getBirthday());
        val.put(Constant.PERSON_COLUMN_MOBILE, person.getMobile());
        val.put(Constant.PERSON_COLUMN_EMAIL, person.getEmail());
        val.put(Constant.PERSON_COLUMN_PICTURE, getByteArrayFromDrawable(person.getPicture()));

        String selection = Constant.PERSON_COLUMN_PK + " =";
        String[] selectionArgs = {String.valueOf(person.getPk())};
        int count = database.update(Constant.PERSON_TABLE_TITLE, val, Constant.PERSON_COLUMN_PK+"="+person.getPk(),null);
        DatabaseHelper.println("업데이트 : " + person.getPk() + " 이름 : " + person.getName());
    }

    //해당하는 사람들을 담은 ArrayList를 넘겨주는 findMember;
    public ArrayList<Person> findMember(String columns_name, String data){
        ArrayList<Person> person = new ArrayList<>();
        String selection = columns_name + " LIKE ?";
        String[] selectionArgs = {"%" + data+ "%"};
        DatabaseHelper.println("조회 : " +selection + " " +selectionArgs[0]);

        Cursor cursor  = database.query(Constant.PERSON_TABLE_TITLE, null, selection, selectionArgs, null,null, null);

        if(cursor.getCount() > 0){
            Person p;

            while (cursor.moveToNext()){
                p = new Person();
                p.setPk(cursor.getInt(0));
                p.setName(cursor.getString(1));
                p.setId_num(cursor.getInt(2));
                p.setMajor(cursor.getString(3));
                p.setGender(cursor.getString(4));
                p.setBirthday(cursor.getString(5));
                p.setMobile(cursor.getString(6));
                p.setEmail(cursor.getString(7));
                p.setPicture(getBitmapFromByteArray(cursor.getBlob(8)));

                DatabaseHelper.println("검색 결과 : " + p.getName() + " " + p.getPk());
                person.add(p);
            }
        }
        else{
            DatabaseHelper.println("조회 결과 : + " +data+ " 없음");
        }
        cursor.close();
        return person;
    }

    public int getMemberCount(){
        Cursor cursor=database.query(Constant.PERSON_TABLE_TITLE, null, null, null, null, null, sortOrder);
        return cursor.getCount();
    }

    public ArrayList<Person> lookUpMember(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<Person> members= new ArrayList<Person>();
        Cursor cursor = database.query(Constant.PERSON_TABLE_TITLE, null, null, null, null, null, sortOrder);
        int recordCount = cursor.getCount();
        DatabaseHelper.println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){

            Person p = new Person();
            p.setPk(cursor.getInt(0));
            p.setName(cursor.getString(1));
            p.setId_num(cursor.getInt(2));
            p.setMajor(cursor.getString(3));
            p.setGender(cursor.getString(4));
            p.setBirthday(cursor.getString(5));
            p.setMobile(cursor.getString(6));
            p.setEmail(cursor.getString(7));
            p.setPicture(getBitmapFromByteArray(cursor.getBlob(8)));

            members.add(p);
            DatabaseHelper.println("레코드 : " + p.getName() + " " + p.getPk());
        }
        cursor.close();
        return members;
    }



    public Bitmap getBitmapFromByteArray(byte[] bytes){
        Bitmap bit;

        if(bytes != null){
            bit = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            return bit;
        }
        else{
            DatabaseHelper.println("사진파일이 없음");
            return null;
        }
    }

    public byte[] getByteArrayFromDrawable(Bitmap d){
        if(d != null){
            //if drawable
            //Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
            Bitmap bitmap = d;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] data = stream.toByteArray();
            return data;
        }
        else{
            DatabaseHelper.println("사진파일이 없음");
            return null;
        }
    }
}