package com.suri5.clubmngmt.Group;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DBManager;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.Person;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupDBManager implements DBManager<Group> {
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public GroupDBManager(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void createTable(){
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        database.execSQL(Constant.GROUP_CREATE_TABLE);
        database.execSQL(Constant.GROUP_PERSON_CREATE_TABLE);
    }

    @Override
    public void insertRecord(Group record) {
        ContentValues val = new ContentValues();
        val.put(Constant.GROUP_COLUMN_NAME, record.getName());
        val.put(Constant.GROUP_COLUMN_TOTAL, record.getTotalNum());
        //insert시 Primary key return
        database.insert(Constant.GROUP_TABLE_TITLE, null, val);
    }

    @Override
    public void deleteRecord(int pk) {
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        String selection = Constant.GROUP_COLUMN_PK + " LIKE ?";
        String[] selectionArgs = {Integer.toString(pk)};
        int deleteRows = database.delete(Constant.GROUP_TABLE_TITLE, selection, selectionArgs);
    }

    @Override
    public void updateRecord(Group record) {
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        ContentValues val = new ContentValues();
        val.put(Constant.GROUP_COLUMN_NAME, record.getName());
        val.put(Constant.GROUP_COLUMN_TOTAL, record.getTotalNum());

        String selection = Constant.GROUP_COLUMN_PK+ " LIKE ?";
        String[] selectionArgs = {Integer.toString(record.getKey())};
        int count = database.update(Constant.GROUP_TABLE_TITLE, val,selection, selectionArgs);
    }

    @Override
    public ArrayList<Group> findRecordFromName(String string) {
        ArrayList<Group> groups = new ArrayList<>();
        String selection = Constant.GROUP_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + string + "%"};
        DatabaseHelper.println("조회 : " +selection + " " +selectionArgs[0]);

        Cursor cursor  = database.query(Constant.GROUP_TABLE_TITLE, null, selection, selectionArgs, null,null, null);

        if(cursor.getCount() > 0){
            Group g;

            while (cursor.moveToNext()){
                g = new Group();
                g.setKey(cursor.getInt(0));
                g.setName(cursor.getString(1));
                g.setTotalNum(cursor.getInt(2));

                DatabaseHelper.println("검색 결과 : " + g.getName() + " " + g.getKey());
                groups.add(g);
            }
        }
        else{
            DatabaseHelper.println("조회 결과 : + " + string + " 없음");
        }
        cursor.close();
        return groups;
    }

    @Override
    public ArrayList<Group> findRecord(String column, String data) {
        ArrayList<Group> groups = new ArrayList<>();
        String selection = column + " LIKE ?";
        String[] selectionArgs = {"%" + data+ "%"};
        DatabaseHelper.println("조회 : " +selection + " " +selectionArgs[0]);

        Cursor cursor  = database.query(Constant.GROUP_TABLE_TITLE, null, selection, selectionArgs, null,null, null);

        if(cursor.getCount() > 0){
            Group g;

            while (cursor.moveToNext()){
                g = new Group();
                g.setKey(cursor.getInt(0));
                g.setName(cursor.getString(1));
                g.setTotalNum(cursor.getInt(2));

                DatabaseHelper.println("검색 결과 : " + g.getName() + " " + g.getKey());
                groups.add(g);
            }
        }
        else{
            DatabaseHelper.println("조회 결과 : + " +data+ " 없음");
        }
        cursor.close();
        return groups;
    }

    @Override
    public ArrayList<Group> getAllRecord() {
        Group g;
        println("lookUpGroup 호출됨");
        ArrayList<Group> groups = new ArrayList<Group>();
        Cursor cursor = database.query(Constant.GROUP_TABLE_TITLE, null, null, null, null, null, null);
        int recordCount = cursor.getCount();
        println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            g = new Group();
            g.setKey(cursor.getInt(0));
            g.setName(cursor.getString(1));

            int number = findGroupmemberNum(g.getKey());
            g.setTotalNum(number);
            updateRecord(g);
            groups.add(g);
        }

        cursor.close();
        return groups;
    }

    @Override
    public int getRecordCount() {
        Cursor cursor=database.query(Constant.PERSON_TABLE_TITLE, null, null, null, null, null, null);
        return cursor.getCount();
    }

    public ArrayList<Person> findGroupmember(int groupkey){
        println(groupkey + " 찾기 시작");
        ArrayList<Person> brif_member = new ArrayList<Person>();
        Person p;

        String query = "SELECT " + Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK
                + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_NAME
                + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_IDNUM
                + " FROM " + Constant.PERSON_TABLE_TITLE + ", "
                + Constant.GROUP_PERSON_TABLE_TITLE
                + " WHERE " + Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_GROUPKEY+ " = " + groupkey //여기에 키
                + " AND " +  Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_PERSONKEY +" = "+  Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK;
        Cursor cursor = database.rawQuery(query,null);

        while (cursor.moveToNext()){
            p = new Person();
            p.setPk(cursor.getInt(0));
            p.setName(cursor.getString(1));
            p.setId_num(cursor.getInt(2));
            brif_member.add(p);
        }

        return brif_member;
    }

    public int findGroupmemberNum(int groupkey){
        int num = 0;
        String query = "SELECT COUNT (" +Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK+")"
                + " FROM " + Constant.PERSON_TABLE_TITLE + ", "
                + Constant.GROUP_PERSON_TABLE_TITLE
                + " WHERE " + Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_GROUPKEY+ " = " + groupkey //여기에 키
                + " AND " +  Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_PERSONKEY +" = "+  Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK;
        Cursor cursor = database.rawQuery(query,null);

        if(cursor.moveToFirst()){
            num = cursor.getInt(0);
        }
        return num;
    }

    //현재 그룹 뺀 나머지 사람들
    public ArrayList<Person> lookUpMemberExcept(int groupkey){
        DatabaseHelper.println("lookUpMemberExcept 호출됨");
        ArrayList<Person> members= new ArrayList<Person>();

        String query =
                "SELECT " + Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK
                        + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_NAME
                        + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_IDNUM
                        + " FROM " + Constant.PERSON_TABLE_TITLE
                        + " EXCEPT "
                        +" SELECT " + Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK
                        + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_NAME
                        + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_IDNUM
                        + " FROM " + Constant.PERSON_TABLE_TITLE + ", "
                        + Constant.GROUP_PERSON_TABLE_TITLE
                        + " WHERE " + Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_GROUPKEY+ " = " + groupkey //여기에 키
                        + " AND " +  Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_PERSONKEY +" = "+  Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK;

        println(query);
        Cursor cursor = database.rawQuery(query,null);

        while (cursor.moveToNext()){

            Person p = new Person();
            p.setPk(cursor.getInt(0));
            p.setName(cursor.getString(1));
            p.setId_num(cursor.getInt(2));

            members.add(p);
            DatabaseHelper.println("레코드 : " + p.getName() + " " + p.getPk());
        }
        cursor.close();
        return members;
    }

    //전체 인원들
    public ArrayList<Person> lookUpMember(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<Person> members= new ArrayList<Person>();

        String query =
                "SELECT " + Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK
                        + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_NAME
                        + ", "+Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_IDNUM
                        + " FROM " + Constant.PERSON_TABLE_TITLE;

        println(query);
        Cursor cursor = database.rawQuery(query,null);

        while (cursor.moveToNext()){

            Person p = new Person();
            p.setPk(cursor.getInt(0));
            p.setName(cursor.getString(1));
            p.setId_num(cursor.getInt(2));

            members.add(p);
            DatabaseHelper.println("레코드 : " + p.getName() + " " + p.getPk());
        }
        cursor.close();
        return members;
    }

    public ArrayList<String> getAllMembersName(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<String> names= new ArrayList<>();
        String[] columns = {Constant.PERSON_COLUMN_NAME};
        Cursor cursor = database.query(true,Constant.PERSON_TABLE_TITLE, columns, null, null, null, null, null,null);
        int recordCount = cursor.getCount();
        DatabaseHelper.println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names;
    }

    public ArrayList<String> getAllGroupsName(){
        DatabaseHelper.println("lookUpMember 호출됨");
        ArrayList<String> names= new ArrayList<>();
        String[] columns = {Constant.GROUP_COLUMN_NAME};
        Cursor cursor = database.query(true,Constant.GROUP_TABLE_TITLE, columns, null, null, null, null, null,null);
        int recordCount = cursor.getCount();
        DatabaseHelper.println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            names.add(cursor.getString(0));
        }
        cursor.close();
        return names;
    }


    //그룹에서 인원 삭제
    public void deleteMemberFromGroup(int groupkey, int personkey){
        String sql =  "delete from "
                +Constant.GROUP_PERSON_TABLE_TITLE
                +" WHERE " + Constant.GROUP_PERSON_COLUMN_GROUPKEY + " = " + groupkey
                +" AND " + Constant.GROUP_PERSON_COLUMN_PERSONKEY + " = " + personkey;
        database.rawQuery(sql, null);
    }
    //그룹에서 인원 삽입
    public void insertMemberFromGroup(int personkey, int groupkey){
        ContentValues val = new ContentValues();
        val.put(Constant.GROUP_PERSON_COLUMN_PERSONKEY, personkey);
        val.put(Constant.GROUP_PERSON_COLUMN_GROUPKEY, groupkey);
        //insert시 Primary key return
        database.insert(Constant.GROUP_PERSON_TABLE_TITLE, null, val);
    }

    //그룹에서 인원 전체 삭제
    public void deleteMemberAllGroup(int groupkey){
        String selection = Constant.GROUP_PERSON_COLUMN_GROUPKEY + " LIKE ?";
        String[] selectionArgs = {Integer.toString(groupkey)};
        int deleteRows = database.delete(Constant.GROUP_PERSON_TABLE_TITLE, selection, selectionArgs);
        println("삭제 성공 ");
    }
}
