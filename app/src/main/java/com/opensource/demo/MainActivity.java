package com.opensource.demo;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        super.onCreate(savedInstanceState);
    }

}
