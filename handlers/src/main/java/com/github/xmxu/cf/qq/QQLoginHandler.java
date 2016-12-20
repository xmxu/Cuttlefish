package com.github.xmxu.cf.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.github.xmxu.cf.Callback;
import com.github.xmxu.cf.Config;
import com.github.xmxu.cf.LoginResult;
import com.github.xmxu.cf.Result;
import com.github.xmxu.cf.SimpleLoginHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * QQ登录处理器
 * Created by Simon on 2016/11/21.
 */

public class QQLoginHandler extends SimpleLoginHandler<LoginResult> {

    private Tencent mTencent;

    private IUiListener mUiListener;

    private Callback<LoginResult> mCallback;
    private Object mTag;
    private Context mAppContext;

    QQLoginHandler() {

    }

    public static QQLoginHandler get() {
        return new QQLoginHandler();
    }


    @Override
    public void login(Activity activity, Callback<LoginResult> callback, Object tag) {

        this.mCallback = callback;
        this.mTag = tag;
        mAppContext = activity.getApplicationContext();

        mUiListener = new BaseListener();

        if (mTencent == null) {
            mTencent = Tencent.createInstance(Config.get().getQQAppId(), activity.getApplicationContext());
        }

        if (mTencent != null) {
            initTencentParams();
            if (!mTencent.isSessionValid()) {
                mTencent.login(activity, Config.get().getQQScope(), mUiListener);
            } else {
                LoginResult result = new LoginResult();
                result.setTag(mTag);
                result.setCode(Result.Code.OK);
                result.setUid(mTencent.getOpenId());
                result.setOpenId(mTencent.getOpenId());
                result.setAccessToken(mTencent.getAccessToken());
                if (mCallback != null) {
                    mCallback.onComplete(result);
                }
                mCallback = null;
            }
        }

    }

    private void initTencentParams () {
        String content = QQTokenKeeper.readToken(mAppContext);
        if (!TextUtils.isEmpty(content)) {
            try {
                JSONObject json = new JSONObject(content);
                String accessToken = json.getString("access_token");
                String openId = json.getString("openid");
                String expire = json.getString("expires_in");
                mTencent.setOpenId(openId);
                mTencent.setAccessToken(accessToken, expire);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Tencent.onActivityResultData(requestCode, resultCode, intent, mUiListener);
    }

    private class BaseListener implements IUiListener {

        @Override
        public void onComplete(Object o) {

            //登录成功
            JSONObject json = (JSONObject) o;
            QQTokenKeeper.writeToken(mAppContext, json.toString());
            if (mCallback != null) {
                LoginResult result = new LoginResult(Result.Code.OK, "Succeed");
                result.setAccessToken(json.optString("access_token"));
                result.setOpenId(json.optString("openid"));
                result.setUid(json.optString("openid"));
                result.setTag(mTag);
                result.setOriginal(json);
                mCallback.onComplete(new LoginResult());
            }
            mCallback = null;
        }

        @Override
        public void onError(UiError uiError) {
            if (mCallback != null) {
                mCallback.onFailure(new Result(uiError.errorCode, uiError.errorMessage, mTag));
            }
            mCallback = null;
        }

        @Override
        public void onCancel() {
            if (mCallback != null) {
                mCallback.onFailure(new Result(Result.Code.CANCEL, "Cancel", mTag));
            }
            mCallback = null;
        }
    }
}
