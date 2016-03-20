package com.zhiyuan.jiangnan.bookfinished.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhiyuan.jiangnan.bookfinished.R;
import com.zhiyuan.jiangnan.bookfinished.db.CityDb;
import com.zhiyuan.jiangnan.bookfinished.db.CityFactory;
import com.zhiyuan.jiangnan.bookfinished.db.CountyDb;
import com.zhiyuan.jiangnan.bookfinished.db.ProvinceDb;
import com.zhiyuan.jiangnan.bookfinished.util.CityUtility;
import com.zhiyuan.jiangnan.bookfinished.util.HttpCallbackListner;
import com.zhiyuan.jiangnan.bookfinished.util.HttpUtil;
import com.zhiyuan.jiangnan.bookfinished.util.WeatherString;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE  = 0;
    public static final int LEVEL_CITY       = 1;
    public static final int LEVEL_COUNTY     = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private CityFactory cityFactory;
    private List<String> dataList = new ArrayList<String>();

    /* province*/
    private List<ProvinceDb> provinceDbs;
    private List<CityDb> cityDbs;
    private List<CountyDb> countyDbs;
    private ProvinceDb selectedProvince;
    private CityDb     selectedCity;
    private int currentLevel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView)findViewById(R.id.title_text);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        cityFactory = CityFactory.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if( currentLevel == LEVEL_PROVINCE)
                {
                    selectedProvince = provinceDbs.get(position);
                    //加载省级市的城市选项数据
                    ChooseAreaActivity.this.queryCities();
                }else if( currentLevel == LEVEL_CITY)
                {
                    selectedCity = cityDbs.get(position);
                    //加载市的城市选项数据
                    ChooseAreaActivity.this.queryCounties();
                }
                else if( currentLevel == LEVEL_COUNTY)
                {
                    String countyCode = countyDbs.get(position).getCounty_code();
                    Intent intent = new Intent(ChooseAreaActivity.this, ShowCityWeather.class);
                    intent.putExtra(WeatherString.CountyCode,countyCode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //加载省级数据
        this.queryProvinces();
    }
    /**
     * 查询全国所有的省，有限从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryProvinces() {
        provinceDbs = cityFactory.loadProvinces();
        if (provinceDbs.size() > 0) {
            //DB has it
            dataList.clear();
            for (ProvinceDb provinceDb : provinceDbs) {
                dataList.add(provinceDb.getProvince_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("中国");
            currentLevel = ChooseAreaActivity.LEVEL_PROVINCE;
        } else {
            //到服务器上进行查询
            this.queryFromServer(null,ChooseAreaActivity.LEVEL_PROVINCE);
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCities()
    {
        cityDbs = cityFactory.loadCities(selectedProvince.getId());
        if(cityDbs.size()>0)
        {
            dataList.clear();
            for(CityDb cityDb:cityDbs)
            {
                dataList.add(cityDb.getCity_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvince_name());
            currentLevel = ChooseAreaActivity.LEVEL_CITY;
        }
        else
        {
            //到服务器上进行查询
            this.queryFromServer(selectedProvince.getProvince_code(),ChooseAreaActivity.LEVEL_CITY);
        }
    }
    /**
     * 查询选中市内所有的县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties()
    {
        countyDbs = cityFactory.loadCounties(selectedCity.getId());
        if(countyDbs.size() > 0)
        {
            dataList.clear();
            for(CountyDb countyDb : countyDbs)
            {
                dataList.add(countyDb.getCounty_name());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCity_name());
            currentLevel = ChooseAreaActivity.LEVEL_COUNTY;
        }
        else
        {
            //到服务器上进行查询
            this.queryFromServer(selectedCity.getCity_code(),ChooseAreaActivity.LEVEL_COUNTY);
        }
    }
    /**
     * 从服务器上查询省市县数据
     */
    private void queryFromServer(String code, final int type)
    {
        String address;
        if(!TextUtils.isEmpty(code))
        {
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }
        else
        {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        this.showProgressDialoy();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListner() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                switch (type) {
                    case ChooseAreaActivity.LEVEL_PROVINCE: {
                        result = CityUtility.handleProvinceResponse(cityFactory, response);
                        break;
                    }
                    case ChooseAreaActivity.LEVEL_CITY: {
                        result = CityUtility.handleCitiesResponse(cityFactory, response, selectedProvince.getId());
                        break;
                    }
                    case ChooseAreaActivity.LEVEL_COUNTY: {
                        result = CityUtility.handleCountiesResponse(cityFactory, response, selectedCity.getId());
                        break;
                    }
                    default: {
                        break;
                    }
                }
                if( result == true)
                   {
                     //通过runOnUiThread()方法回到主线程处理逻辑
                     runOnUiThread(new Runnable() {
                     @Override
                      public void run() {
                             //关闭原有的ProgressDialog
                              ChooseAreaActivity.this.closeProgressDialog();
                              switch (type)
                              {
                                 case ChooseAreaActivity.LEVEL_PROVINCE:
                                 {
                                    ChooseAreaActivity.this.queryProvinces();
                                    break;
                                 }
                                 case ChooseAreaActivity.LEVEL_CITY:
                                 {
                                     ChooseAreaActivity.this.queryCities();
                                     break;
                                 }
                                 case ChooseAreaActivity.LEVEL_COUNTY:
                                 {
                                    ChooseAreaActivity.this.queryCounties();
                                 }
                                 default:
                                 {
                                    break;
                                 }
                              }
                              return ;
                          }
                      });
                   }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                ChooseAreaActivity.this.closeProgressDialog();
                                Toast.makeText(ChooseAreaActivity.this,"加载失败,result == false",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }


            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ChooseAreaActivity.this.closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示对话框
     */
    private void showProgressDialoy()
    {
        if(progressDialog == null)
        {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("正在加载");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭对话框
     */
    private void closeProgressDialog()
    {
        if(this.progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }
    /*
    * 捕获back键，根据的当前级别进行判断
     */

    @Override
    public void onBackPressed() {
        if(currentLevel == LEVEL_COUNTY)
        {
            queryCities();
        }else if( currentLevel == LEVEL_CITY)
        {
            queryProvinces();
        }
        else {
            finish();
        }
    }
}
