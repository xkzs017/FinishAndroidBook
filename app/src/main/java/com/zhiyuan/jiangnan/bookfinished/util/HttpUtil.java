package com.zhiyuan.jiangnan.bookfinished.util;

/**
 * Created by wanminfei on 2016/3/17.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 通用HTTP操作类
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallbackListner listner)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try
                {
                    URL url = new URL(address);
                    connection = (HttpURLConnection )url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine()) != null)
                    {
                        response.append(line);
                    }
                    if(listner != null)
                    {
                        listner.onFinish(response.toString());
                    }
                }
                catch (Exception e)
                {
                    if(listner != null)
                    {
                        listner.onError(e);
                    }
                }
                finally {
                    if(connection != null)
                    {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
