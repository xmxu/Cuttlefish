package com.github.xmxu.cf;

import android.content.Context;

/**
 * 创建器
 * Created by Simon on 2016/11/21.
 */

public class Creator {

    private Context mContext;

    Creator(Context context) {
        mContext = context;
    }

    public LoginRequest<LoginResult> login() {
        return new LoginRequest<>(mContext);
    }

    public ShareRequest<ShareResult> share() {
        return new ShareRequest<>(mContext);
    }

}
