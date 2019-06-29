package bwie.com.yanggaofeng20190629.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * date:2019/6/29
 * name:windy
 * function: 存储数据
 */
public class SpUtil {

    /**
     * 存储 用户信息
     *
     * @param context
     * @param userId
     * @param sessionId
     */
    public static void saveUserInfo(Context context, String userId, String sessionId) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putString("userId", userId);
        edit.putString("sessionId", sessionId);

        edit.commit();
    }

    /**
     * 存储二级列表id  用来获取二级列表中的商品详情
     *
     * @param context
     * @param id
     */
    public static void saveSecondId(Context context, String id) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("secondId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("id", id);
        edit.commit();
    }
}
