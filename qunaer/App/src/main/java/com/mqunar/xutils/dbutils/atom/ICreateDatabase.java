package com.mqunar.xutils.dbutils.atom;

import android.database.sqlite.SQLiteDatabase;
import com.mqunar.xutils.dbutils.DbUtils.DaoConfig;

public interface ICreateDatabase {
    SQLiteDatabase createDatabase(DaoConfig daoConfig);
}
