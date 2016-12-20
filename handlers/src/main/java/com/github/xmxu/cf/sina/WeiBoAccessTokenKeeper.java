package com.github.xmxu.cf.sina;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/**
 * Created by wy on 14-3-20.
 */
public class WeiBoAccessTokenKeeper {
	private static final String PREFERENCES_NAME = "lyy_weibo";

	private static final String KEY_UID = "uid";
	private static final String KEY_ACCESS_TOKEN = "access_token";
	private static final String KEY_EXPIRES_IN = "expires_in";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_EXPIRE = "expire";

    public static final int EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000;//7天

	/**
	 * 保存 Token 对象到 SharedPreferences。
	 * 
	 * @param token
	 *            Token 对象
	 */
	public static void writeAccessToken(Context context, Oauth2AccessToken token) {
		if (null == token) {
			return;
		}

		if (context == null) {
			return;
		}

		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(KEY_UID, token.getUid());
		editor.putString(KEY_ACCESS_TOKEN, token.getToken());
		editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
        editor.putString(KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putLong(KEY_EXPIRE, System.currentTimeMillis() + EXPIRE_TIME);
		editor.apply();
	}

    public static void writeAccessToken(Context context, String accessToken) {
        if (null == accessToken) {
            Log.d("TEST","token == null");
            return;
        }
		if (context == null) {
			return;
		}

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(KEY_ACCESS_TOKEN, accessToken);
        editor.putLong(KEY_EXPIRE, System.currentTimeMillis() + EXPIRE_TIME);
        editor.apply();
    }

	/**
	 * 从 SharedPreferences 读取 Token 信息。
	 * 
	 * @return 返回 Token 对象
	 */
	public static Oauth2AccessToken readAccessToken(Context context) {
		if (context == null) {
			return null;
		}

		Oauth2AccessToken token = new Oauth2AccessToken();
		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_APPEND);
		token.setUid(pref.getString(KEY_UID, ""));
		token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
		token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        token.setRefreshToken(pref.getString(KEY_REFRESH_TOKEN, ""));

		if(token.getToken().isEmpty()){
			return null;
		}
		return token;
	}

    public static boolean isExpired (Context context) {
		if (context == null) {
			return true;
		}
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
                PREFERENCES_NAME, Context.MODE_APPEND);
        return pref.getLong(KEY_EXPIRE, 0) <= System.currentTimeMillis();
    }

	/**
	 * 清空 SharedPreferences 中 Token信息。
	 */
	public static void clear(Context context) {
		if (context == null) {
			return;
		}
		SharedPreferences pref = context.getApplicationContext().getSharedPreferences(
				PREFERENCES_NAME, Context.MODE_APPEND);
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.apply();
	}
}
