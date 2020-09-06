package com.suri5.clubmngmt.Schedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Budget.Budget;
import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DatabaseHelper;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class ScheduleDB {

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
        }database.execSQL(Constant.SCHEDULE_CREATE_TABLE);
    }

    public void insertRecord(Schedule schedule){
        if(database == null){
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }
        ContentValues val = new ContentValues();
        val.put(Constant.SCHEDULE_COLUMN_TITLE, schedule.getTitle());
        val.put(Constant.SCHEDULE_COLUMN_START_DATE, schedule.getStartDate());
        val.put(Constant.SCHEDULE_COLUMN_START_TIME, schedule.getStartTime());
        val.put(Constant.SCHEDULE_COLUMN_END_DATE, schedule.getEndDate());
        val.put(Constant.SCHEDULE_COLUMN_END_TIME, schedule.getEndTime());
        val.put(Constant.SCHEDULE_COLUMN_PLACE, schedule.getPlace());
        val.put(Constant.SCHEDULE_COLUMN_COMMENT, schedule.getComment());

        database.insert(Constant.SCHEDULE_TABLE_TITLE, null, val);
    }

    public void updateRecord(Schedule schedule){
        ContentValues val = new ContentValues();

        val.put(Constant.SCHEDULE_COLUMN_TITLE, schedule.getTitle());
        val.put(Constant.SCHEDULE_COLUMN_START_DATE, schedule.getStartDate());
        val.put(Constant.SCHEDULE_COLUMN_START_TIME, schedule.getStartTime());
        val.put(Constant.SCHEDULE_COLUMN_END_DATE, schedule.getEndDate());
        val.put(Constant.SCHEDULE_COLUMN_END_TIME, schedule.getEndTime());
        val.put(Constant.SCHEDULE_COLUMN_PLACE, schedule.getPlace());
        val.put(Constant.SCHEDULE_COLUMN_COMMENT, schedule.getComment());

        String selection = Constant.SCHEDULE_COLUMN_PK + " LIKE ?";
        String[] selectionArgs = {Integer.toString(schedule.getKey())};

        int count = database.update(Constant.SCHEDULE_TABLE_TITLE, val, selection, selectionArgs);
    }

    public void deleteRecord(Schedule schedule){
        String selection = Constant.SCHEDULE_COLUMN_PK + " LIKE ?";
        String[] selectionArgs = {Integer.toString(schedule.getKey())};
        int deleteRows = database.delete(Constant.SCHEDULE_TABLE_TITLE, selection, selectionArgs);

    }

    //스케쥴 전체 다 불러오기
    public ArrayList<Schedule> lookUpSchedule(){
        ArrayList<Schedule> items = new ArrayList<>();
        Cursor cursor = database.query(Constant.SCHEDULE_TABLE_TITLE, null, null, null, null, null, null);

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

        String query = "SELECT * FROM " + Constant.SCHEDULE_TABLE_TITLE + " WHERE " + Constant.SCHEDULE_COLUMN_START_DATE +" <= "+ date
        + " AND " +Constant.SCHEDULE_COLUMN_END_DATE+" >= " + date + " ORDER by " + Constant.SCHEDULE_COLUMN_START_DATE + " ASC , " + Constant.SCHEDULE_COLUMN_START_TIME +" ASC";
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
