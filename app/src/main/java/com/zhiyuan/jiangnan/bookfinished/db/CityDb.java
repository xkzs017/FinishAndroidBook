package com.zhiyuan.jiangnan.bookfinished.db;

/**
 * Created by wanminfei on 2016/3/16.
 */
public class CityDb {
    static private String table_create = "create table city (" +
            "id integer primary key autoincrement," +
            "city_name text," +
            "city_code text," +
            "province_id integer)";

    public static String stringId = "id";
    public static String stringCityName = "city_name";
    public static String stringCityCode = "city_code";
    public static String stringProvince_id = "province_id";
    public static String tableName = "city";
    private String city_name;
    private String city_code;
    private int    province_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int    id;

    static public String getTableCreate()
    {
        return table_create;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_code() {
        return city_code;
    }

    public void setCity_code(String city_code) {
        this.city_code = city_code;
    }

    public int getProvince_id() {
        return province_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }
}
