package com.github.xmxu.cf;

/**
 * 分享回调体
 * Created by Simon on 2016/11/21.
 */

public class ShareResult extends Result {


    public ShareResult(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public ShareResult(int errorCode, String errorMsg, Object tag) {
        super(errorCode, errorMsg, tag);
    }
}
