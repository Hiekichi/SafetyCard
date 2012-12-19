package jp.ac.iwasaki.android.safetycard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SCDatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "kiroku.db";
    private static final int    DATABASE_VERSION = 1; 
    
    private static final String COMMAND_CREATE_TABLE
            = "create table kirokutbl"
            + "  ( _id integer primary key autoincrement,"
            +    " name     text,"
            +    " tel_no   text,"
            +    " address  text,"
            +    " latitude  text,"
            +    " longitude  text,"
            +    " car_no   text,"
            +    " date     text,"
            +    " time     text,"
            +    " place    text,"
            +    " other    text,"
            +    " photo1   BLOB,"
            +    " photo2   BLOB,"
            +    " photo3   BLOB"
            +    ")";

    public SCDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMAND_CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
