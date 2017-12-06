package com.opensource.legosdk.core

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

/**
 * Created by cuiminghui on 2017/5/15.
 */
class LGOModalWebViewActivity : LGOWebViewActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent?.let { intent ->
            intent.getIntExtra("LGOModalController.ModalType", -1)?.let {
                when (it) {
                    1, 2, 3, 4, 5 -> {
                        var modalWidth = WindowManager.LayoutParams.MATCH_PARENT
                        intent.getIntExtra("LGOModalController.ModalWidth", -1)?.let {
                            if (it > -1) {
                                modalWidth = (it * resources.displayMetrics.density).toInt()
                            }
                        }
                        var modalHeight = WindowManager.LayoutParams.MATCH_PARENT
                        intent.getIntExtra("LGOModalController.ModalHeight", -1)?.let {
                            if (it > -1) {
                                modalHeight = (it * resources.displayMetrics.density).toInt()
                            }
                        }
                        window.setLayout(modalWidth, modalHeight)
                    }
                    else -> {
                        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
                    }
                }
                when (it) {
                    2 -> {
                        val attrs = window.attributes
                        attrs.y = -9999
                        attrs.windowAnimations = R.style.ModalTopAnimation
                        attrs.dimAmount = if (intent.getBooleanExtra("LGOModalController.clearMask", false) || intent.getBooleanExtra("LGOModalController.nonMask", false)) 0.0f else 0.5f
                        window.attributes = attrs
                    }
                    3 -> {
                        val attrs = window.attributes
                        attrs.x = -9999
                        attrs.windowAnimations = R.style.ModalLeftAnimation
                        attrs.dimAmount = if (intent.getBooleanExtra("LGOModalController.clearMask", false) || intent.getBooleanExtra("LGOModalController.nonMask", false)) 0.0f else 0.5f
                        window.attributes = attrs
                    }
                    4 -> {
                        val attrs = window.attributes
                        attrs.y = 9999
                        attrs.windowAnimations = R.style.ModalBottomAnimation
                        attrs.dimAmount = if (intent.getBooleanExtra("LGOModalController.clearMask", false) || intent.getBooleanExtra("LGOModalController.nonMask", false)) 0.0f else 0.5f
                        window.attributes = attrs
                    }
                    5 -> {
                        val attrs = window.attributes
                        attrs.x = 9999
                        attrs.windowAnimations = R.style.ModalRightAnimation
                        attrs.dimAmount = if (intent.getBooleanExtra("LGOModalController.clearMask", false) || intent.getBooleanExtra("LGOModalController.nonMask", false)) 0.0f else 0.5f
                        window.attributes = attrs
                    }
                }
            }
            intent.getBooleanExtra("LGOModalController.clearWebView", false)?.let { clearWebView ->
                if (clearWebView) {
                    webView.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    webView.setBackgroundColor(Color.WHITE)
                }
            }
        }
        setFinishOnTouchOutside(!intent.getBooleanExtra("LGOModalController.nonMask", false))
    }

}