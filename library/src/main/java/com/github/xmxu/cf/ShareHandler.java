package com.github.xmxu.cf;

import android.app.Activity;

/**
 * 分享处理器
 * Created by Simon on 2016/11/21.
 */

public interface ShareHandler<T> extends Handler {

    void share(Activity activity, ShareRequest request, Callback<T> callback, Object tag);

}
