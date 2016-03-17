package com.zhiyuan.jiangnan.bookfinished.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanminfei on 2016/3/17.
 * 操作全国地区数据库的工厂处理函数
 */
public class CityFactory {
    //db name
    private static final String dbName = "weather";

    //数据库版本
    private static final int curVersion = 1;

    private static CityFactory cityFactory;

    private SQLiteDatabase db;

    /**
     * 私有化的构造函数，用于单实例情景
     */
    public CityFactory(Context context) {
        DbOpenHelper dbOpenHelper = new DbOpenHelper(context,this.dbName,null, this.curVersion);
        db = dbOpenHelper.getWritableDatabase();
    }

    /**
     * 获取CityFactory的单实例
     * 增加synchronized标志，同步区块，仅有一个线程可在进程中运行
     */
    public synchronized static CityFactory getInstance(Context context)
    {
        if( cityFactory == null)
        {
            cityFactory = new CityFactory(context);
        }
        return cityFactory;
    }

    /**
     * 将省份存储到数据库
     */
    public void saveProvince(ProvinceDb provinceDb)
    {
        if(provinceDb != null)
        {
            ContentValues values = new ContentValues();
            values.put(ProvinceDb.stringProvinceName, provinceDb.getProvince_name());
            values.put(ProvinceDb.stringProvinceCode, provinceDb.getProvince_code());
            db.insert(ProvinceDb.tableName,null,values);
        }
        return;
    }
    /**
     * 从数据库读取全国所有省份的信息
     */
    public List<ProvinceDb> loadProvinces()
    {
        List<ProvinceDb> list = new ArrayList<ProvinceDb>();
        Cursor cursor = db.query(ProvinceDb.tableName,null,null,null,null,null,null);
        if(cursor.moveToFirst())
        {
            do {
                ProvinceDb provinceDb = new ProvinceDb();
                provinceDb.setId(cursor.getInt(cursor.getColumnIndex(ProvinceDb.stringId)));
                provinceDb.setProvince_name(cursor.getString(cursor.getColumnIndex(ProvinceDb.stringProvinceName)));
                provinceDb.setProvince_code(cursor.getString(cursor.getColumnIndex(ProvinceDb.stringProvinceCode)));
                list.add(provinceDb);
            }while(cursor.moveToNext());
        }
        return list;
    }

    /**
     * 存储city实例到数据库中
     */
    public void saveCity(CityDb cityDb)
    {
        if( cityDb != null)
        {
            ContentValues values = new ContentValues();
            values.put(CityDb.stringCityName, cityDb.getCity_name());
            values.put(CityDb.stringCityCode, cityDb.getCity_code());
            values.put(CityDb.stringProvince_id, cityDb.getProvince_id());
            db.insert(CityDb.tableName, null, values);
        }
        return;
    }

    /**
     * 获取某个省下面所有的城市信息
     */
    public List<CityDb> loadCities( int provinceId)
    {
        List<CityDb> list = new ArrayList<CityDb>();
        Cursor cursor = db.query(CityDb.tableName,null,CityDb.stringProvince_id+" = ?",
                new String[]{ String.valueOf(provinceId)},null,null,null);
        if(cursor.moveToFirst())
        {
            do {
                CityDb cityDb = new CityDb();
                cityDb.setId(cursor.getInt( cursor.getColumnIndex(CityDb.stringId)));
                cityDb.setProvince_id(provinceId);
                cityDb.setCity_code(cursor.getString(cursor.getColumnIndex(CityDb.stringCityCode)));
                cityDb.setCity_name(cursor.getString(cursor.getColumnIndex(CityDb.stringCityName)));
                list.add(cityDb);
            }while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 存储城市下面的城市到数据库
     */
    public void saveCounty(CountyDb countyDb)
    {
        if( countyDb != null )
        {
            ContentValues values = new ContentValues();
            values.put(CountyDb.stringCountyName,countyDb.getCounty_name());
            values.put(CountyDb.stringCountyCode,countyDb.getCounty_code());
            values.put(CountyDb.stringCityId,countyDb.getCity_id());
            db.insert(CountyDb.tableName, null, values);
        }
        return ;
    }

    /**
     * 从数据库读取某城市下所有的县信息
     */
    public List<CountyDb> loadCounties(int cityId)
    {
        List<CountyDb> list = new ArrayList<CountyDb>();
        Cursor cursor = db.query(CountyDb.tableName,null,CountyDb.stringCityId+" = ?",
                new String[]{String.valueOf(cityId)},null,null,null);
        if( cursor.moveToFirst())
        {
            do {
                CountyDb countyDb = new CountyDb();
                countyDb.setId(cursor.getInt(cursor.getColumnIndex(CountyDb.stringId)));
                countyDb.setCity_id(cursor.getInt(cursor.getColumnIndex(CountyDb.stringCityId)));
                countyDb.setCounty_code(cursor.getString(cursor.getColumnIndex(CountyDb.stringCountyCode)));
                countyDb.setCounty_name(cursor.getString(cursor.getColumnIndex(CountyDb.stringCountyName)));
            }while ( cursor.moveToNext());
        }
        return list;
    }
}
