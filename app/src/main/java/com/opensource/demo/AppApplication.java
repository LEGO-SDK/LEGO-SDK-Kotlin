package com.opensource.demo;

import android.app.Application;
import android.graphics.drawable.ColorDrawable;

import com.opensource.legosdk.core.LGOCore;
import com.opensource.legosdk.core.LGOWebViewActivity;

/**
 * Created by cuiminghui on 2017/10/11.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        LGOCore.Companion.getWhiteList().add("webpage.yy.com");
    }

}
