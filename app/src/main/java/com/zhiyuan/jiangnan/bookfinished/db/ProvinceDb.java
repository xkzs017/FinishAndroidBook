package com.zhiyuan.jiangnan.bookfinished.db;

/**
 * Created by wanminfei on 2016/3/16.
 */
public class ProvinceDb {
    static private String table_create ="create table province ("
            + "id integer primary key autoincrement," +
            "province_name text" +
            "province_code text)";

    public static String tableName = "province";
    public static String stringId  = "id";
    public static String stringProvinceName = "province_name";
    public static String stringProvinceCode = "province_code";

    private String province_name;
    private String province_code;
    private int    id;

    static public String getTableCreate()
    {
        return table_create;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getProvince_code() {
        return province_code;
    }

    public void setProvince_code(String province_code) {
        this.province_code = province_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
