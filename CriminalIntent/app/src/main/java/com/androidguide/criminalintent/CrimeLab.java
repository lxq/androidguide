package com.androidguide.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidguide.criminalintent.db.CrimeBaseHelper;
import com.androidguide.criminalintent.db.CrimeCursorWrapper;
import com.androidguide.criminalintent.db.CrimeSchema;
import com.androidguide.criminalintent.db.CrimeSchema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 单件类
public class CrimeLab {
    private static  CrimeLab sCrimeLab;

    private Context mContext;
    //SQLite
    private SQLiteDatabase mDatabase;

    // 获取单件实例
    public static CrimeLab get(Context ctx) {
        if (null == sCrimeLab) {
            sCrimeLab = new CrimeLab(ctx);
        }
        return  sCrimeLab;
    }
    // 单件类不允许外部构建
    private CrimeLab(Context ctx) {
        // 这里使用APP Context的原因是，数据库是进程存在期间都需要操作的；
        // 而Activity的Context会有自己的生命周期，不能满足在整个应用中使用db的要求.
        mContext = ctx.getApplicationContext();
        // 创建可写的SQLite db
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        // 查询所有记录
        CrimeCursorWrapper cursor = query(null, null);
        // 通过游标进行遍历操作
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return crimes;
    }

    public Crime getCrime(UUID id) {
        // 关键字查询
        CrimeCursorWrapper cursor = query(CrimeTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        try {
            if (0 == cursor.getCount()) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
   }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        // 插入SQLite的表中
        mDatabase.insert(CrimeTable.NAME, null, values);
    }

    // 更新表记录
    public void update(Crime c) {
        String uuid = c.getId().toString();
        ContentValues values = getContentValues(c);
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Cols.UUID + " = ?",
                new String[]{uuid});
    }

    // 查询表数据
    private CrimeCursorWrapper query(String clause, String[] args) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,// null表示全部列
                clause,
                args,
                null, // groupBy
                null,
                null
        );

        return new CrimeCursorWrapper(cursor);
    }

    // 一条记录的值
    private static ContentValues getContentValues(Crime c) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Cols.UUID, c.getId().toString());
        values.put(CrimeTable.Cols.TITLE, c.getTitle());
        values.put(CrimeTable.Cols.DATE, c.getDate().toString());
        values.put(CrimeTable.Cols.SOLVED, c.isSolved()?1:0);
        return values;
    }
}
