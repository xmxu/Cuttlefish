package com.github.xmxu.cf;

/**
 * 第三方配置信息
 * Created by Simon on 2016/11/21.
 */

public class Config {

    private String mQQAppId = "";
    private String mQQAppKey = "";

    private String mWechatAppId = "";
    private String mWechatAppKey = "";

    private String mWeiboAppId = "";
    private String mWeiboAppKey = "";

    private String mWeiboRedirectUrl = "https://api.weibo.com/oauth2/default.html";
    private String mWeiboScope = "email,direct_messages_read,direct_messages_write,"
            + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
            + "follow_app_official_microblog," + "invitation_write";
    private String mWechatScope = "snsapi_userinfo";
    private String mQQScope = "get_simple_userinfo";


    private static Config sInstance = null;
    private Config() {}

    public static Config get() {
        if (sInstance == null) {
            synchronized (Config.class) {
                if (sInstance == null) {
                    sInstance = new Config();
                }
            }
        }
        return sInstance;
    }


    public Config qq(String appId, String appKey) {
        mQQAppId = appId;
        mQQAppKey = appKey;
        return this;
    }

    public Config weibo(String appId, String appKey) {
        mWeiboAppId = appId;
        mWeiboAppKey = appKey;
        return this;
    }

    public Config wechat(String appId, String appKey) {
        mWechatAppId = appId;
        mWechatAppKey = appKey;
        return this;
    }

    public Config weiboRedirectUrl(String url) {
        mWeiboRedirectUrl = url;
        return this;
    }

    public Config weiboScope(String scope) {
        mWeiboScope = scope;
        return this;
    }

    public Config wechatScope(String scope) {
        mWechatScope = scope;
        return this;
    }

    public Config qqScope(String scope) {
        mQQScope = scope;
        return this;
    }

    public String getQQAppId() {
        return mQQAppId;
    }

    public String getQQAppKey() {
        return mQQAppKey;
    }

    public String getWechatAppId() {
        return mWechatAppId;
    }

    public String getWechatAppKey() {
        return mWechatAppKey;
    }

    public String getWeiboAppId() {
        return mWeiboAppId;
    }

    public String getWeiboAppKey() {
        return mWeiboAppKey;
    }

    public String getWeiboRedirectUrl() {
        return mWeiboRedirectUrl;
    }

    public String getWeiboScope() {
        return mWeiboScope;
    }

    public String getWechatScope() {
        return mWechatScope;
    }

    public String getQQScope() {
        return mQQScope;
    }
}
