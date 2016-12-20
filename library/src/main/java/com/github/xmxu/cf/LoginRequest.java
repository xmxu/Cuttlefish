package com.github.xmxu.cf;

import android.app.Activity;
import android.content.Context;

/**
 * 登录请求
 * Created by Simon on 2016/11/21.
 */

public class LoginRequest<T> extends Request {

    /**
     * 回调接口
     */
    protected Callback<T> mCallback;
    /**
     * 代表当前请求的tag
     */
    protected Object mTag;

    LoginRequest(Context context) {
        super(context);
    }

    public LoginRequest<T> callback(Callback<T> callback) {
        this.mCallback = callback;
        return this;
    }

    public LoginRequest<T> tag(Object tag) {
        mTag = tag;
        return this;
    }

    public Caller to(LoginHandler<T> creator) {
        creator.login((Activity) mContext, mCallback, mTag);
        return new Caller(creator);
    }

}
