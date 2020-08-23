package com.suri5.clubmngmt.Common;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = Constant.PERSON_TABLE_TITLE;
    //String[] TABLENAME = {"PersonRecords","Groups","ScheduleRecords","AccountRecords"};
    public static int VERSION = 1;

    public DatabaseHelper(Context context){
        super(context,Constant.DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        println("onCreate 호출됨");
    }

    public void onOpen(SQLiteDatabase db){
        println("onOpen 호출됨");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        println("onUpgrade 호출됨 : " +i+" -> "+ i1);
        if(i1 > 1){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        }
    }
    public static void println(String data){
        Log.d("DB" , data);
    }
}