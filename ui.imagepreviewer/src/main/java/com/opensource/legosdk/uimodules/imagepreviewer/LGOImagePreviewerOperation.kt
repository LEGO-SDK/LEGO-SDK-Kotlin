package com.opensource.legosdk.uimodules.imagepreviewer

import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import android.os.Parcelable
import android.view.View.GONE
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.Toast
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.nostra13.universalimageloader.core.ImageLoader
import android.widget.ProgressBar
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.DisplayImageOptions
import android.support.v4.view.PagerAdapter
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.*
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.widget.FrameLayout
import android.widget.ImageView
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.QueueProcessingType


/**
 * Created by cuiminghui on 2017/4/22.
 */
class LGOImagePreviewerOperation(val request: LGOImagePreviewerRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestActivity()?.let {
            val intent = Intent(it, LGOImagePreviewerFragmentActivity::class.java)
            intent.putExtra("URLs", request.URLs.toTypedArray())
            request.currentURL?.let {
                intent.putExtra("currentURL", it)
            }
            it.startActivity(intent)
            return LGOResponse().accept(null)
        }
        return LGOResponse().reject("UI.ImagePreviewer", -1, "Unknown error.")
    }

}

class LGOImagePreviewerFragmentActivity: FragmentActivity() {

    var URLs: Array<String>? = null
    var currentURL: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, SYSTEM_UI_FLAG_FULLSCREEN)
        URLs = intent.getStringArrayExtra("URLs")
        currentURL = intent.getStringExtra("currentURL")
        initImageLoader()
        setContentView(requestRootView())
    }

    fun requestRootView(): View {
        val rootView = ViewPager(this)
        rootView.layoutParams = ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        rootView.setBackgroundColor(Color.BLACK)
        val URLs = URLs ?: return rootView
        rootView.adapter = object : PagerAdapter() {

            override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
                return view?.equals(`object`) ?: false
            }

            override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
                (`object` as? View)?.let {
                    container?.removeView(it)
                }
            }

            override fun getCount(): Int {
                return URLs.size
            }

            override fun instantiateItem(container: ViewGroup?, position: Int): Any {
                val frameLayout = FrameLayout(this@LGOImagePreviewerFragmentActivity)
                val imageView = ImageView(this@LGOImagePreviewerFragmentActivity)
                imageView.adjustViewBounds = true
                frameLayout.addView(imageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER))
                val options = DisplayImageOptions.Builder()
                        .resetViewBeforeLoading(true)
                        .cacheOnDisk(true)
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .considerExifParams(true)
                        .displayer(FadeInBitmapDisplayer(300))
                        .build()
                ImageLoader.getInstance().displayImage(URLs[position], imageView, options, object: SimpleImageLoadingListener() {
                    override fun onLoadingFailed(imageUri: String?, view: View?, failReason: FailReason?) {
                        val message = "Load image failed."
                        Toast.makeText(view?.context, message, Toast.LENGTH_SHORT).show()
                    }
                })
                container?.addView(frameLayout, ViewGroup.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
                return frameLayout
            }

        }
        currentURL?.let {
            val idx = URLs.indexOf(currentURL)
            if (idx >= 0) {
                rootView.currentItem = idx
            }
        }
        return rootView
    }

    fun initImageLoader() {
        if (ImageLoader.getInstance().isInited) {
            return
        }
        val config = ImageLoaderConfiguration.Builder(this)
        config.threadPriority(Thread.NORM_PRIORITY - 2)
        config.denyCacheImageMultipleSizesInMemory()
        config.diskCacheFileNameGenerator(Md5FileNameGenerator())
        config.diskCacheSize(50 * 1024 * 1024)
        config.tasksProcessingOrder(QueueProcessingType.LIFO)
        ImageLoader.getInstance().init(config.build())
    }

}