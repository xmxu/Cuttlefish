package com.github.xmxu.cf.sina;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.github.xmxu.cf.Callback;
import com.github.xmxu.cf.Config;
import com.github.xmxu.cf.LoginResult;
import com.github.xmxu.cf.Result;
import com.github.xmxu.cf.SimpleLoginHandler;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 微博登录处理器
 * Created by Simon on 2016/11/21.
 */

public class WeiboLoginHandler extends SimpleLoginHandler<LoginResult> {

    private static final String TAG = "WeiboLoginHandler";

    private AuthInfo mAuthInfo;
    private Oauth2AccessToken mAccessToken;
    private SsoHandler mSsoHandler;
    private Callback<LoginResult> mCallback;
    private Object mTag;

    private Context mAppContext;

    WeiboLoginHandler() {

    }

    public static WeiboLoginHandler get() {
        return new WeiboLoginHandler();
    }

    @Override
    public void login(Activity activity, Callback<LoginResult> callback, Object tag) {
        this.mCallback = callback;
        this.mTag = tag;
        mAppContext = activity.getApplicationContext();
        if (mAuthInfo == null) {
            mAuthInfo = new AuthInfo(activity.getApplicationContext(), Config.WEIBO_APPID, Config.REDIRECT_URL, Config.SCOPE);
        }
        mSsoHandler = new SsoHandler(activity, mAuthInfo);

        final Oauth2AccessToken accessToken = WeiBoAccessTokenKeeper.readAccessToken(mAppContext);
        if (accessToken != null) {
            if(WeiBoAccessTokenKeeper.isExpired(mAppContext)) {
                mSsoHandler.authorize(new AuthListener());
            } else {
                String token = accessToken.getToken();
                if (TextUtils.isEmpty(token)) {
                    mSsoHandler.authorize(new AuthListener());
                } else {
                    if (!TextUtils.isEmpty(accessToken.getRefreshToken())) {
                        refreshExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                refreshToken(accessToken.getRefreshToken());
                            }
                        });
                    }
                    LoginResult result = new LoginResult();
                    result.setCode(Result.Code.OK);
                    result.setTag(mTag);
                    result.setUid(accessToken.getUid());
                    result.setAccessToken(token);
                    result.setRefreshToken(accessToken.getRefreshToken());
                    result.setOriginal(accessToken);
                    if (mCallback != null) {
                        mCallback.onComplete(result);
                    }
                }
            }
        } else {
            mSsoHandler.authorize(new AuthListener());
        }
    }

    private ExecutorService refreshExecutor = Executors.newSingleThreadExecutor();

    private void refreshToken (String refreshToken) {
        //https://api.weibo.com/oauth2/access_token?client_id=YOUR_CLIENT_ID
        // &client_secret=YOUR_CLIENT_SECRET
        // &grant_type=refresh_token
        // &redirect_uri=YOUR_REGISTERED_REDIRECT_URI
        // &refresh_token=

        String urlStr = String.format("https://api.weibo.com/oauth2/access_token?" +
                        "client_id=%s&client_secret=%s" +
                        "&grant_type=refresh_token&redirect_uri=%s&refresh_token=%s", Config.WEIBO_APPID,
                Config.WEIBO_APPKEY, Config.REDIRECT_URL, refreshToken);
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.connect();

            int status = urlConnection.getResponseCode();
            if (status / 100 == 2) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("utf-8")));
                String line = "";
                StringBuilder contentSb = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    contentSb.append(line);
                }

                JSONObject object = new JSONObject(contentSb.toString());
                String access_token = object.optString("access_token", null);
                WeiBoAccessTokenKeeper.writeAccessToken(mAppContext, access_token);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, intent);
        }
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            //从bundle 解析
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            Log.d(TAG, "onComplete() called with: values = [" + mAccessToken + "]");

            LoginResult result = new LoginResult();
            result.setTag(mTag);
            if (mAccessToken.isSessionValid()) {
                // 保存 Token
                result.setCode(Result.Code.OK);
                String token = mAccessToken.getToken();
                result.setUid(mAccessToken.getUid());
                result.setAccessToken(token);
                result.setRefreshToken(mAccessToken.getRefreshToken());
                WeiBoAccessTokenKeeper.writeAccessToken(mAppContext, mAccessToken);

            } else {
                //出现错误时，将获得code
                String code = values.getString("code");
                if (TextUtils.isDigitsOnly(code)) {
                    result.setCode(Integer.valueOf(code));
                }
                result.setErrorMsg(code);
            }
            if (mCallback != null) {
                mCallback.onComplete(result);
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

        @Override
        public void onWeiboException(WeiboException e) {
            Log.d("Sina", "Exception: " + e.getMessage());
            if (mCallback != null) {
                mCallback.onFailure(new Result(Integer.MAX_VALUE, e.getMessage(), mTag));
            }
            mCallback = null;
        }
    }
}
