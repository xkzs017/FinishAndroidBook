package com.zhiyuan.jiangnan.bookfinished.util;

/**
 * Created by wanminfei on 2016/3/17.
 */
public interface HttpCallbackListner {
    //获取内容正常
    void onFinish(String response);
    //获取内容异常
    void onError(Exception e);
}
