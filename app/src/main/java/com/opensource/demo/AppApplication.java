package com.opensource.demo;

import android.app.Application;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;

import com.opensource.legosdk.core.LGOCore;
import com.opensource.legosdk.core.LGOWebViewActivity;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * Created by cuiminghui on 2017/10/11.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        LGOCore.Companion.getWhiteList().add("webpage.yy.com");
        LGOCore.Companion.getWhiteList().add("legox.yy.com");
    }

}
