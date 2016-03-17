package com.zhiyuan.jiangnan.bookfinished.db;

/**
 * Created by wanminfei on 2016/3/16.
 */
public class CountyDb {
    static private String table_create = "create table county (" +
            "id integer primary key autoincrement," +
            "county_name text" +
            "county_code text" +
            "city_id integer)";

    public static String tableName = "county";
    public static String stringId = "id";
    public static String stringCountyName = "county_name";
    public static String stringCountyCode = "county_code";
    public static String stringCityId     = "city_id";

    private String county_name;
    private String county_code;
    private int city_id;
    private int    id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    static public String getTableCreate()
    {
        return table_create;
    }

    public String getCounty_name() {
        return county_name;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }

    public String getCounty_code() {
        return county_code;
    }

    public void setCounty_code(String county_code) {
        this.county_code = county_code;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }
}
