package com.github.xmxu.cf;

/**
 * 回调接口
 * Created by Simon on 2016/11/21.
 */

public interface Callback<T> {
    void onFailure(Result result);
    void onComplete(T result);
}
