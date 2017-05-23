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
        return "http://legox.yy.com/assets/oa/oa_A001.zip?_index.html#/";
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        try {
//            LGOPack.Companion.setPublicKey(getAssets().open("weui.zip.pub"), "https://github.com/LEGO-SDK/");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        String token = "Object>>>{\"message\":\"操作成功\",\"flowBatchNum\":20,\"token\":\"12694E049777450C91CEBEEB163B47E7\",\"ismain\":false,\"gps\":[{\"latitudeGCJ\":\"23.008323\",\"mansion\":\"番禺万达B1\",\"longitudeGCJ\":\"113.347617\",\"punchcardRangeGCJ\":150,\"address\":\"广州市番禺万达广场B1座\",\"longitude\":\"113.347617\",\"latitude\":\"23.008323\",\"punchcardRange\":150},{\"latitudeGCJ\":\"23.119734\",\"mansion\":\"羊城创意园308\",\"longitudeGCJ\":\"113.383139\",\"punchcardRangeGCJ\":150,\"address\":\"羊城创意园3-08\",\"longitude\":\"113.383379\",\"latitude\":\"23.119661\",\"punchcardRange\":150},{\"latitudeGCJ\":\"23.147241\",\"mansion\":\"1931生活中心\",\"longitudeGCJ\":\"113.406372\",\"punchcardRangeGCJ\":150,\"address\":\"大观中路3号\",\"longitude\":\"113.40257254\",\"latitude\":\"23.14870302\",\"punchcardRange\":150},{\"latitudeGCJ\":\"22.3490050336\",\"mansion\":\"珠海唐家大楼\",\"longitudeGCJ\":\"113.6042653348\",\"punchcardRangeGCJ\":150,\"address\":\"珠海唐家大楼\",\"longitude\":\"113.6042653348\",\"latitude\":\"22.3490050336\",\"punchcardRange\":150},{\"latitudeGCJ\":\"39.9770124819\",\"mansion\":\"北京致真大厦\",\"longitudeGCJ\":\"116.3511612728\",\"punchcardRangeGCJ\":150,\"address\":\"北京致真大厦\",\"longitude\":\"116.3511612728\",\"latitude\":\"39.9770124819\",\"punchcardRange\":150}],\"hardwareKey\":\"e81a84e7c199c4ef6eb7808982316fee\",\"code\":\"200\",\"employeeDept\":\"UED中心\",\"employeeCode\":\"G5405\",\"employeeName\":\"崔明辉\"}";
        SharedPreferences sharedPreferences = getSharedPreferences("default", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("com.yy.oaapp.account.origindata", token).apply();
        LGOWebViewActivity.Companion.setNavigationBarDrawable(new ColorDrawable(0xff277de2));
        LGOCore.Companion.getWhiteList().add("legox.yy.com");
        LGOPageRequest pageRequest = new LGOPageRequest(new LGORequestContext(null));
        pageRequest.setNavigationBarHidden(true);
        this.setPageSetting(pageRequest);
        super.onCreate(savedInstanceState);
    }

}
