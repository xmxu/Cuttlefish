package com.github.xmxu.cf.qq;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.util.Log;

import com.github.xmxu.cf.Callback;
import com.github.xmxu.cf.Config;
import com.github.xmxu.cf.Result;
import com.github.xmxu.cf.ShareHandler;
import com.github.xmxu.cf.ShareRequest;
import com.github.xmxu.cf.ShareResult;
import com.github.xmxu.cf.SimpleShareHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * QQ分享处理器
 * Created by Simon on 2016/11/21.
 */

public class QQShareHandler extends SimpleShareHandler<ShareResult> {

    private Tencent mTencent;
    private IUiListener mIUiListener;
    private Callback<ShareResult> mCallback;
    private Object mTag;
    private int mType = QQ;

    @IntDef({QQ, QZONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }
    public static final int QQ = 0;
    public static final int QZONE = 1;

    QQShareHandler() {

    }

    QQShareHandler(@Type int type) {
        mType = type;
    }

    public static QQShareHandler get(@Type int type) {
        return new QQShareHandler(type);
    }

    public static QQShareHandler get() {
        return get(QQ);
    }

    @Override
    public void share(Activity activity, ShareRequest request, Callback<ShareResult> callback, Object tag) {
        this.mCallback = callback;
        this.mTag = tag;
        mIUiListener = new ShareListener();
        if (mTencent == null) {
            mTencent = Tencent.createInstance(Config.QQ_APPID, activity.getApplicationContext());
        }

        if (mTencent == null) {
            if (mCallback != null) {
                mCallback.onFailure(new Result(Result.Code.NO_CLIENT, "Can not found client.", tag));
            }
            mCallback = null;
            return;
        }

        Bundle bundle = new Bundle();
        //分享的消息摘要，最长50个字
        bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, request.getContent());
        //手Q客户端顶部，替换“返回”按钮文字，如果为空，用返回代替
        bundle.putString(QQShare.SHARE_TO_QQ_APP_NAME, request.getAppName());

        if (mType == QQ) {
            bundle.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
            //            这条分享消息被好友点击后的跳转URL。
            bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, request.getLink());
            //分享的标题。注：PARAM_TITLE、PARAM_IMAGE_URL、PARAM_	SUMMARY不能全为空，最少必须有一个是有值的。
            bundle.putString(QQShare.SHARE_TO_QQ_TITLE, request.getTitle());

            //分享的图片URL
            bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL,
                    request.getImageUrl());

            mTencent.shareToQQ(activity, bundle, mIUiListener);
        } else if (mType == QZONE) {
            bundle.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
            bundle.putString(QzoneShare.SHARE_TO_QQ_TITLE, request.getTitle());//必填
            bundle.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, request.getContent());//选填
            bundle.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, request.getLink());//必填
            if (request.getImageUrl() != null) {
                ArrayList<String> picUris = new ArrayList<>();
                picUris.add(request.getImageUrl());
                bundle.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, picUris);
            }
            mTencent.shareToQzone((Activity) activity, bundle, mIUiListener);
        }

    }

    @Override
    public void onNewIntent(Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Tencent.onActivityResultData(requestCode, resultCode, intent, mIUiListener);
    }

    private class ShareListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            Log.d("QQLogic", "ShareComplete: " + o);
            if (mCallback != null) {
                mCallback.onComplete(new ShareResult(Result.Code.OK, "Succeed", mTag));
            }
            mCallback = null;
        }

        @Override
        public void onError(UiError uiError) {
            Log.d("QQLogic", "ShareError: " + uiError);
            if (mCallback != null) {
                mCallback.onFailure(new Result(uiError.errorCode, uiError.errorMessage, mTag));
            }
            mCallback = null;
        }

        @Override
        public void onCancel() {
            Log.d("QQLogic", "ShareCancel: ");
            if (mCallback != null) {
                mCallback.onFailure(new Result(Result.Code.CANCEL, "Cancel", mTag));
            }
            mCallback = null;
        }
    }
}
