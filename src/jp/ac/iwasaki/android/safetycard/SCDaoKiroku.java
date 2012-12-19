package jp.ac.iwasaki.android.safetycard;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SCDaoKiroku {
    public static final String TABLE_NAME = "kirokutbl";
    public static final String COL_ID        = "_id";
    public static final String COL_NAME      = "name";
    public static final String COL_TEL_NO    = "tel_no";
    public static final String COL_ADDRESS   = "address";
    public static final String COL_LATITUDE  = "latitude";
    public static final String COL_LONGITUDE = "longitude";
    public static final String COL_CAR_NO    = "car_no";
    public static final String COL_DATE      = "date";
    public static final String COL_TIME      = "time";
    public static final String COL_PLACE     = "place";
    public static final String COL_OTHER     = "other";
    public static final String COL_PHOTO1    = "photo1";
    public static final String COL_PHOTO2    = "photo2";
    public static final String COL_PHOTO3    = "photo3";
    
    private static final String[]
          COLS = { COL_ID, COL_NAME, COL_TEL_NO, COL_ADDRESS,
    			   COL_LATITUDE, COL_LONGITUDE, COL_CAR_NO,
    	           COL_DATE,  COL_TIME, COL_PLACE, COL_OTHER,
    	           COL_PHOTO1, COL_PHOTO2, COL_PHOTO3 };

    private SQLiteDatabase db;
    
    // DB取得
    public SCDaoKiroku(SQLiteDatabase db) {
        this.db = db;
    }
    
    // レコードの挿入
    public long insert(SCRecKiroku recKiroku) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME,     recKiroku.getName());
        values.put(COL_TEL_NO,   recKiroku.getTel_no());
        values.put(COL_ADDRESS,  recKiroku.getAddress());
        values.put(COL_LATITUDE,  recKiroku.getLatitude());
        values.put(COL_LONGITUDE,  recKiroku.getLongitude());
        values.put(COL_CAR_NO,   recKiroku.getCar_no());
        values.put(COL_DATE,     recKiroku.getDate());
        values.put(COL_TIME,     recKiroku.getTime());
        values.put(COL_PLACE,    recKiroku.getPlace());
        values.put(COL_OTHER,    recKiroku.getOther());
        values.put(COL_PHOTO1,   recKiroku.getPhoto1());
        values.put(COL_PHOTO2,   recKiroku.getPhoto2());
        values.put(COL_PHOTO3,   recKiroku.getPhoto3());

        String nullColumnHack = null;

        return db.insert(TABLE_NAME, nullColumnHack, values);
    }
    
    // レコードの削除
    public int delete(long _id) {
        String whereClause = COL_ID + " = " + _id;
        String[] whereArgs = null;
        
        return db.delete(TABLE_NAME, whereClause , whereArgs);
    }
    
    // レコードの更新
    public int update(SCRecKiroku recKiroku) {
        ContentValues values = new ContentValues();
        values.put(COL_NAME,     recKiroku.getName());
        values.put(COL_TEL_NO,   recKiroku.getTel_no());
        values.put(COL_ADDRESS,  recKiroku.getAddress());
        values.put(COL_LATITUDE,  recKiroku.getLatitude());
        values.put(COL_LONGITUDE,  recKiroku.getLongitude());
        values.put(COL_CAR_NO,   recKiroku.getCar_no());
        values.put(COL_DATE,     recKiroku.getDate());
        values.put(COL_TIME,     recKiroku.getTime());
        values.put(COL_PLACE,    recKiroku.getPlace());
        values.put(COL_OTHER,    recKiroku.getOther());
        values.put(COL_PHOTO1,   recKiroku.getPhoto1());
        values.put(COL_PHOTO2,   recKiroku.getPhoto2());
        values.put(COL_PHOTO3,   recKiroku.getPhoto3());

        String whereClause = COL_ID + " = " + recKiroku.getId();
        String[] whereArgs = null;
        
        return db.update(TABLE_NAME, values, whereClause, whereArgs);
    }
    
    // 全レコードを含むカーソルを取得
    public Cursor findCursorAll() {
        String   selection = null;
        String[] selectionArgs = null;
        String   groupBy = null;
        String   having = null;
        String   orderBy = null;
        
        Cursor cursor = 
            db.query(TABLE_NAME, COLS, selection, selectionArgs,
                      groupBy, having, orderBy);
        return cursor;
    }

    /*
    public List<SCRecKiroku> getListAll() {
        String   selection = null;
        String[] selectionArgs = null;
        String   groupBy = null;
        String   having = null;
        String   orderBy = null;

        List<SCRecKiroku> list = new ArrayList<SCRecKiroku>();
        Cursor cursor = 
            db.query(TABLE_NAME, COLS, selection, selectionArgs,
                      groupBy, having, orderBy);
        while(cursor.moveToNext()) {
            SCRecKiroku recKiroku =
                new SCRecKiroku(
                		  cursor.getLong(0),
                          cursor.getString(1),
                          cursor.getString(2),
                          cursor.getString(3),
                          cursor.getString(4),
                          cursor.getString(5),
                          cursor.getString(6),
                          cursor.getString(7),
                          cursor.getString(8),
                          cursor.getBlob(9),
                          cursor.getBlob(10),
                          cursor.getBlob(11)
                    );
            list.add(recKiroku);
        }
        
        return list;
    }
	*/
    
    // 指定したIDのレコードを取得
    public SCRecKiroku getRecById(long _id) {
        String   selection = COL_ID + " = " + _id;
        String[] selectionArgs = null;
        String   groupBy = null;
        String   having = null;
        String   orderBy = null;
        
        Cursor cursor = 
            db.query(TABLE_NAME, COLS, selection, selectionArgs,
                      groupBy, having, orderBy);
        cursor.moveToFirst();

        return new SCRecKiroku(
      		  cursor.getLong(0),
              cursor.getString(1),
              cursor.getString(2),
              cursor.getString(3),
              cursor.getString(4),
              cursor.getString(5),
              cursor.getString(6),
              cursor.getString(7),
              cursor.getString(8),
              cursor.getString(9),
              cursor.getString(10),
              cursor.getBlob(11),
              cursor.getBlob(12),
              cursor.getBlob(13)
        );
        
    }
}
