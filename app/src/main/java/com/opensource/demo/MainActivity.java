package com.opensource.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.opensource.legosdk.core.LGOWebView;
import com.opensource.legosdk.nativemodules.device.LGODevice;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://www.baidu.com/");
    }


}
