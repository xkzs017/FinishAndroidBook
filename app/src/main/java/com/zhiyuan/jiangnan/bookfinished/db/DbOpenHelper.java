package com.zhiyuan.jiangnan.bookfinished.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wanminfei on 2016/3/17.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建各个表
        db.execSQL(ProvinceDb.getTableCreate());
        db.execSQL(CityDb.getTableCreate());
        db.execSQL(CountyDb.getTableCreate());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //版本更新
    }
}
