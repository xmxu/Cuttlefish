package com.github.xmxu.cf;

import android.app.Activity;
import android.content.Intent;

/**
 * 简单分享处理器
 * Created by Simon on 2016/11/22.
 */

public class SimpleShareHandler<T> implements ShareHandler<T> {

    @Override
    public void share(Activity activity, ShareRequest request, Callback<T> callback, Object tag) {

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }
}
