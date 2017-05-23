package com.opensource.legosdk.core

import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toolbar

/**
 * Created by cuiminghui on 2017/5/16.
 */
class LGONavigationBar @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        minimumHeight = (48.0 * context.resources.displayMetrics.density).toInt()
    }

    var activity: LGOWebViewActivity? = null
        get() {
            return context as? LGOWebViewActivity
        }

    var titleView: View? = null

    var leftView: View? = null

    var rightView: View? = null

    var tintColor: Int = Color.WHITE
        set(value) {
            field = value
            reload()
        }

    var barTintColor: Int = Color.BLACK

    var statusBarTranslucent: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            if (!value) {
                minimumHeight = (48.0 * context.resources.displayMetrics.density).toInt()
            }
            else {
                minimumHeight = (68.0 * context.resources.displayMetrics.density).toInt()
            }
            activity?.resetLayouts()
        }

    var hidden: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            activity?.resetLayouts()
        }

    fun reload() {
        if (LGOWebViewActivity.navigationBarDrawable != null) {
            this.background = LGOWebViewActivity.navigationBarDrawable
        }
        else {
            this.setBackgroundColor(barTintColor)
        }
        this.removeAllViews()
        activity?.navigationItems?.rightBarButtonItem?.let {
            resetRightView()?.let {
                if (statusBarTranslucent) {
                    it.setPadding(it.paddingLeft, (20 * context.resources.displayMetrics.density).toInt(), it.paddingRight, it.paddingBottom)
                }
                this.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
            }
        }
        resetTitleView()?.let {
            if (statusBarTranslucent) {
                it.setPadding(it.paddingLeft, (20 * context.resources.displayMetrics.density).toInt(), it.paddingRight, it.paddingBottom)
            }
            this.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f))
        }
        activity?.navigationItems?.leftBarButtonItem?.let {
            resetLeftView()?.let {
                if (statusBarTranslucent) {
                    it.setPadding(it.paddingLeft, (20 * context.resources.displayMetrics.density).toInt(), it.paddingRight, it.paddingBottom)
                }
                this.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
            }
        }
        if (activity?.navigationItems?.leftBarButtonItem == null) {
            resetBackView()?.let {
                if (statusBarTranslucent) {
                    it.setPadding(it.paddingLeft, (20 * context.resources.displayMetrics.density).toInt(), it.paddingRight, it.paddingBottom)
                }
                this.addView(it, 0, LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.0f))
            }
        }
    }

    fun requestNavigationItemContentView(activity: LGOWebViewActivity, item: LGONavigationItem.LGOBarButtonItem): View {
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
        if (activity?.navigationItems?.hasBackStack ?: false) {
            activity?.let { activity ->
                activity?.navigationItems.backBarButtonItem?.let {
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
            activity.navigationItems.leftBarButtonItem?.let {
                val contentView = requestNavigationItemContentView(activity, it)
                contentView.setPadding((12 * (activity.resources.displayMetrics.density)).toInt(), 0, (12 * (activity.resources.displayMetrics.density)).toInt(), 0)
                leftView = contentView
            }
        }
        return leftView
    }

    fun resetRightView(): View? {
        activity?.let { activity ->
            activity.navigationItems.rightBarButtonItem?.let {
                val contentView = requestNavigationItemContentView(activity, it)
                contentView.setPadding((12 * (activity.resources.displayMetrics.density)).toInt(), 0, (12 * (activity.resources.displayMetrics.density)).toInt(), 0)
                rightView = contentView
            }
        }
        return rightView
    }

    fun resetTitleView(): View? {
        activity?.let { activity ->
            ((titleView as? TextView) ?: TextView(activity))?.let { contentView ->
                contentView.text = activity?.title
                contentView.setSingleLine(true)
                contentView.ellipsize = TextUtils.TruncateAt.END
                contentView.setPadding(if (activity.navigationItems.leftBarButtonItem != null) 0 else (12 * (activity.resources.displayMetrics.density)).toInt(), 0, 0, 0)
                contentView.textSize = 18.0f
                contentView.gravity = Gravity.CENTER_VERTICAL or Gravity.LEFT
                contentView.setTextColor(tintColor)
                contentView.setBackgroundColor(Color.TRANSPARENT)
                titleView = contentView
            }
        }
        return titleView
    }

}