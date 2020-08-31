package com.suri5.clubmngmt.Schedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Budget.Budget;
import com.suri5.clubmngmt.Common.DatabaseHelper;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class ScheduleDB {
    public static final String DATABASE_NAME = "ClubManagementDB";
    public static final String TABLE_NAME = "Schedule";
    public static final String COLUMNS_ID = "_id";
    public static final String SCHEDULE_TITLE = "Title";
    public static final String SCHEDULE_START_DATE = "StartDate";
    public static final String SCHEDULE_START_TIME = "StartTime";
    public static final String SCHEDULE_END_DATE = "EndDate";
    public static final String SCHEDULE_END_TIME ="EndTime";
    public static final String SCHEDULE_PLACE = "Place";
    public static final String SCHEDULE_COMMENT = "Comment";

    private static String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME + "("  + COLUMNS_ID +" integer PRIMARY KEY autoincrement, "
            + SCHEDULE_TITLE +" text, "
            + SCHEDULE_START_DATE+ " text, "
            + SCHEDULE_START_TIME+ " text, "
            + SCHEDULE_END_DATE + " text, "
            + SCHEDULE_END_TIME + " text, "
            + SCHEDULE_PLACE + " text, "
            + SCHEDULE_COMMENT + " text)" ;

    private String[] projection = {};
    private String selelction = "";
    private String[] selectionArgs = {};
    private String sortOrder = COLUMNS_ID + " ASC";

    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public ScheduleDB(DatabaseHelper databaseHelper){
        this.dbHelper = databaseHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void createTable(){
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }database.execSQL(CREATE_TABLE);
    }

    public void insertRecord(Schedule schedule){
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        ContentValues val = new ContentValues();
        val.put(SCHEDULE_TITLE, schedule.getTitle());
        val.put(SCHEDULE_START_DATE, schedule.getStartDate());
        val.put(SCHEDULE_START_TIME, schedule.getStartTime());
        val.put(SCHEDULE_END_DATE, schedule.getEndDate());
        val.put(SCHEDULE_END_TIME, schedule.getEndTime());
        val.put(SCHEDULE_PLACE, schedule.getPlace());
        val.put(SCHEDULE_COMMENT, schedule.getComment());

        database.insert(TABLE_NAME, null, val);
    }

    public void updateRecord(Schedule schedule){
        ContentValues val = new ContentValues();

        val.put(SCHEDULE_TITLE, schedule.getTitle());
        val.put(SCHEDULE_START_DATE, schedule.getStartDate());
        val.put(SCHEDULE_START_TIME, schedule.getStartTime());
        val.put(SCHEDULE_END_DATE, schedule.getEndDate());
        val.put(SCHEDULE_END_TIME, schedule.getEndTime());
        val.put(SCHEDULE_PLACE, schedule.getPlace());
        val.put(SCHEDULE_COMMENT, schedule.getComment());

        String selection = COLUMNS_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(schedule.getKey())};

        int count = database.update(TABLE_NAME, val, selection, selectionArgs);
    }

    public void deleteRecord(Schedule schedule){
        String selection = COLUMNS_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(schedule.getKey())};
        int deleteRows = database.delete(TABLE_NAME, selection, selectionArgs);

    }

    //스케쥴 전체 다 불러오기
    public ArrayList<Schedule> lookUpSchedule(){
        ArrayList<Schedule> items = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, sortOrder);

        while(cursor.moveToNext()){
           Schedule schedule = new Schedule();
           schedule.setKey(cursor.getInt(0));
           schedule.setTitle(cursor.getString(1));
           schedule.setStartDate(cursor.getString(2));
           schedule.setStartTime(cursor.getString(3));
           schedule.setEndDate(cursor.getString(4));
           schedule.setEndTime(cursor.getString(5));
           schedule.setPlace(cursor.getString(6));
           schedule.setComment(cursor.getString(7));
           items.add(schedule);
        }
        cursor.close();
        return items;
    }

    public ArrayList<Schedule> getSchedule(String date){
        ArrayList<Schedule> items = new ArrayList<>();
        Schedule schedule;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + SCHEDULE_START_DATE +" <= "+ date
        + " AND " +SCHEDULE_END_DATE+" >= " + date;
        Cursor cursor = database.rawQuery(query,null);
        println(query);

        while(cursor.moveToNext()){
            schedule = new Schedule();
            schedule.setKey(cursor.getInt(0));
            schedule.setTitle(cursor.getString(1));
            schedule.setStartDate(cursor.getString(2));
            schedule.setStartTime(cursor.getString(3));
            schedule.setEndDate(cursor.getString(4));
            schedule.setEndTime(cursor.getString(5));
            schedule.setPlace(cursor.getString(6));
            schedule.setComment(cursor.getString(7));
            items.add(schedule);
        }
        cursor.close();
        return items;
    }

}
