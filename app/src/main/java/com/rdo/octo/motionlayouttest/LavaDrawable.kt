package com.rdo.octo.motionlayouttest

import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LavaDrawable(private val paint: Paint, private val width: Int, private val height: Int) : Drawable() {

    private var distance = 0

    fun setDistance(newDistance: Int = distance) {
        this.distance = newDistance
        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        if (distance < height) {
            val path = Path()
            path.moveTo(width / 2f, height / 2f)
            for (i in 0..628) {
                val angle = i / 100.toDouble()
                val x = width / 2f * (1 + cos(angle))
                val y = height / 2f * (1 + sin(angle))
                path.lineTo(x.toFloat(), y.toFloat())
            }
            path.close()
            canvas.drawPath(path, paint)
        } else {
            val path = Path()
            val y1 = (distance / 2).toFloat()
            val x1 = sqrt((3 * height / 2) * (3 * height / 2).toFloat() - (distance * distance / 4))

        }
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}