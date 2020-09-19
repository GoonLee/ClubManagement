package com.suri5.clubmngmt.Common;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;

import java.util.ArrayList;

public abstract class DBManager<T extends Parcelable> {
    protected SQLiteDatabase database;
    protected DatabaseHelper dbHelper;

    public DBManager(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
        database = dbHelper.getWritableDatabase();
    }

    public abstract void insertRecord(T record);

    public abstract void deleteRecord(int pk);

    public abstract void updateRecord(T record);

    public abstract ArrayList<T> findRecordFromName(String string);

    public abstract ArrayList<T> findRecord(String column, String data);

    public abstract ArrayList<T> getAllRecord();

    public abstract int getRecordCount();
}
