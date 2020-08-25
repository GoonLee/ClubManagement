package com.suri5.clubmngmt.Group;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import java.util.ArrayList;

import com.suri5.clubmngmt.Common.Constant;
import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupDB {

    // 무슨 속성 가져올건지 -> 다가져올꺼면 null
    private String[] projection = {};
    //columns for WHERE
    private String selelction = "";
    private String[] selectionArgs = {};
    private String sortOrder = Constant.GROUP_COLUMN_NAME + " ASC";

    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public GroupDB(DatabaseHelper databaseHelper) {
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
    public void insertRecord(Group group){
        ContentValues val = new ContentValues();
        val.put(Constant.GROUP_COLUMN_NAME, group.getName());
        val.put(Constant.GROUP_COLUMN_TOTAL, group.getTotalNum());
        //insert시 Primary key return
        database.insert(Constant.GROUP_TABLE_TITLE, null, val);
    }

    public void deleteRecord(Group group){
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        String selection = Constant.GROUP_COLUMN_PK + " LIKE ?";
        String[] selectionArgs = {Integer.toString(group.getKey())};
        int deleteRows = database.delete(Constant.GROUP_TABLE_TITLE, selection, selectionArgs);
    }

    public void updateRecord(Group group) {
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        ContentValues val = new ContentValues();
        val.put(Constant.GROUP_COLUMN_NAME, group.getName());
        val.put(Constant.GROUP_COLUMN_TOTAL, group.getTotalNum());

        String selection = Constant.GROUP_COLUMN_PK+ " LIKE ?";
        String[] selectionArgs = {Integer.toString(group.getKey())};
        int count = database.update(Constant.GROUP_TABLE_TITLE, val,selection, selectionArgs);
    }
    public ArrayList<Group> lookupGroup(){
        Group g;
        println("lookUpGroup 호출됨");
        ArrayList<Group> groups = new ArrayList<Group>();
        Cursor cursor = database.query(Constant.GROUP_TABLE_TITLE, null, null, null, null, null, sortOrder);
        int recordCount = cursor.getCount();
        println("레코드 갯수 : " + recordCount);

        while (cursor.moveToNext()){
            g = new Group();
            g.setKey(cursor.getInt(0));
            g.setName(cursor.getString(1));
            g.setTotalNum(cursor.getInt(2));

            groups.add(g);
        }

        cursor.close();
        return groups;
    }

    public Group findGroup(String data){
        Group g;
        String selection = Constant.GROUP_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {"%" + data+ "%"};
        DatabaseHelper.println("조회 : " +selection + " " +selectionArgs[0]);

        Cursor cursor  = database.query(Constant.GROUP_TABLE_TITLE, null, selection, selectionArgs, null,null, null);
        if(cursor.getCount() > 0){
            g = new Group();
            cursor.moveToFirst();
            g.setKey(cursor.getInt(0));
            g.setName(cursor.getString(1));
            g.setTotalNum(cursor.getInt(2));
            return g;
        }
        else{
            println("해당 그룹 없음");
            return null;
        }
    }

    //그룹 코드를 바탕으로 해당하는 인원들의 키, 이름을 출력하는 메소드
    public void lookupGroupmemeber() {
        Group g;
        String query = "select " +  Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK + ", "
                + Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_NAME + ", "
                + Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_NAME +
                " from " + Constant.PERSON_TABLE_TITLE +
                " inner join " + Constant.GROUP_PERSON_TABLE_TITLE
                + " on "+ Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK +" = "+ Constant.GROUP_PERSON_TABLE_TITLE+"."+ Constant.GROUP_PERSON_COLUMN_PERSONKEY
                + " inner join " + Constant.GROUP_TABLE_TITLE
                + " on "+ Constant.GROUP_TABLE_TITLE+"."+ Constant.GROUP_COLUMN_PK +" = "+ Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_GROUPKEY
                + " order by " + Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_NAME;

        Cursor cursor  = database.rawQuery(query,null);

        int recordCount = cursor.getCount();
        println("레코드 갯수 : " + recordCount);

        while(cursor.moveToNext()){
            println("인원 키 : " + cursor.getInt(0) + " 인원 이름 : " +cursor.getString(1)+ " 그룹 이름 : " +cursor.getString(2));
        }
    }

    /*Group에서 Groups.name = groupname 이면서 GROUP_COLUMN_PK = GROUP_PERSON_COLUMN_GROUPKEY 로 줄이고, 이 교집합 중에서
     GROUP_PERSON_COLUMN_PERSONKEY = PERSON_COLUMN_PK 인 애 찾기 (순서가 탐색에서 중요하다고 해서 일부러 이 순서대로 함 확실하진않음)
     */
    public void findGroupmemeber(String Groupname) {
        Group g;
        String query = "select " +  Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK + ", "
                + Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_NAME + ", "
                + Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_NAME +
                " from " + Constant.GROUP_TABLE_TITLE +
                " inner join " + Constant.GROUP_PERSON_TABLE_TITLE
                + " on "+ Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_NAME + " = '" +Groupname+"'"
                + " and " +Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_PK +" = "+ Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_GROUPKEY
                + " inner join " + Constant.PERSON_TABLE_TITLE
                + " on "+ Constant.GROUP_PERSON_TABLE_TITLE+"."+Constant.GROUP_PERSON_COLUMN_PERSONKEY +" = "+  Constant.PERSON_TABLE_TITLE+"."+Constant.PERSON_COLUMN_PK
                + " order by " + Constant.GROUP_TABLE_TITLE+"."+Constant.GROUP_COLUMN_NAME;

        Cursor cursor  = database.rawQuery(query,null);

        int recordCount = cursor.getCount();
        println("그룹 찾기 "+ Groupname + " 레코드 갯수 : " + recordCount);

        while(cursor.moveToNext()){
            println("인원 키 : " + cursor.getInt(0) + " 인원 이름 : " +cursor.getString(1)+ " 그룹 이름 : " +cursor.getString(2));
        }
    }
}
