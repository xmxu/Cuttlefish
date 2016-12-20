package com.github.xmxu.cf.wechat;

import com.github.xmxu.cf.Handler;

/**
 * 微信辅助器
 * Created by Simon on 2016/11/22.
 */

public class WechatHelper {

    private Handler mCurrentHandler;

    private static WechatHelper sInstance = null;
    public static WechatHelper getInstance() {
        if (sInstance == null) {
            synchronized (WechatHelper.class) {
                if (sInstance == null) {
                    sInstance = new WechatHelper();
                }
            }
        }
        return sInstance;
    }


    private WechatHelper() {
    }

    public Handler getCurrentHandler() {
        return mCurrentHandler;
    }

    public void setCurrentHandler(Handler currentHandler) {
        mCurrentHandler = currentHandler;
    }
}
