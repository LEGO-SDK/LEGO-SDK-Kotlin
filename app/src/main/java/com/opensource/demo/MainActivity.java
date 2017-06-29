package com.opensource.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.webkit.WebView;

import com.opensource.legosdk.core.LGOCore;
import com.opensource.legosdk.core.LGORequestContext;
import com.opensource.legosdk.core.LGOWebView;
import com.opensource.legosdk.core.LGOWebViewActivity;
import com.opensource.legosdk.nativemodules.device.LGODevice;
import com.opensource.legosdk.nativemodules.httprequest.LGOHTTPRequestOperation;
import com.opensource.legosdk.uimodules.page.LGOPageRequest;
import com.opensource.legosdk.webview.pack.LGOPack;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

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
        super.onCreate(savedInstanceState);
    }

}
