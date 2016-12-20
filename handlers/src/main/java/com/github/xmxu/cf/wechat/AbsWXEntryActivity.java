package com.github.xmxu.cf.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.xmxu.cf.Handler;

/**
 * 微信入口
 * Created by Simon on 2016/11/21.
 */

public class AbsWXEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent();
    }

    private void handleIntent() {
        Handler handler = WechatHelper.getInstance().getCurrentHandler();
        if (handler != null) {
            handler.onNewIntent(getIntent());
        }
        WechatHelper.getInstance().setCurrentHandler(null);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

}
