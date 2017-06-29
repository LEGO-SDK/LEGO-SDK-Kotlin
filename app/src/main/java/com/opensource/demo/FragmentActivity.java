package com.opensource.demo;

import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.opensource.legosdk.core.LGOCore;
import com.opensource.legosdk.core.LGOWebViewActivity;
import com.opensource.legosdk.core.LGOWebViewFragment;
import com.opensource.legosdk.webview.pack.LGOPack;

import java.io.IOException;

public class FragmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            LGOPack.Companion.setPublicKey(getAssets().open("weui.zip.pub"), "https://raw.githubusercontent.com/LEGO-SDK/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        LGOCore.Companion.getWhiteList().add("githubusercontent.com");
        LGOCore.Companion.getWhiteList().add("google.com.hk");
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_fragment);
        this.configureFragment();
    }

    void configureFragment() {
        LGOWebViewFragment fragment = (LGOWebViewFragment)getSupportFragmentManager().findFragmentById(R.id.myFragment);
        fragment.setUrlString("https://raw.githubusercontent.com/LEGO-SDK/LEGO-SDK-OC/master/Resources/weui.zip");
    }

}
