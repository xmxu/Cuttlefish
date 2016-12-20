package com.github.xmxu.cf;

/**
 * 请求管理者
 * Created by Simon on 2016/11/22.
 */

public class Caller {

    private final Handler mHandler;

    public Caller(Handler handler) {
        mHandler = handler;
    }

    public Handler handler() {
        return mHandler;
    }
}
