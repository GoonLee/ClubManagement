package com.suri5.clubmngmt.Group;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.Person;

import java.security.PublicKey;
import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class GroupDB {
    public static final String DATABASE_NAME = "ClubManagementDB";
    public static final String TABLE_NAME = "Groups";
    public static final String COLUMNS_ID = "_id";
    public static final String Group_NAME = "Name";
    public static final String Group_TOTAL = "TotalNum";

    private static String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME + "("  + COLUMNS_ID +" integer PRIMARY KEY autoincrement, "
            + Group_NAME +" text UNIQUE, "
            + Group_TOTAL + " integer)";

    // 무슨 속성 가져올건지 -> 다가져올꺼면 null
    private String[] projection = {};
    //columns for WHERE
    private String selelction = "";
    private String[] selectionArgs = {};
    private String sortOrder = Group_NAME + " ASC";

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
        database.execSQL(CREATE_TABLE);
    }
    public void insertRecord(Group group){
        ContentValues val = new ContentValues();
        val.put(Group_NAME, group.getName());
        val.put(Group_TOTAL, group.getTotalNum());
        //insert시 Primary key return
        database.insert(TABLE_NAME, null, val);
    }

    public void deleteRecord(Group group){
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        String selection = COLUMNS_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(group.getKey())};
        int deleteRows = database.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void updateRecord(Group group) {
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        ContentValues val = new ContentValues();
        val.put(Group_NAME, group.getName());
        val.put(Group_TOTAL, group.getTotalNum());

        String selection = COLUMNS_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(group.getKey())};
        int count = database.update(TABLE_NAME, val,selection, selectionArgs);
    }
    public ArrayList<Group> lookupGroup(){
        Group g;
        println("lookUpGroup 호출됨");
        ArrayList<Group> groups = new ArrayList<Group>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, sortOrder);
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

    //해당하는 사람들을 담은 ArrayList를 넘겨주는 findMember;
    public Group findMember(String columns_name, String data) {
        Group g;
        String selection = columns_name + " LIKE ?";
        String[] selectionArgs = {"%" + data + "%"};
        println("조회 : " + selection + " " + selectionArgs[0]);
        Cursor cursor  = database.query(TABLE_NAME, null, selection, selectionArgs, null,null, null);
        if(cursor.getCount() > 0){
            g = new Group();
            g.setKey(cursor.getInt(0));
            g.setName(cursor.getString(1));
            g.setTotalNum(cursor.getInt(2));
            return g;
        }
        else {
            return null;
        }
    }
}
