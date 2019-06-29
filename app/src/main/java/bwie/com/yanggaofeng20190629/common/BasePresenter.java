package bwie.com.yanggaofeng20190629.common;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.List;

import javax.security.auth.login.LoginException;

import bwie.com.yanggaofeng20190629.entity.FirstEntity;
import bwie.com.yanggaofeng20190629.entity.Result;
import bwie.com.yanggaofeng20190629.entity.SecondEntity;
import bwie.com.yanggaofeng20190629.entity.UserInfo;
import bwie.com.yanggaofeng20190629.model.ApiServer;
import bwie.com.yanggaofeng20190629.util.RetrofitUtil;
import bwie.com.yanggaofeng20190629.util.SpUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public abstract class BasePresenter {

    private ImplView implView;

    public BasePresenter(ImplView implView) {
        this.implView = implView;
    }

    @SuppressLint("CheckResult")
    public void requestData(final Object... args) {
        final ApiServer apiServer = RetrofitUtil.getInstance().create(ApiServer.class);

        Observable<Result> register = apiServer.register("15751515963", "123");

        register.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Result>() {//第一层 注册
                    @Override
                    public void accept(Result result) throws Exception {
                        Log.i("windy", "注册返回信息:" + result.getMessage());
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Result, ObservableSource<Result<UserInfo>>>() {
                    @Override
                    public ObservableSource<Result<UserInfo>> apply(Result result) throws Exception {
                        return apiServer.login("15751515963", "123");
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Result<UserInfo>>() {//第二层  登录
                    @Override
                    public void accept(Result<UserInfo> userInfoResult) throws Exception {
                        Log.i("windy", "登录返回信息:" + userInfoResult.getMessage());
                        UserInfo result = userInfoResult.getResult();
                        String userId = result.getUserId();
                        String sessionId = result.getSessionId();
                        Log.i("windy", "登录请求后的userId:" + userId);
                        Log.i("windy", "登录请求后的sessionId:" + sessionId);
                        SpUtil.saveUserInfo(MyApp.context, userId, sessionId); //存储sp
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Result<UserInfo>, ObservableSource<Result<List<FirstEntity>>>>() {
                    @Override
                    public ObservableSource<Result<List<FirstEntity>>> apply(Result<UserInfo> userInfoResult) throws Exception {
                        return apiServer.firstList();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Result<List<FirstEntity>>>() {//第三层 一级列表
                    @Override
                    public void accept(Result<List<FirstEntity>> listResult) throws Exception {
                        Log.i("windy", "一级类目返回信息:" + listResult.getMessage());
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Result<List<FirstEntity>>, ObservableSource<Result<List<SecondEntity>>>>() {
                    @Override
                    public ObservableSource<Result<List<SecondEntity>>> apply(Result<List<FirstEntity>> listResult) throws Exception {
                        List<FirstEntity> result = listResult.getResult();
                        String id = result.get(0).getId(); //拿到第一个id 用来请求二级列表
                        Log.i("windy", "一级列表Id:" + id);
                        return apiServer.secondList(id);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Result<List<SecondEntity>>>() {
                    @Override
                    public void accept(Result<List<SecondEntity>> listResult) throws Exception {
                        Log.i("windy", "二级列表返回的信息" + listResult.getMessage());
                        List<SecondEntity> result = listResult.getResult();
                        String id = result.get(0).getId(); //二级列表id
                        Log.i("windy", "二级列表Id:" + id);
                        SpUtil.saveSecondId(MyApp.context, id);  //存储sp 用户查询二级商品列表
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Function<Result<List<SecondEntity>>, ObservableSource<Result>>() {
                    @Override
                    public ObservableSource<Result> apply(Result<List<SecondEntity>> listResult) throws Exception {
                        return getModel(apiServer, args);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Result>() {
                    @Override
                    public void accept(Result result) throws Exception {
                        if ("0000".equals(result.getStatus())) {
                            implView.success(result);
                        } else {
                            implView.fail(result);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    protected abstract Observable getModel(ApiServer apiServer, Object... args);

    public void unBind() {
        if (implView != null) {
            implView = null;
        }
    }
}
