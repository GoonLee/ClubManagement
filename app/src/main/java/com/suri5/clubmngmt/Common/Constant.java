package com.suri5.clubmngmt.Common;

public class Constant {
    private Constant(){}

    public static final int REQUEST_CODE_GET_IMAGE = 101;
    public static final int RESULT_SAVE = 102;
    public static final int RESULT_OK_FROM_GROUPMEMBEREDITACTIVITY = 111;

    public static final String DATABASE_NAME = "ClubManagementDB";

    public static final String PERSON_TABLE_TITLE = "Member";
    public static final String PERSON_COLUMN_PK = "id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_IDNUM = "Id_num";
    public static final String PERSON_COLUMN_MAJOR = "Major";
    public static final String PERSON_COLUMN_GENDER = "Gender";
    public static final String PERSON_COLUMN_BIRTHDAY = "Birthday";
    public static final String PERSON_COLUMN_MOBILE = "Mobile";
    public static final String PERSON_COLUMN_EMAIL = "Email";
    public static final String PERSON_COLUMN_PICTURE = "Image";


    public static final String GROUP_PERSON_TABLE_TITLE = "Group_Person";
    public static final String GROUP_PERSON_COLUMN_PERSONKEY = "Person_key";
    public static final String GROUP_PERSON_COLUMN_GROUPKEY = "Group_key";

    public static final String GROUP_TABLE_TITLE = "Groups";
    public static final String GROUP_COLUMN_PK = "_id";
    public static final String GROUP_COLUMN_NAME = "Name";
    public static final String GROUP_COLUMN_TOTAL = "TotalNum";



    public static final String PERSON_CREATE_TABLE = "create table if not exists "
            + PERSON_TABLE_TITLE + "("  + PERSON_COLUMN_PK +" integer PRIMARY KEY autoincrement, "
            + PERSON_COLUMN_NAME +" text, "
            + PERSON_COLUMN_IDNUM + " integer, "
            + PERSON_COLUMN_MAJOR + " text, "
            + PERSON_COLUMN_GENDER + " text, "
            + PERSON_COLUMN_BIRTHDAY + " text, "
            + PERSON_COLUMN_MOBILE + " text, "
            + PERSON_COLUMN_EMAIL + " text,"
            + PERSON_COLUMN_PICTURE + " blob)" ;

    public static String GROUP_CREATE_TABLE = "create table if not exists "
            + GROUP_TABLE_TITLE + "("  + GROUP_COLUMN_PK +" integer PRIMARY KEY autoincrement, "
            + GROUP_COLUMN_NAME +" text UNIQUE, "
            + GROUP_COLUMN_TOTAL + " integer)";

    public static final String GROUP_PERSON_CREATE_TABLE = "create table if not exists "
            +GROUP_PERSON_TABLE_TITLE + "("
            + GROUP_PERSON_COLUMN_PERSONKEY+ " integer, "
            + GROUP_PERSON_COLUMN_GROUPKEY+ " integer, "
            + " constraint person_k foreign key(" + GROUP_PERSON_COLUMN_PERSONKEY + ")"
            + " references " + PERSON_TABLE_TITLE +"(" + PERSON_COLUMN_PK + ") "
            + " on delete cascade, "
            + " constraint group_k foreign key(" + GROUP_PERSON_COLUMN_GROUPKEY + ")"
            + " references " + GROUP_TABLE_TITLE +"(" + GROUP_COLUMN_PK + ")"
            + " on delete cascade"
            +")";


    public static final String SCHEDULE_TABLE_TITLE = "Schedule";
    public static final String BUDGET_TABLE_TITLE = "Budget";
}