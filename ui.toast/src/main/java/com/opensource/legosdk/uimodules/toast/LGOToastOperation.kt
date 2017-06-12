package com.opensource.legosdk.uimodules.toast

import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import java.util.*

/**
 * Created by cuiminghui on 2017/6/12.
 */
class LGOToastOperation(val request: LGOToastRequest): LGORequestable() {

    companion object {

        var sharedContainer: View? = null
            private set

        var sharedTimer = Timer()
            private set

    }

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestActivity()?.let { activity ->
            activity.runOnUiThread {
                if (request.opt.equals("hide")) {
                    sharedContainer?.let { container ->
                        (container.parent as? ViewGroup)?.let {
                            it.removeView(container)
                        }
                    }
                    sharedContainer = null
                }
                else {
                    sharedContainer?.let { container ->
                        (container.parent as? ViewGroup)?.let {
                            it.removeView(container)
                        }
                    }
                    sharedContainer = null
                    val scale = activity.resources.displayMetrics.density
                    val container = object: FrameLayout(activity) {
                        override fun onTouchEvent(event: MotionEvent?): Boolean {
                            return true
                        }
                    }
                    sharedContainer = container
                    container.setBackgroundColor(Color.TRANSPARENT)
                    request.toastView()?.let { toastView ->
                        container.addView(toastView, ViewGroup.LayoutParams((120 * scale).toInt(), (120 * scale).toInt()))
                        container.addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                            toastView.x = ((view.width - 120 * scale) / 2.0).toFloat()
                            toastView.y = ((view.height - 120 * scale) / 2.0).toFloat()
                        }
                    }
                    activity.addContentView(container, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    sharedTimer.cancel()
                    sharedTimer.purge()
                    sharedTimer = Timer()
                    sharedTimer.schedule(object: TimerTask(){
                        override fun run() {
                            activity.runOnUiThread {
                                (container.parent as? ViewGroup)?.let {
                                    it.removeView(container)
                                }
                            }
                        }
                    }, if (request.style.equals("loading")) (request.timeout * 1000).toLong() else 2000 )
                }
            }
        }
        return super.requestSynchronize()
    }

}