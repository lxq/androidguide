package com.androidguide.criminalintent.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.androidguide.criminalintent.Crime;
import com.androidguide.criminalintent.db.CrimeSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        // 获取记录字段值
        String uuid = getString(getColumnIndex(CrimeTable.Cols.UUID));
        String title = getString(getColumnIndex(CrimeTable.Cols.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Cols.DATE));
        int solved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED));

        // 构建crime对象
        Crime c = new Crime(UUID.fromString(uuid));
        c.setTitle(title);
        c.setDate(new Date(date));
        c.setSolved(solved != 0);

        return c;
    }
}
