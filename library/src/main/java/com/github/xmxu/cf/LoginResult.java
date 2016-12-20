package com.github.xmxu.cf;

/**
 * 登录回调实体
 * Created by Simon on 2016/11/21.
 */

public class LoginResult extends Result {

    private String uid;
    private String accessToken;
    private String refreshToken;
    private String openId;
    private Object original;

    public LoginResult(int errorCode, String errorMsg) {
        super(errorCode, errorMsg);
    }

    public LoginResult() {
    }

    public LoginResult(int errorCode, String errorMsg, Object tag) {
        super(errorCode, errorMsg, tag);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Object getOriginal() {
        return original;
    }

    public void setOriginal(Object original) {
        this.original = original;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginResult{");
        sb.append("uid='").append(uid).append('\'');
        sb.append(", accessToken='").append(accessToken).append('\'');
        sb.append(", refreshToken='").append(refreshToken).append('\'');
        sb.append(", openId='").append(openId).append('\'');
        sb.append(", original=").append(original);
        sb.append('}');
        return sb.toString();
    }
}
