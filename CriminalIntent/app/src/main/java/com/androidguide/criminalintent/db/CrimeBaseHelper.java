package com.androidguide.criminalintent.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androidguide.criminalintent.db.CrimeSchema.CrimeTable;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "crime.db";

    public CrimeBaseHelper(Context ctx) {
        super(ctx, DB_NAME, null, VERSION);
    }

    // 创建初始数据库
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CrimeTable.NAME +
                "(" +
                "_id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + "," +
                CrimeTable.Cols.TITLE +"," +
                CrimeTable.Cols.DATE + "," +
                CrimeTable.Cols.SOLVED +
                ")");
    }

    // 根据版本更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
