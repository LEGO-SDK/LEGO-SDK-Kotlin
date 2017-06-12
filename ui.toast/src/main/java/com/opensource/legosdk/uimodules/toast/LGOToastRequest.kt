package com.opensource.legosdk.uimodules.toast

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.text.Layout
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.opensource.legosdk.core.LGORequest
import com.opensource.legosdk.core.LGORequestContext
import java.io.ByteArrayInputStream

/**
 * Created by cuiminghui on 2017/6/12.
 */
class LGOToastRequest(val opt: String, val style: String, val title: String, val timeout: Int, context: LGORequestContext?) : LGORequest(context) {

    fun toastView(): View? {
        context?.requestContentContext()?.let { context ->
            val toast = object: FrameLayout(context) {
                override fun onDraw(canvas: Canvas?) {
                    super.onDraw(canvas)
                    canvas?.let { canvas ->
                        val paint = Paint()
                        paint.color = 0xa0000000.toInt()
                        canvas.drawRoundRect(RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat()), 6.0f * resources.displayMetrics.density, 6.0f * resources.displayMetrics.density, paint)
                    }
                }
            }
            toast.setBackgroundColor(Color.TRANSPARENT)
            if (style.equals("success")) {
                val imageView = ImageView(context)
                imageView.setImageDrawable(BitmapDrawable.createFromStream(ByteArrayInputStream(Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAGIAAABGCAMAAAAARrEcAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAIcUExURUdwTP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////9ONOj4AAACzdFJOUwABAgP7BPz6Bf79+R6wKuy/E+8kmajOPOkYMo9A9uDUKw/T8wgWiIaOf5Bwl6toY3ygVRpgtFx1T3nfCwox7vHnxhfIM8316IkSuNa7RObj2t7M3YJ9eHt0bqdklGxvvIVM+FEjVj2cByBiZ4QJR8NyWCaA9JEMaQbRLL2KokPcN9dNEOLtW0ZfUzrkm5VQy5+jS0lIPvc4DaHHHzUvIjAZNi4cphvqncopOeUoHdiqr2oRGMZ4wQAABBZJREFUWMO1mGVbG0EQgOdydzkBAqUUp7RYvaVASwsVCjUoUHd3d3d3d3d3mT/Y2YvthijZy4dwecjtm5n3Znd2Adx66QCN4wFU1wBggnkX8aZ7DMWEzNuoaXjHrRA8Hrh2HQ3L8uJndwiUmwYbDUQkxgeXRK9B1JC9DKx0RfTOEezn+wnZb+WL1mFrPnqtACG3WL5oFUoLSXSAMOM1BSVd9Ch0RDuEwgHSCTTeiaBoIsz2uUCYkBUUzQh9ZROoovcdRc32EzJwrnQCVfSpi0ENRJj/UzaBRB9GjvArUzaBKrokJJoIs1plE0w4NAbtgGiKpeKNZAJVtG8WalaIkN8umUCiL+dSckKEeX0kE0j0sLBounj2hKmRW9EHw6KJMGWg5BhMON+Jth0m5FTJjYFEHzsbnFj9hAlyCR4Fzh0Ia7AM7NDlEmiwPZxoS8MOUy7BBCWPE00xZCmyCe3H0WtzMUgmkOji05xoIvQDqQQSPW56WANbiOISPKaauujxyBM0HB2vffVAqhGS6M2IXj6GuAQVinYMANWTCqFoNyeazeLD4xEUgDGYvT75QBQVpjZxotn0EZdAIw/BMsSZkOTkpSqwcR2ngcUwNC7BhFInl7i6nt2djOhart6cGEoSbFSqm9gNtoEtu5LY0tAEcTLUKgUINf4nJvbrTyBoeq9NmCwT6qfwotG2cFAiwt9Q0PQ3i1ZEJW5PfOYCL5oRhiQiwMzwokve+hewByb2CjpW40UjhbMwIQGqOoX53rsqZrJI9H4UCKRkESiJC6oqh7uNkrWsOXqyTDjSj69o53pJMgQdmtvCuWLJKi9mbUuUXW5bqCcOEoZF+WZUxsAv/IRmYMbyHqVOogtmCKIZYSWbcJObcfpU8CmmyzxdFEKir4iiGWFksgTGqC7n76fnfpqPnxcppEFCRTuEUUnNBiHGx/k8g5I1uS6cLBO2jBAq2sE1pEJggzyfK+SBrocGnl4SvSFfFM0Il1IjsMH6douPvIU5maDqHt0DmwpF0YywN1UCY3xtERiUrOy1zr+2W6JoRrgKeqoExng3SRyKPgz2tU7MixBNn6xbvSEwxtRckUEjeydZaNkRBO1eb5sZEx5PFxkW+/maoIG+oD3ofbtkwsOyiKTYXjEEIjzqSmf7oEOXFsHASEJZaXotnwn37XgMAysL0t0C6TAOYzOo6L+lv8kyoU5cFARC7gsZ2zgVxsZgUC1OlLJRpKZ+W1SGgZ9eStqKUnswMgrDwG55R2TEWNqDYeD3HxK307TiNzoNDE/oL/cAixiLue26Q3gl++BEgQXc9GfgHOkHWIxRE+pdifBeOsFpIgcHzowNrChygeAwVjgMA8urXSE4jBvUFWTIP8AS+rOnlKdp/1wjOIySyt9pH2D9B0gXLwUiIj7lAAAAAElFTkSuQmCC", 0)), "icon.png"))
                toast.addView(imageView, ViewGroup.LayoutParams((49 * context.resources.displayMetrics.density).toInt(), (35 * context.resources.displayMetrics.density).toInt()))
                toast.addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                    imageView.x = ((view.width - imageView.width) / 2.0).toFloat()
                    imageView.y = ((view.height - imageView.height) / 2.0 - 14 * context.resources.displayMetrics.density).toFloat()
                }
            }
            else if (style.equals("error")) {
                val imageView = ImageView(context)
                imageView.setImageDrawable(BitmapDrawable.createFromStream(ByteArrayInputStream(Base64.decode("iVBORw0KGgoAAAANSUhEUgAAAFoAAABaCAMAAAAPdrEwAAAABGdBTUEAALGPC/xhBQAAAAFzUkdCAK7OHOkAAAKaUExURUdwTP////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////7+/v39/QAAAPz8/EtLS0hISEZGRkpKSklJSZOTk5qams7OzkVFRfn5+fb29ouLi/r6+szMzN7e3tnZ2TQ0NEBAQEdHR+jo6Hd3dyIiIj4+PkRERObm5vX19ff398XFxa+vr/Pz84iIiPLy8tLS0o+Pj/j4+J6enrGxse7u7oGBgTo6OtbW1hwcHAEBAScnJwUFBZSUlOvr68nJyTU1NcTExOPj4y4uLuXl5aGhob29vefn51lZWSgoKEJCQri4uF5eXgwMDGNjYxkZGampqbOzs7CwsM/Pz3h4eD8/PyMjI+np6XBwcBcXF1BQUGlpaW9vbwQEBBYWFk9PTwMDAx8fHxUVFWhoaB4eHhQUFK6urq2trZ2XQNwAAACBdFJOUwAB+/n8AgT+/QP67/jeBXELEzfkKPJMd/B6w/PQxJXLXtX1PMYdoZzjr1esMTI2bJHpClQSlDXHVdtyyD6M2g8atGsX4dnUGRSIYuqbLvYcECUGq92K3KA5wEInzlwfsyG8OB660krPU0mAqSKSI1gpxZOl0W2kuLd5NH3up6btfnt1G/sAAAZJSURBVFjDrZn1QxtLEMc3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", 0)), "icon.png"))
                toast.addView(imageView, ViewGroup.LayoutParams((45 * context.resources.displayMetrics.density).toInt(), (45 * context.resources.displayMetrics.density).toInt()))
                toast.addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                    imageView.x = ((view.width - imageView.width) / 2.0).toFloat()
                    imageView.y = ((view.height - imageView.height) / 2.0 - 14 * context.resources.displayMetrics.density).toFloat()
                }
            }
            else if (style.equals("loading")) {
                val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
                progressBar.indeterminateDrawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY)
                progressBar.isIndeterminate = true
                toast.addView(progressBar, ViewGroup.LayoutParams((54 * context.resources.displayMetrics.density).toInt(), (54 * context.resources.displayMetrics.density).toInt()))
                toast.addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
                    progressBar.x = ((view.width - progressBar.width) / 2.0).toFloat()
                    progressBar.y = ((view.height - progressBar.height) / 2.0 - 14 * context.resources.displayMetrics.density).toFloat()
                }
            }
            if (title.length > 0) {
                val titleLabel = TextView(context)
                titleLabel.textSize = 16f
                titleLabel.setTextColor(0xe0ffffff.toInt())
                titleLabel.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
                titleLabel.text = title
                titleLabel.y = 80 * context.resources.displayMetrics.density
                toast.addView(titleLabel, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (30 * context.resources.displayMetrics.density).toInt()))
            }
            return toast
        }
        return null
    }

}