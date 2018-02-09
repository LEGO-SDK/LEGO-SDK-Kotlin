package com.opensource.legosdk.uimodules.imagepreviewer

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.assist.QueueProcessingType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse

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
    private var permissionRequestCode = -1
    private var permissionOpt: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, SYSTEM_UI_FLAG_FULLSCREEN)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.BLACK
        }
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
                val imageView = PinchImageView(this@LGOImagePreviewerFragmentActivity)
                imageView.adjustViewBounds = true
                imageView.setOnLongClickListener {
                    val dialogBuilder = AlertDialog.Builder(this@LGOImagePreviewerFragmentActivity)
                    dialogBuilder.setItems(listOf("保存图片", "取消").toTypedArray(), { _, idx ->
                        if (idx == 0) {
                            (imageView.drawable as? BitmapDrawable)?.let {
                                if (ContextCompat.checkSelfPermission(this@LGOImagePreviewerFragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    this@LGOImagePreviewerFragmentActivity.permissionRequestCode = (Math.random() * 1000).toInt()
                                    this@LGOImagePreviewerFragmentActivity.permissionOpt = {
                                        MediaStore.Images.Media.insertImage(contentResolver, it.bitmap, "", null)?.let {
                                            Toast.makeText(this@LGOImagePreviewerFragmentActivity, "已保存到系统相册", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    if (ActivityCompat.shouldShowRequestPermissionRationale(this@LGOImagePreviewerFragmentActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                        AlertDialog.Builder(this@LGOImagePreviewerFragmentActivity)
                                                .setTitle("请允许应用使用存储权限")
                                                .setCancelable(true)
                                                .setNegativeButton("禁止", { _, _ -> })
                                                .setPositiveButton("好的", { _, _ ->
                                                    ActivityCompat.requestPermissions(this@LGOImagePreviewerFragmentActivity, listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(), this@LGOImagePreviewerFragmentActivity.permissionRequestCode)
                                                })
                                                .create()
                                                .show()
                                    }
                                    else {
                                        ActivityCompat.requestPermissions(this@LGOImagePreviewerFragmentActivity, listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(), this@LGOImagePreviewerFragmentActivity.permissionRequestCode)
                                    }
                                }
                                else {
                                    MediaStore.Images.Media.insertImage(contentResolver, it.bitmap, "", null)?.let {
                                        Toast.makeText(this@LGOImagePreviewerFragmentActivity, "已保存到系统相册", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    })
                    dialogBuilder.setCancelable(true)
                    val dialog = dialogBuilder.create()
                    dialog.window.setGravity(Gravity.BOTTOM)
                    dialog.show()
                    println(true)
                    return@setOnLongClickListener true
                }
                frameLayout.addView(imageView, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER))
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.permissionRequestCode && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            this.permissionOpt?.invoke()
        }
    }

}