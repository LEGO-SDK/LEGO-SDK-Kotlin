package com.opensource.legosdk.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Base64
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.*

var backButtonImage: Bitmap? = null

inline fun requestBackButtonBitmap(): Bitmap? {
    if (backButtonImage == null) {
        val backButtonIcon = Base64.decode("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", 0)
        BitmapFactory.decodeByteArray(backButtonIcon, 0, backButtonIcon.size)?.let {
            it.density = 3
            backButtonImage = it
        }
    }
    return backButtonImage
}

/**
 * Created by cuiminghui on 2017/5/11.
 */
class LGOActionBarController {

    class LGOActionBarItem(val title: String?, val image: Bitmap?, val triggerBlock: (() -> Unit)?)

    var hasBackStack: Boolean = false
        get() {
            activity?.let {
                it.intent?.let {
                    return it.getBooleanExtra("LGONavigationController.Class", false)
                }
            }
            return false
        }

    var tintColor: Int = Color.WHITE
        set(value) {
            field = value
            reload()
        }

    var backBarButtonItem: LGOActionBarItem = LGOActionBarItem(null, requestBackButtonBitmap(), {
        activity?.finish()
    })

    var leftBarButtonItem: LGOActionBarItem? = null
        set(value) {
            field = value
            reload()
        }

    var rightBarButtonItem: LGOActionBarItem? = null
        set(value) {
            field = value
            reload()
        }

    var activity: LGOWebViewActivity? = null

    var contentView: LinearLayout? = null
        get() {
            if (field == null) {
                activity?.let {
                    field = LinearLayout(it)
                }
            }
            return field
        }

    var titleView: View? = null
        get() {
            if (field == null) {
                activity?.let {
                    field = TextView(it)
                }
            }
            return field
        }

    var leftView: View? = null
    var rightView: View? = null

    fun reload() {
        contentView?.let {
            activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            activity?.supportActionBar?.setDisplayShowHomeEnabled(false)
            activity?.supportActionBar?.setDisplayShowCustomEnabled(true)
            activity?.supportActionBar?.setDisplayShowTitleEnabled(false)
            it.removeAllViews()
            rightBarButtonItem?.let {
                resetRightView()?.let {
                    contentView?.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
                }
            }
            resetTitleView()
            it.addView(titleView, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))
            leftBarButtonItem?.let {
                resetLeftView()?.let {
                    contentView?.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
                }
            }
            if (leftBarButtonItem == null) {
                resetBackView()?.let {
                    contentView?.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
                }
            }
            activity?.supportActionBar?.setCustomView(it, ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT))
            (it.parent as? Toolbar)?.setContentInsetsAbsolute(0, 0)
        }
    }

    fun requestNavigationItemContentView(activity: LGOWebViewActivity, item: LGOActionBarItem): View {
        val contentView = LinearLayout(activity)
        item.title?.let {
            val textView = TextView(activity)
            textView.setSingleLine(true)
            textView.text = it
            textView.textSize = 16.0f
            textView.setTextColor(tintColor)
            textView.gravity = Gravity.CENTER_VERTICAL
            contentView.addView(textView, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
        }
        item.image?.let {
            val imageView = ImageView(activity)
            imageView.setImageBitmap(it)
            imageView.setColorFilter(tintColor)
            contentView.addView(imageView, 0, LinearLayout.LayoutParams((it.width / it.density * activity.resources.displayMetrics.density).toInt(), LinearLayout.LayoutParams.MATCH_PARENT))
        }
        contentView.setBackgroundColor(Color.TRANSPARENT)
        contentView.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    view.alpha = 0.25f
                }
                MotionEvent.ACTION_MOVE -> {
                    if (motionEvent.x / activity.resources.displayMetrics.scaledDensity < -44.0 ||
                        motionEvent.x / activity.resources.displayMetrics.scaledDensity > view.width + 44.0 ||
                        motionEvent.y / activity.resources.displayMetrics.scaledDensity < -44.0 ||
                        motionEvent.y / activity.resources.displayMetrics.scaledDensity > view.height + 44.0) {
                        view.alpha = 1.0f
                    }
                    else {
                        view.alpha = 0.25f
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (motionEvent.x / activity.resources.displayMetrics.scaledDensity < -44.0 ||
                            motionEvent.x / activity.resources.displayMetrics.scaledDensity > view.width + 44.0 ||
                            motionEvent.y / activity.resources.displayMetrics.scaledDensity < -44.0 ||
                            motionEvent.y / activity.resources.displayMetrics.scaledDensity > view.height + 44.0) {
                    }
                    else {
                        item.triggerBlock?.invoke()
                    }
                    view.alpha = 1.0f
                }
                MotionEvent.ACTION_CANCEL -> {
                    view.alpha = 1.0f
                }
            }
            return@setOnTouchListener true
        }
        return contentView
    }

    fun resetBackView(): View? {
        if (hasBackStack) {
            activity?.let { activity ->
                backBarButtonItem?.let {
                    val contentView = requestNavigationItemContentView(activity, it)
                    contentView.setPadding((12 * (activity.resources.displayMetrics.density)).toInt(), 0, (12 * (activity.resources.displayMetrics.density)).toInt(), 0)
                    return contentView
                }
            }
        }
        return null
    }

    fun resetLeftView(): View? {
        activity?.let { activity ->
            leftBarButtonItem?.let {
                val contentView = requestNavigationItemContentView(activity, it)
                contentView.setPadding((12 * (activity.resources.displayMetrics.density)).toInt(), 0, (12 * (activity.resources.displayMetrics.density)).toInt(), 0)
                leftView = contentView
            }
        }
        return leftView
    }

    fun resetRightView(): View? {
        activity?.let { activity ->
            rightBarButtonItem?.let {
                val contentView = requestNavigationItemContentView(activity, it)
                contentView.setPadding((12 * (activity.resources.displayMetrics.density)).toInt(), 0, (12 * (activity.resources.displayMetrics.density)).toInt(), 0)
                rightView = contentView
            }
        }
        return rightView
    }

    fun resetTitleView() {
        activity?.let { activity ->
            (titleView as? TextView)?.let {
                it.text = activity?.title
                it.setSingleLine(true)
                it.ellipsize = TextUtils.TruncateAt.END
                it.setPadding(if (leftBarButtonItem != null) 0 else (12 * (activity.resources.displayMetrics.density)).toInt(), 0, 0, 0)
                it.textSize = 18.0f
                it.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                it.setTextColor(tintColor)
                it.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }

}