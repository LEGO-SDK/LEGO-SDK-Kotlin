package com.opensource.demo;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.opensource.legosdk.core.LGOCore;
import com.opensource.legosdk.core.LGOWebViewActivity;
import com.opensource.legosdk.webview.pack.LGOPack;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class MainActivity extends LGOWebViewActivity {

    @Nullable
    @Override
    public String getUrlString() {
        return "http://webpage.yy.com/s/lego-sdk/orz.html?20171010";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        LGOCore.Companion.getWhiteList().add("webpage.yy.com");
        super.onCreate(savedInstanceState);
    }

}
