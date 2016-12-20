package com.github.xmxu.cf;

import android.app.Activity;

/**
 * 登录处理器
 * Created by Simon on 2016/11/21.
 */

public interface LoginHandler<T> extends Handler {

    void login(Activity activity, Callback<T> callback, Object tag);

}
