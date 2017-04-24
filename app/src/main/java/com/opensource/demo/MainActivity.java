package com.opensource.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.opensource.legosdk.core.LGOWebView;
import com.opensource.legosdk.core.LGOWebViewActivity;
import com.opensource.legosdk.nativemodules.device.LGODevice;

import org.jetbrains.annotations.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends LGOWebViewActivity {

    @Nullable
    @Override
    public String getUrlString() {
        return "http://3g.qq.com";
    }

}
