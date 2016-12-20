package com.github.xmxu.cf.wechat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.Log;

import com.github.xmxu.cf.Callback;
import com.github.xmxu.cf.Config;
import com.github.xmxu.cf.Result;
import com.github.xmxu.cf.ShareRequest;
import com.github.xmxu.cf.ShareResult;
import com.github.xmxu.cf.SimpleShareHandler;
import com.github.xmxu.cf.sina.ImageLoaderAsyncTask;
import com.github.xmxu.cf.sina.OnDataFinishedListener;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 微信分享处理器
 * Created by Simon on 2016/11/22.
 */

public class WechatShareHandler extends SimpleShareHandler<ShareResult> implements IWXAPIEventHandler {

    private static final String TAG = "WechatShareHandler";

    private Callback<ShareResult> mCallback;
    private Object mTag;
    private IWXAPI api;
    private int mType = SESSION;

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        Log.d(TAG, "onResp() called with: baseResp = [" + baseResp + "]");
        if (baseResp instanceof SendMessageToWX.Resp) {
            //登录
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    if (mCallback != null) {
                        mCallback.onComplete(new ShareResult(Result.Code.OK, "Succeed", mTag));
                    }
                    mCallback = null;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    if (mCallback != null) {
                        mCallback.onFailure(new Result(Result.Code.CANCEL, "Cancel", mTag));
                    }
                    mCallback = null;
                    break;
                default:
                    if (mCallback != null) {
                        mCallback.onFailure(new Result(baseResp.errCode, baseResp.errStr, mTag));
                    }
                    mCallback = null;
                    break;
            }
        }
    }

    @IntDef({SESSION, TIMELINE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{}
    public static final int SESSION = 0;
    public static final int TIMELINE = 1;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    WechatShareHandler() {
        mType = SESSION;
    }

    WechatShareHandler(@Type int type) {
        mType = type;
    }

    public static WechatShareHandler get() {
        return new WechatShareHandler();
    }

    public static WechatShareHandler get(@Type int type) {
        return new WechatShareHandler(type);
    }

    @Override
    public void share(Activity activity, final ShareRequest request, Callback<ShareResult> callback, Object tag) {

        WechatHelper.getInstance().setCurrentHandler(this);

        mCallback = callback;
        mTag = tag;

        api = WXAPIFactory.createWXAPI(activity, Config.WECHAT_APPID, true);
        if (!api.isWXAppInstalled()) {
            if (mCallback != null) {
                mCallback.onFailure(new Result(Result.Code.NO_CLIENT, "No client", tag));
            }
            mCallback = null;
            return;
        }

        api.registerApp(Config.WECHAT_APPID);

        WXWebpageObject webpageObject = new WXWebpageObject();
        webpageObject.webpageUrl = request.getLink();

        final WXMediaMessage mediaMessage = new WXMediaMessage(webpageObject);
        mediaMessage.title = request.getTitle();
        mediaMessage.description = request.getDescription();
        mediaMessage.messageExt = request.getContent();
        if (!TextUtils.isEmpty(request.getImageUrl())) {
            new ImageLoaderAsyncTask(new OnDataFinishedListener() {
                @Override
                public void onDataSuccessfully(final Object data) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            SendMessageToWX.Req req = new SendMessageToWX.Req();
                            mediaMessage.setThumbImage((Bitmap) data);
                            if (mType == SESSION) {
                                req.scene = SendMessageToWX.Req.WXSceneSession;
                            } else if (mType == TIMELINE) {
                                req.scene = SendMessageToWX.Req.WXSceneTimeline;
                            } else {
                                return;
                            }
                            req.transaction = String.valueOf(System.currentTimeMillis());
                            req.message = mediaMessage;
                            if (api != null) {
                                api.sendReq(req);
                            }
                        }
                    });
                }

                @Override
                public void onDataFailed() {

                }
            }).execute(request.getImageUrl());
        } else if (request.getImageResId() != 0) {

            mediaMessage.setThumbImage(BitmapFactory.decodeResource(activity.getResources(), request.getImageResId()));
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            if (mType == SESSION) {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            } else if (mType == TIMELINE) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
//                throw new IllegalArgumentException("You must indicate dest");
            }
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = mediaMessage;
            if (api != null) {
                api.sendReq(req);
            }

        } else {
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            if (mType == SESSION) {
                req.scene = SendMessageToWX.Req.WXSceneSession;
            } else if (mType == TIMELINE) {
                req.scene = SendMessageToWX.Req.WXSceneTimeline;
            } else {
//                throw new IllegalArgumentException("You must indicate dest");
            }
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = mediaMessage;

            if (api != null) {
                api.sendReq(req);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (api != null) {
            api.handleIntent(intent, this);
        }
    }
}
