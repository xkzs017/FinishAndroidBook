package com.zhiyuan.jiangnan.bookfinished.util;

import android.content.Context;
import android.text.TextUtils;

import com.zhiyuan.jiangnan.bookfinished.db.CityDb;
import com.zhiyuan.jiangnan.bookfinished.db.CityFactory;
import com.zhiyuan.jiangnan.bookfinished.db.CountyDb;
import com.zhiyuan.jiangnan.bookfinished.db.ProvinceDb;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wanminfei on 2016/3/19.
 */
public class CityUtility {
    /**
     * 解析并处理服务器返回的省级数据
     */
    public synchronized static boolean handleProvinceResponse(
            CityFactory cityFactory,String response
    )
    {
        if(!TextUtils.isEmpty(response))
        {
            //解析省级数据
            String[] codeAndNames = response.split(",");
            if(codeAndNames != null && codeAndNames.length > 0)
            {
                //保存所有数据
                for( String codeAndName: codeAndNames)
                {
                    String[] array = codeAndName.split("\\|");
                    ProvinceDb provinceDb = new ProvinceDb();
                    provinceDb.setProvince_code(array[0]);
                    provinceDb.setProvince_name(array[1]);
                    cityFactory.saveProvince(provinceDb);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCitiesResponse(
            CityFactory cityFactory,String response, int provinceId
    )
    {
        if(!TextUtils.isEmpty(response))
        {
            String[] codeAndNames = response.split(",");
            if(codeAndNames != null && codeAndNames.length >0)
            {
                for(String codeAndName : codeAndNames)
                {
                    String[] array = codeAndName.split("\\|");
                    CityDb cityDb = new CityDb();
                    cityDb.setCity_code(array[0]);
                    cityDb.setCity_name(array[1]);
                    cityDb.setProvince_id(provinceId);
                    cityFactory.saveCity(cityDb);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountiesResponse(
            CityFactory cityFactory, String response, int cityId
    )
    {
        if(!TextUtils.isEmpty(response))
        {
            String[] codeAndNames = response.split(",");
            if( codeAndNames != null && codeAndNames.length > 0)
            {
                for( String codeAndName : codeAndNames)
                {
                    String[] array = codeAndName.split("\\|");
                    CountyDb countyDb = new CountyDb();
                    countyDb.setCounty_code(array[0]);
                    countyDb.setCounty_name(array[1]);
                    countyDb.setCity_id(cityId);
                    cityFactory.saveCounty(countyDb);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 解析服务器返回的JSON数据，并将数据返回
     */
    public static JSONObject handleWeatherResponse(Context context,String response)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject(WeatherString.WeatherInfo);
            weatherInfo.getString(WeatherString.City);
            weatherInfo.getString(WeatherString.CityId);
            weatherInfo.getString(WeatherString.Temp1);
            weatherInfo.getString(WeatherString.Temp2);
            weatherInfo.getString(WeatherString.Weather);
            weatherInfo.getString(WeatherString.Ptime);
            return weatherInfo;
        }
        catch ( JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
