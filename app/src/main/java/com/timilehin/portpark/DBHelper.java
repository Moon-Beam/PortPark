package com.timilehin.portpark;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "CarPark.db";
    public static final String TABLE_NAME = "carLocations";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreate = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_LATITUDE + " BLOB, "
                + COLUMN_LONGITUDE + " BLOB);";
        sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sqlUpgrade = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(sqlUpgrade);
        onCreate(sqLiteDatabase);
    }

    public boolean saveLocation(Double lat, Double lng){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_LATITUDE, lat);
        contentValues.put(COLUMN_LONGITUDE, lng);

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public Cursor getLocation(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        String sqlRead = "SELECT * From " + TABLE_NAME + ";";
        return sqLiteDatabase.rawQuery(sqlRead, null);
//        Cursor cursor = sqLiteDatabase.query(TABLE, allColluns, null, null, null, null, ID +" DESC", "1");
//        return sql;
    }
}
