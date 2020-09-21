package com.suri5.clubmngmt.Schedule;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Common.Constant;
import com.suri5.clubmngmt.Common.DBManager;
import com.suri5.clubmngmt.Common.DatabaseHelper;
import com.suri5.clubmngmt.Person.Person;

import java.util.ArrayList;

import static com.suri5.clubmngmt.Common.DatabaseHelper.println;

public class ScheduleDBManager implements DBManager<Schedule> {
    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public ScheduleDBManager(DatabaseHelper databaseHelper){
        this.dbHelper = databaseHelper;
        database = dbHelper.getWritableDatabase();
    }

    @Override
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

    @Override
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

    @Override
    public ArrayList<Schedule> findRecordFromName(String string) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        String selection = Constant.SCHEDULE_COLUMN_TITLE + " LIKE ?";
        String[] selectionArgs = {"%" + string + "%"};
        DatabaseHelper.println("조회 : " +selection + " " +selectionArgs[0]);

        Cursor cursor  = database.query(Constant.PERSON_TABLE_TITLE, null, selection, selectionArgs, null,null, null);

        if(cursor.getCount() > 0){
            Schedule s;

            while(cursor.moveToNext()){
                s = new Schedule();
                s.setKey(cursor.getInt(0));
                s.setTitle(cursor.getString(1));
                s.setStartDate(cursor.getString(2));
                s.setStartTime(cursor.getString(3));
                s.setEndDate(cursor.getString(4));
                s.setEndTime(cursor.getString(5));
                s.setPlace(cursor.getString(6));
                s.setComment(cursor.getString(7));
                schedules.add(s);
            }
            cursor.close();
        }
        else{
            DatabaseHelper.println("조회 결과 : + " + string + " 없음");
        }
        cursor.close();
        return schedules;
    }

    @Override
    public void deleteRecord(int pk){
        String selection = Constant.SCHEDULE_COLUMN_PK + " LIKE ?";
        String[] selectionArgs = {Integer.toString(pk)};
        int deleteRows = database.delete(Constant.SCHEDULE_TABLE_TITLE, selection, selectionArgs);
    }

    public ArrayList<Schedule> findRecordFromDate(String date) {
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

    @Override
    public ArrayList<Schedule> findRecord(String column, String data) {
        ArrayList<Schedule> schedules = new ArrayList<>();
        Schedule schedule;
        String selection = column + " LIKE ?";
        String[] selectionArgs = {"%" + data+ "%"};
        DatabaseHelper.println("조회 : " +selection + " " +selectionArgs[0]);

        Cursor cursor  = database.query(Constant.SCHEDULE_TABLE_TITLE, null, selection, selectionArgs, null,null, null);

        if(cursor.getCount() > 0){
            Person p;

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
                schedules.add(schedule);
            }
        }
        else{
            DatabaseHelper.println("조회 결과 : + " +data+ " 없음");
        }
        cursor.close();
        return schedules;
    }

    @Override
    public ArrayList<Schedule> getAllRecord() {
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

    @Override
    public int getRecordCount() {
        Cursor cursor=database.query(Constant.SCHEDULE_TABLE_TITLE, null, null, null, null, null, null);
        return cursor.getCount();
    }
}
