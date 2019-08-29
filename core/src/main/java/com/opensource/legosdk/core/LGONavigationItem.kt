package com.opensource.legosdk.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

var backButtonImage: Bitmap? = null
var backButton2Image: Bitmap? = null

inline fun requestBackButtonBitmap(): Bitmap? {
    if (backButtonImage == null) {
        val backButtonIcon = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAEIAAABCCAYAAADjVADoAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTVCNkJDQTgwNkNFMTFFNzg5ODRERDBCODBDNzI0RTMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTVCNkJDQTkwNkNFMTFFNzg5ODRERDBCODBDNzI0RTMiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo1NUI2QkNBNjA2Q0UxMUU3ODk4NEREMEI4MEM3MjRFMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo1NUI2QkNBNzA2Q0UxMUU3ODk4NEREMEI4MEM3MjRFMyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgycSQAAAAGeSURBVHja7Nw9bsJAEAXgdUifnAWuQI1ECsocgYo+hJYTUOYMtOlTRbkKOQAyYxGkFDYumJ/3PIz0JETh4pOR1zuzVHVdl3uV8nAn4IZYSD4kS8lI44KPhAhvkvXf51fJs2ST7Y74j3CpWbafRhtCUz+ZILoQviWrLBDXEKaS3wwQfQiHDI9PNwRkCFcEVAh3BESIEAQ0iDAEJIhQBBSIcAQECAiEaAgYhEgIKIQoCDiECAhIBG8IWARPCGgELwh4BA8ICgRrCBoESwgqBCsIOgQLCEoEbQhaBE0IagQtCHoEDYhBIGhAVGUgdStEcze8t3w/kXyWc8s+BcRgMLSeGvQYmusIagztlSUthsW7BiWG1dsnHYblfgQVhvUOFQ2Gx54lBYbXLjY8hmdfAxrDu9MFixHR+4TEiOqGw2FEzkdAYURPzMBgIMxQQWCgTNWFYyDNWYZioE3ehmEgzmKHYKBO57tjIJ/XcMVAP8HjhsFwpqsP4ykLRB/GNhPENYxxNogujL3GhSvSv014kcwlX5Kd5JgVIs2Cyr1OAgwA39mb1P3SNzkAAAAASUVORK5CYII=", 0)
        BitmapFactory.decodeByteArray(backButtonIcon, 0, backButtonIcon.size)?.let {
            it.density = 3
            backButtonImage = it
        }
    }
    return backButtonImage
}

inline fun requestBackButton2Bitmap(): Bitmap? {
    if (backButton2Image == null) {
        val backButtonIcon = Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAEIAAABCCAYAAADjVADoAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTVCNkJDQTgwNkNFMTFFNzg5ODRERDBCODBDNzI0RTMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTVCNkJDQTkwNkNFMTFFNzg5ODRERDBCODBDNzI0RTMiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo1NUI2QkNBNjA2Q0UxMUU3ODk4NEREMEI4MEM3MjRFMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo1NUI2QkNBNzA2Q0UxMUU3ODk4NEREMEI4MEM3MjRFMyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgycSQAAAAGeSURBVHja7Nw9bsJAEAXgdUifnAWuQI1ECsocgYo+hJYTUOYMtOlTRbkKOQAyYxGkFDYumJ/3PIz0JETh4pOR1zuzVHVdl3uV8nAn4IZYSD4kS8lI44KPhAhvkvXf51fJs2ST7Y74j3CpWbafRhtCUz+ZILoQviWrLBDXEKaS3wwQfQiHDI9PNwRkCFcEVAh3BESIEAQ0iDAEJIhQBBSIcAQECAiEaAgYhEgIKIQoCDiECAhIBG8IWARPCGgELwh4BA8ICgRrCBoESwgqBCsIOgQLCEoEbQhaBE0IagQtCHoEDYhBIGhAVGUgdStEcze8t3w/kXyWc8s+BcRgMLSeGvQYmusIagztlSUthsW7BiWG1dsnHYblfgQVhvUOFQ2Gx54lBYbXLjY8hmdfAxrDu9MFixHR+4TEiOqGw2FEzkdAYURPzMBgIMxQQWCgTNWFYyDNWYZioE3ehmEgzmKHYKBO57tjIJ/XcMVAP8HjhsFwpqsP4ykLRB/GNhPENYxxNogujL3GhSvSv014kcwlX5Kd5JgVIs2Cyr1OAgwA39mb1P3SNzkAAAAASUVORK5CYII=", 0)
        BitmapFactory.decodeByteArray(backButtonIcon, 0, backButtonIcon.size)?.let {
            it.density = 3
            backButton2Image = it
        }
    }
    return backButton2Image
}

/**
 * Created by cuiminghui on 2017/5/11.
 */
class LGONavigationItem {

    class LGOBarButtonItem(val title: String?, val image: Bitmap?, val triggerBlock: (() -> Unit)?)

    var activity: LGOWebViewActivity? = null
    var fragment: LGOWebViewFragment? = null

    var hasBackStack: Boolean = false
        get() {
            (activity ?: fragment?.activity)?.let {
                it.intent?.let {
                    return it.getBooleanExtra("LGONavigationController.Class", false)
                }
            }
            return false
        }

    var backBarButtonItem: LGOBarButtonItem = LGOBarButtonItem(null, requestBackButtonBitmap(), {
        activity?.finish()
        fragment?.activity?.finish()
    })
        set(value) {
            field = value
            activity?.navigationBar?.reload()
            fragment?.navigationBar?.reload()
        }


    var leftBarButtonItem: LGOBarButtonItem? = null
        set(value) {
            field = value
            activity?.navigationBar?.reload()
            fragment?.navigationBar?.reload()
        }

    var rightBarButtonItem: LGOBarButtonItem? = null
        set(value) {
            field = value
            activity?.navigationBar?.reload()
            fragment?.navigationBar?.reload()
        }

}