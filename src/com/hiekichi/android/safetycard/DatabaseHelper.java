package com.hiekichi.android.safetycard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    
    private static final String DATABASE_NAME = "kiroku.db";
    private static final int    DATABASE_VERSION = 1; 
    
    private static final String COMMAND_CREATE_TABLE_NAMAES
            = "create table namaes"
            + "  ( _id integer primary key autoincrement,"
            +    " namae text not null,"
            +    " count integer )";

    public DatabaseHelper(Context context) {
        // データベースの作成
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        // テーブルの作成（今回はテーブルが1つだけ）
        db.execSQL(COMMAND_CREATE_TABLE_NAMAES);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // データベースのバージョンが変わったときの処理
        // 　今回は特に指定していません
    }
}
