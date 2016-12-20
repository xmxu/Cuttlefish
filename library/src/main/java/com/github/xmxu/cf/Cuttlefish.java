package com.github.xmxu.cf;

import android.content.Context;

/**
 * Cuttlefish工厂
 * Created by Simon on 2016/11/21.
 */

public class Cuttlefish {

    // Creator | RequestBody | Handler
    //Cuttlefish.with(Context).login() | .callback() | .to(QQ.Creator);
    //Creator: appId appKey login

    //Creator | RequestBody | Hanlder
    //Cuttlefish.with(Context).shareLink() | .link().title().content().callback() | .to(Weibo.creator)

    public static Creator with(Context context) {
        return new Creator(context);
    }

}

