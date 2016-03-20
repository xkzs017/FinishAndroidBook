package com.zhiyuan.jiangnan.bookfinished.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyuan.jiangnan.bookfinished.R;
import com.zhiyuan.jiangnan.bookfinished.util.CityUtility;
import com.zhiyuan.jiangnan.bookfinished.util.HttpCallbackListner;
import com.zhiyuan.jiangnan.bookfinished.util.HttpUtil;
import com.zhiyuan.jiangnan.bookfinished.util.WeatherString;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ShowCityWeather extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout weatherInfoLayout;
    //城市名字
    private TextView cityNameText;
    //天气描述
    private TextView publishTimeText;
    //天气描述信息
    private TextView weatherDespText;
    //显示气温1
    private TextView temp1Text;
    //显示气温2
    private TextView temp2Text;
    //显示当前日期
    private TextView currentDateText;
    //切换城市按钮
    private Button switchCity;
    //更新天气按钮
    private Button refreshWeather;
    private String weatherCode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_show_city_weather);

        //初始化控件
        this.weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
        cityNameText = (TextView)findViewById(R.id.city_name);
        publishTimeText = (TextView)findViewById(R.id.publish_text);
        weatherDespText = (TextView)findViewById(R.id.weather_desp);
        temp1Text = (TextView)findViewById(R.id.temp1);
        temp2Text = (TextView)findViewById(R.id.temp2);
        currentDateText = (TextView)findViewById(R.id.current_date);
        switchCity = (Button)findViewById(R.id.switch_city);
        refreshWeather = (Button)findViewById(R.id.refresh_weather);
        String countyCode = getIntent().getStringExtra(WeatherString.CountyCode);
        if(!TextUtils.isEmpty(countyCode))
        {
            //有县级代号时就去查询天气
            publishTimeText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            //查询天气代号
            queryWeatherCode(countyCode);
        }
        else
        {
            Toast.makeText(this,"not selected county",Toast.LENGTH_SHORT).show();
        }
        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.switch_city:
            {
                Intent intent = new Intent(this,ChooseAreaActivity.class);
                intent.putExtra(WeatherString.FromWeatherActivity,true);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.refresh_weather:
            {
                publishTimeText.setText("同步中...");
                if(!TextUtils.isEmpty(this.weatherCode))
                {
                    queryWeatherInfo(this.weatherCode);
                }
                break;
            }
            default:
            {
                break;
            }
        }
    }
    /**
     * 查询县级代号所对应的天气代号
     */
    private void queryWeatherCode(String countyCode)
    {
        String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
        queryFromServer(address,"countyCode");
        return;
    }
    private void queryWeatherInfo(String weatherCode)
    {
        String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        this.weatherCode = weatherCode;
        queryFromServer(address,"weatherCode");
    }

    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息
     */
    private void queryFromServer(String address, final String type)
    {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListner() {
            @Override
            public void onFinish(String response) {
                if("countyCode".equals(type))
                {
                    if(!TextUtils.isEmpty(response))
                    {
                        String[] arrays = response.split("\\|");
                        if(arrays != null && arrays.length == 2)
                        {
                            String weatherCode = arrays[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if("weatherCode".equals(type))
                {
                    final JSONObject weather = CityUtility.handleWeatherResponse(ShowCityWeather.this, response);
                    if(weather != null)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather(weather);
                            }
                        });
                    }
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishTimeText.setText("同步失败!");
                    }
                });
            }
        });
    }

    private void showWeather(JSONObject weather) {
        try {
            cityNameText.setText(weather.getString(WeatherString.City));
            temp1Text.setText(weather.getString(WeatherString.Temp1));
            temp2Text.setText(weather.getString(WeatherString.Temp2));
            weatherDespText.setText(weather.getString(WeatherString.Weather));
            publishTimeText.setText("今天"+weather.getString(WeatherString.Ptime)+"发布");
            //currentDateText.setText();
            weatherInfoLayout.setVisibility(View.VISIBLE);
            cityNameText.setVisibility(View.VISIBLE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
