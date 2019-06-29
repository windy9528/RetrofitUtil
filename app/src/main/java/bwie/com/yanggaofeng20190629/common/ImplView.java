package bwie.com.yanggaofeng20190629.common;

import bwie.com.yanggaofeng20190629.entity.Result;

/**
 * date:2019/6/29
 * name:windy
 * function:
 */
public interface ImplView<T> {

    void success(Result data);

    void fail(Result result);
}
