package com.suri5.clubmngmt.Common;

import android.os.Parcelable;

import java.util.ArrayList;

public interface DBManager<T extends Parcelable> {

    public abstract void insertRecord(T record);

    public abstract void deleteRecord(int pk);

    public abstract void updateRecord(T record);

    public abstract ArrayList<T> findRecordFromName(String string);

    public abstract ArrayList<T> findRecord(String column, String data);

    public abstract ArrayList<T> getAllRecord();

    public abstract int getRecordCount();
}
