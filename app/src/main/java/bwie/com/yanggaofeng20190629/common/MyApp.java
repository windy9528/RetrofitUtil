package bwie.com.yanggaofeng20190629.common;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import bwie.com.yanggaofeng20190629.util.SpUtil;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public class MyApp extends Application {

    public static Context context;
    public static SharedPreferences userInfo;
    public static SharedPreferences secondId;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        userInfo = context.getSharedPreferences("userInfo", MODE_PRIVATE);
        secondId = context.getSharedPreferences("secondId", MODE_PRIVATE);

    }

    public static SharedPreferences getUserInfo() {
        return userInfo;
    }

    public static SharedPreferences getSecondId() {
        return secondId;
    }
}
