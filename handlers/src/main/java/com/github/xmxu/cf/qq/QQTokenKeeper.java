package com.github.xmxu.cf.qq;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * QQToken缓存
 * Created by freeman on 16/3/14.
 */
public class QQTokenKeeper {

    private static final String FILE_NAME = /*ThirdSdkHelper.getInstance().getEnvironment() + */"lyy_qq";
    private static final String KEY_CONTENT = "content";


    public static void writeToken (Context context, String content) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(KEY_CONTENT, content);
        editor.apply();
    }

    public static String readToken (Context context) {
        SharedPreferences sp = context.getApplicationContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(KEY_CONTENT, null);
    }

}
