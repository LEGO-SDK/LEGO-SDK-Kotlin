package com.opensource.legosdk.uimodules.page

import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext

/**
 * Created by cuiminghui on 2017/4/23.
 */
class LGOPageRequest(context: LGORequestContext?) : LGORequest(context) {

    var urlPattern: String? = null

    var title: String? = null

    var backgroundColor: String? = null

    var statusBarHidden: Boolean = false

    var navigationBarHidden: Boolean = false

    var navigationBarSeparatorHidden: Boolean = false

    var navigationBarBackgroundColor: String? = null

    var navigationBarTintColor: String? = null

    var fullScreenContent: Boolean = false

    var showsIndicator: Boolean = true

}