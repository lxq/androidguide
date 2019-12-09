package com.androidguide.criminalintent.db;

public class CrimeSchema {

    // 创建表的内部类
    public static final class CrimeTable {
        // 表名
        public static final String NAME = "crimes";

        // 字段名称
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String SOLVED = "solved";
        }
    }
}
