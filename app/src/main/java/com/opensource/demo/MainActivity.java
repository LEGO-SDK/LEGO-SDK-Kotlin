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
        return "https://raw.githubusercontent.com/LEGO-SDK/LEGO-SDK-OC/master/Resources/weui.zip";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            LGOPack.Companion.setPublicKey(getAssets().open("weui.zip.pub"), "https://raw.githubusercontent.com/LEGO-SDK/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        LGOCore.Companion.getWhiteList().add("githubusercontent.com");
        LGOCore.Companion.getWhiteList().add("google.com.hk");
        LGOCore.Companion.getWhiteList().add("duowan.cn");
        super.onCreate(savedInstanceState);
    }

}
