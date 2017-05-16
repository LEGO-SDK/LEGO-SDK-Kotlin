package com.opensource.legosdk.uimodules.navigationcontroller

import android.content.Intent
import com.opensource.legosdk.core.LGORequestable
import com.opensource.legosdk.core.LGOResponse
import com.opensource.legosdk.core.LGOWebViewActivity

/**
 * Created by cuiminghui on 2017/4/24.
 */
class LGONavigationOperation(val request: LGONavigationRequest): LGORequestable() {

    override fun requestSynchronize(): LGOResponse {
        request.context?.requestContentContext()?.let {
            when (request.opt) {
                "push" -> {
                    val intent = Intent(it, LGOWebViewActivity::class.java)
                    intent.putExtra("LGONavigationController.RequestPath", request.path)
                    intent.putExtra("LGONavigationController.Class", true)
                    request.args?.let {
                        intent.putExtra("LGONavigationController.args", it.toString())
                    }
                    it.startActivity(intent)
                    return LGOResponse().accept(null)
                }
                "pop" -> {
                    request.context.requestActivity()?.finish()
                }
                else -> {
                    return LGOResponse().reject("UI.NavigationController", -2, "invalid opt.")
                }
            }
        }
        return LGOResponse().reject("UI.NavigationController", -1, "Context error.")
    }

}