package com.opensource.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.opensource.legosdk.core.LGOModule;
import com.opensource.legosdk.core.LGORequest;
import com.opensource.legosdk.core.LGORequestContext;
import com.opensource.legosdk.core.LGORequestable;
import com.opensource.legosdk.core.LGOResponse;
import com.opensource.legosdk.core.LGOWebViewActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;

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
                LGOWebViewActivity.Companion.openURL(MainActivity.this, "http://webpage.yy.com/s/lego-sdk/index.html");
            }
        });
    }

}

