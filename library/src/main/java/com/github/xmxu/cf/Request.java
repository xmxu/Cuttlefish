package com.github.xmxu.cf;

import android.content.Context;

/**
 * 请求
 * Created by Simon on 2016/11/21.
 */

public abstract class Request {

    protected Context mContext;

    Request(Context context) {
        mContext = context;
    }

}
