package com.opensource.demo;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.opensource.legosdk.core.LGOCore;
import com.opensource.legosdk.core.LGOWebView;
import com.opensource.legosdk.core.LGOWebViewActivity;
import com.opensource.legosdk.webview.pack.LGOPack;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(@android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LGOWebViewActivity.Companion.openURL(MainActivity.this, "http://webpage.yy.com/s/lego-sdk/orz.html");
            }
        });
    }

}
