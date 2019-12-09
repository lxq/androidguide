package com.androidguide.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.androidguide.criminalintent.db.CrimeBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 单件类
public class CrimeLab {
    private static  CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

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
        mContext = ctx.getApplicationContext();
        // 创建可写的SQLite db
        mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();

        mCrimes = new ArrayList<>();
        // init
        for (int i = 0; i < 5; i++) {
            Crime e = new Crime();
            e.setTitle("习惯 #" + i);
            e.setSolved((i%2) == 0);
            mCrimes.add(e);
        }
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime e: mCrimes) {
            if (e.getId().equals(id)) {
                return e;
            }
        }
        return null;
    }

    public void addCrime(Crime c) {
        mCrimes.add(c);
    }
}
