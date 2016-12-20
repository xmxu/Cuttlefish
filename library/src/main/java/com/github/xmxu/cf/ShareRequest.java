package com.github.xmxu.cf;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * 分享请求体
 * Created by Simon on 2016/11/21.
 */

public class ShareRequest<T> extends Request {

    private String title;
    private String content;
    private String description;
    private String link;
    private String imageUrl;
    private Bitmap imageBitmap;
    private int imageResId;

    private String appName;

    protected Callback<T> mCallback;
    protected Object mTag;

    ShareRequest(Context context) {
        super(context);
    }

    public ShareRequest<T> title(String val) {
        title = val;
        return this;
    }

    public ShareRequest<T> content(String val) {
        content = val;
        return this;
    }

    public ShareRequest<T> description(String val) {
        description = val;
        return this;
    }

    public ShareRequest<T> link(String val) {
        link = val;
        return this;
    }

    public ShareRequest<T> image(String val) {
        imageUrl = val;
        return this;
    }

    public ShareRequest<T> image(Bitmap val) {
        imageBitmap = val;
        return this;
    }

    public ShareRequest<T> image(int val) {
        imageResId = val;
        return this;
    }

    public ShareRequest<T> appName(String val) {
        appName = val;
        return this;
    }

    public ShareRequest<T> callback(Callback<T> callback) {
        this.mCallback = callback;
        return this;
    }

    public ShareRequest<T> tag(Object tag) {
        this.mTag = tag;
        return this;
    }

    public Caller to(ShareHandler<T> handler) {
        handler.share((Activity) mContext, this, mCallback, mTag);
        return new Caller(handler);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getImageResId() {
        return imageResId;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public String getAppName() {
        return appName;
    }
}
