package com.suri5.clubmngmt.Person;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Group.Group;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

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
        database.execSQL(Constant.GROUP_CREATE_TABLE);
        database.execSQL(Constant.GROUP_PERSON_CREATE_TABLE);
        database.execSQL(Constant.SCHEDULE_CREATE_TABLE);
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

    public void deletePerson(int pk){
        if(database == null){
            DatabaseHelper.println("Create DB first");
            return;
        }
        String selection = Constant.PERSON_COLUMN_PK + " Like ?";
        String[] selectionArgs = {Integer.toString(pk)};
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

    //전체 인원
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

    //해당 인원이 속해있는 그룹 모두 받아오기
    public ArrayList<Group> lookupGroup(int pk){
        println(pk + " 찾기 시작");
        ArrayList<Group> groups = new ArrayList<Group>();
        Group g;

        String query = "SELECT " + Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_PK
                + ", "+Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_NAME
                + ", "+Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_TOTAL
                + " FROM " + Constant.GROUP_TABLE_TITLE + ", "
                + Constant.GROUP_PERSON_TABLE_TITLE
                + " WHERE " + Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_PERSONKEY+ " = " + pk //여기에 키
                + " AND " +  Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_GROUPKEY +" = "+  Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_PK;
        println(query);

        Cursor cursor = database.rawQuery(query,null);

        while (cursor.moveToNext()){
            g = new Group();
            g.setKey(cursor.getInt(0));
            g.setName(cursor.getString(1));
            g.setTotalNum(cursor.getInt(2));
            groups.add(g);
        }
        return groups;
    }

    //인원에서 그룹 삭제
    public void deleteGroupFromMember(int personkey, int groupkey){
        String sql =  "delete from "
                +Constant.GROUP_PERSON_TABLE_TITLE
                +" WHERE " + Constant.GROUP_PERSON_COLUMN_GROUPKEY + " = " + groupkey
                +" AND " + Constant.GROUP_PERSON_COLUMN_PERSONKEY + " = " + personkey;
        database.rawQuery(sql, null);
    }

    //인원에서 그룹 삽입
    public void insertGroupFromMember(int personkey, int groupkey){
        ContentValues val = new ContentValues();
        val.put(Constant.GROUP_PERSON_COLUMN_PERSONKEY, personkey);
        val.put(Constant.GROUP_PERSON_COLUMN_GROUPKEY, groupkey);
        //insert시 Primary key return
        database.insert(Constant.GROUP_PERSON_TABLE_TITLE, null, val);
    }

    //인원에서 그룹 전체 삭제
    public void deleteGroupALLFromMember(int personkey){
        String selection = Constant.GROUP_PERSON_COLUMN_PERSONKEY + " LIKE ?";
        String[] selectionArgs = {Integer.toString(personkey)};
        int deleteRows = database.delete(Constant.GROUP_PERSON_TABLE_TITLE, selection, selectionArgs);
        println("삭제 성공 ");
    }

    public ArrayList<String> getAllMembersName(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<String> names= new ArrayList<>();
        String[] columns = {Constant.PERSON_COLUMN_NAME};
        Cursor cursor = database.query(true,Constant.PERSON_TABLE_TITLE, columns, null, null, null, null, sortOrder,null);
        int recordCount = cursor.getCount();
        DatabaseHelper.println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names;
    }

    public ArrayList<String> getAllMembersIdNum(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<String> idNums= new ArrayList<>();
        String[] columns = {Constant.PERSON_COLUMN_IDNUM};
        Cursor cursor = database.query(true,Constant.PERSON_TABLE_TITLE, columns, null, null, null, null, sortOrder,null);
        int recordCount = cursor.getCount();
        DatabaseHelper.println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            idNums.add(cursor.getString(0));
        }
        cursor.close();
        return idNums;
    }

    public ArrayList<String> getAllMembersMajor(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<String> majors= new ArrayList<>();
        String[] columns = {Constant.PERSON_COLUMN_MAJOR};
        Cursor cursor = database.query(true,Constant.PERSON_TABLE_TITLE, columns, null, null, null, null, sortOrder,null);
        int recordCount = cursor.getCount();
        DatabaseHelper.println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            majors.add(cursor.getString(0));
        }
        cursor.close();
        return majors;
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