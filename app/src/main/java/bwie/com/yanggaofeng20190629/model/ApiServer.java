package bwie.com.yanggaofeng20190629.model;

import java.util.List;

import bwie.com.yanggaofeng20190629.common.Constant;
import bwie.com.yanggaofeng20190629.entity.FirstEntity;
import bwie.com.yanggaofeng20190629.entity.Result;
import bwie.com.yanggaofeng20190629.entity.SecondEntity;
import bwie.com.yanggaofeng20190629.entity.SecondGoods;
import bwie.com.yanggaofeng20190629.entity.Shops;
import bwie.com.yanggaofeng20190629.entity.UserInfo;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public interface ApiServer {

    //注册
    @FormUrlEncoded
    @POST(Constant.REGISTER_URL)
    Observable<Result> register(@Field("phone") String phone, @Field("pwd") String pwd);

    //登录
    @FormUrlEncoded
    @POST(Constant.LOGIN_URL)
    Observable<Result<UserInfo>> login(@Field("phone") String phone, @Field("pwd") String pwd);

    //一级列表
    @GET(Constant.FIRST_URL)
    Observable<Result<List<FirstEntity>>> firstList();

    //二级列表
    @GET(Constant.SECOND_URL)
    Observable<Result<List<SecondEntity>>> secondList(@Query("firstCategoryId") String firstCategoryId);

    //二级商品
    @GET(Constant.SECOND_GOODS_URL)
    Observable<Result<List<SecondGoods>>> showList(@Query("categoryId") String categoryId,
                                                   @Query("page") String page,
                                                   @Query("count") String count);

    //同步购物车
    @PUT(Constant.SYNC_URL)
    Observable<Result> syncCart(@Header("userId") String userId,
                                @Header("sessionId") String sessionId,
                                @Query("data") String data);

    //展示购物车
    @GET(Constant.CART_URL)
    Observable<Result<List<Shops>>> showCart(@Header("userId") String userId,
                                             @Header("sessionId") String sessionId);
}
