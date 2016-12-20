package com.github.xmxu.cf;

import android.app.Activity;
import android.content.Intent;

/**
 * 简单的登录处理器
 * Created by Simon on 2016/11/21.
 */

public class SimpleLoginHandler<T> implements LoginHandler<T> {

    @Override
    public void login(Activity activity, Callback<T> callback, Object tag) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }
}
