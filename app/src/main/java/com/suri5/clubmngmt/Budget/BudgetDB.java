package com.suri5.clubmngmt.Budget;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.suri5.clubmngmt.Common.DatabaseHelper;

import java.util.ArrayList;

public class BudgetDB {
    public static final String DATABASE_NAME = "ClubManagementDB";
    public static final String TABLE_NAME = "Budget";
    public static final String COLUMNS_ID = "_id";
    public static final String BUDGET_TITLE = "Title";
    public static final String BUDGET_DATE = "Date";
    public static final String BUDGET_ISINCOME = "IsIncome";
    public static final String BUDGET_AMOUNT = "Amount";
    public static final String BUDGET_COMMENT = "Comment";

    private static String CREATE_TABLE = "create table if not exists "
            + TABLE_NAME + "("  + COLUMNS_ID +" integer PRIMARY KEY autoincrement, "
            + BUDGET_TITLE +" text, "
            + BUDGET_DATE + " text, "
            + BUDGET_ISINCOME + " integer, "
            + BUDGET_AMOUNT + " integer, "
            + BUDGET_COMMENT + " text)" ;

    private String[] projection = {};
    private String selelction = "";
    private String[] selectionArgs = {};
    private String sortOrder = COLUMNS_ID + " ASC";


    SQLiteDatabase database;
    DatabaseHelper dbHelper;

    public BudgetDB(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
        database = dbHelper.getWritableDatabase();
    }

    public void createTable(){
        database.execSQL(CREATE_TABLE);
    }

    public void insertRecord(Budget budget){
        ContentValues val = new ContentValues();
        val.put(BUDGET_TITLE, budget.getTitle());
        val.put(BUDGET_DATE, budget.getDate());
        val.put(BUDGET_AMOUNT, budget.getAmount());
        if(budget.isIncome()){
            val.put(BUDGET_ISINCOME, 1);
        } else{
            val.put(BUDGET_ISINCOME, 0);
        }
        val.put(BUDGET_COMMENT, budget.getComment());

        database.insert(TABLE_NAME, null, val);
    }

    public void deleteRecord(Budget budget){
        String selection = COLUMNS_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(budget.getKey())};
        int deleteRows = database.delete(TABLE_NAME, selection, selectionArgs);
    }

    public void updateRecord(Budget budget){
        ContentValues val = new ContentValues();
        val.put(BUDGET_TITLE, budget.getTitle());
        val.put(BUDGET_DATE, budget.getDate());
        if(budget.isIncome()){
            val.put(BUDGET_ISINCOME, 1);
        } else{
            val.put(BUDGET_ISINCOME, 0);
        }
        val.put(BUDGET_AMOUNT, budget.getAmount());
        val.put(BUDGET_COMMENT, budget.getComment());

        String selection = COLUMNS_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(budget.getKey())};
        int count = database.update(TABLE_NAME, val, selection, selectionArgs);
    }

    public ArrayList<Budget> lookUpBudget(){
        ArrayList<Budget> items = new ArrayList<>();
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, sortOrder);

        while(cursor.moveToNext()){
            Budget budget = new Budget();
            budget.setKey(cursor.getInt(0));
            budget.setTitle(cursor.getString(1));
            budget.setDate(cursor.getString(2));
            if(cursor.getInt(3)==1){
                budget.setIncome(true);
            } else{
                budget.setIncome(false);
            }
            budget.setAmount(cursor.getInt(4));
            budget.setComment(cursor.getString(5));
            items.add(budget);
        }
        cursor.close();
        return items;
    }
}
