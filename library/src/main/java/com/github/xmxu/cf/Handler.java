package com.github.xmxu.cf;

import android.content.Intent;

/**
 * 请求处理器接口
 * Created by Simon on 2016/11/21.
 */

public interface Handler {

    void onNewIntent(Intent intent);

    void onActivityResult(int requestCode, int resultCode, Intent intent);

}
