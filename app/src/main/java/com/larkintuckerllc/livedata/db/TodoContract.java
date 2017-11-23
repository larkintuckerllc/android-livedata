package com.larkintuckerllc.livedata.db;

import android.provider.BaseColumns;

public class TodoContract {

    public static final String DB_NAME = "com.larkintuckerllc.livedata.db";
    public static final int DB_VERSION = 1;

    public class TodoEntry implements BaseColumns {

        public static final String TABLE = "todos";
        public static final String COL_TODO_NAME = "name";
        public static final String COL_TODO_DATE = "date";

    }

}
