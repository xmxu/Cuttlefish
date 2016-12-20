package com.github.xmxu.cf.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.github.xmxu.cf.Callback;
import com.github.xmxu.cf.Caller;
import com.github.xmxu.cf.Cuttlefish;
import com.github.xmxu.cf.LoginResult;
import com.github.xmxu.cf.Result;
import com.github.xmxu.cf.ShareResult;
import com.github.xmxu.cf.qq.QQLoginHandler;
import com.github.xmxu.cf.qq.QQShareHandler;
import com.github.xmxu.cf.sina.WeiboLoginHandler;
import com.github.xmxu.cf.sina.WeiboShareHandler;
import com.github.xmxu.cf.wechat.WechatLoginHandler;
import com.github.xmxu.cf.wechat.WechatShareHandler;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Caller mCurrentCaller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            if (mCurrentCaller != null) {
                mCurrentCaller.handler().onNewIntent(getIntent());
            }
        }

    }

    public void onQQLogin(View view) {

        mCurrentCaller = Cuttlefish.with(this).login().callback(mLoginResultCallback).to(QQLoginHandler.get());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCurrentCaller.handler().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.d(TAG, "onNewIntent() called with: intent = [" + intent + "]");
        super.onNewIntent(intent);
        mCurrentCaller.handler().onNewIntent(intent);
    }

    public void onQQShareQZone(View view) {
        mCurrentCaller = Cuttlefish.with(this).share()
                .appName("Cuttlefish")
                .title("这是标题")
                .description("这是描述")
                .content("这是内容")
                .image("http://pic6.58cdn.com.cn/p1/big/n_v1bj3gzshdzauvq2y5rqxa.jpg")
                .link("http://www.baidu.com").callback(mShareResultCallback).to(QQShareHandler.get(QQShareHandler.QZONE));
    }

    public void onQQShareFriend(View view) {
        mCurrentCaller = Cuttlefish.with(this).share()
                .appName("Cuttlefish")
                .title("这是标题")
                .description("这是描述")
                .content("这是内容")
                .image("http://pic6.58cdn.com.cn/p1/big/n_v1bj3gzshdzauvq2y5rqxa.jpg")
                .link("http://www.baidu.com").callback(mShareResultCallback).to(QQShareHandler.get(QQShareHandler.QQ));
    }

    public void onWeiboLogin(View view) {
        mCurrentCaller = Cuttlefish.with(this).login().callback(mLoginResultCallback).to(WeiboLoginHandler.get());
    }

    public void onWeiboShare(View view) {
        mCurrentCaller = Cuttlefish.with(this).share()
                .appName("Cuttlefish")
                .title("这是标题")
                .description("这是描述")
                .content("这是内容")
                .image("http://pic6.58cdn.com.cn/p1/big/n_v1bj3gzshdzauvq2y5rqxa.jpg")
                .link("http://www.baidu.com").callback(mShareResultCallback)
                .to(WeiboShareHandler.get());
    }

    public void onWechatSessionShare(View view) {
        mCurrentCaller = Cuttlefish.with(this).share()
                .appName("Cuttlefish")
                .title("这是标题")
                .description("这是描述")
                .content("这是内容")
                .image("http://pic6.58cdn.com.cn/p1/big/n_v1bj3gzshdzauvq2y5rqxa.jpg")
                .link("http://www.baidu.com").callback(mShareResultCallback)
                .to(WechatShareHandler.get());
    }

    public void onWechatTimelineShare(View view) {
        mCurrentCaller = Cuttlefish.with(this).share()
                .appName("Cuttlefish")
                .title("这是标题")
                .description("这是描述")
                .content("这是内容")
                .image("http://pic6.58cdn.com.cn/p1/big/n_v1bj3gzshdzauvq2y5rqxa.jpg")
                .link("http://www.baidu.com").callback(mShareResultCallback)
                .to(WechatShareHandler.get(WechatShareHandler.TIMELINE));
    }

    public void onWechatLogin(View view) {
        mCurrentCaller = Cuttlefish.with(this).login().callback(mLoginResultCallback).to(WechatLoginHandler.get());
    }

    private Callback<LoginResult> mLoginResultCallback = new Callback<LoginResult>() {
        @Override
        public void onFailure(Result result) {
            if (result.getErrorCode() != Result.Code.CANCEL) {
                Toast.makeText(MainActivity.this, String.format("登录失败: %s, %s", result.getErrorCode(), result.getErrorMsg()), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "用户取消登录", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onComplete(LoginResult result) {
            Toast.makeText(MainActivity.this, String.format("登录成功：uid=%s", result.getUid()), Toast.LENGTH_SHORT).show();
        }
    };

    private Callback<ShareResult> mShareResultCallback = new Callback<ShareResult>() {
        @Override
        public void onFailure(Result result) {
            if (result.getErrorCode() != Result.Code.CANCEL) {
                Toast.makeText(MainActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onComplete(ShareResult result) {
            Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }
    };


}
