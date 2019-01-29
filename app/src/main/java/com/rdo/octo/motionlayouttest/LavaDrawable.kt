package com.rdo.octo.motionlayouttest

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import kotlin.math.*

class LavaDrawable(private val paint: Paint, private val width: Int, private val height: Int) : Drawable() {

    private var distance = 0

    fun setDistance(newDistance: Int = distance) {
        if (newDistance > 3 * height) {
            // Todo animate to 0
        } else if (abs(this.distance - newDistance) > 5) {
            this.distance = newDistance
            callback?.invalidateDrawable(this)
        }
    }

    override fun draw(canvas: Canvas) {
        if (distance == 0) {
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
            val r = height / 2
            val y1 = (distance / 2).toFloat()
            val endCircleAngle = asin(y1 * 2 / (3 * r))
            val x1 = cos(endCircleAngle) * 3 / 2 * r
            val startCircleAngle = PI - endCircleAngle
            val b = endCircleAngle
            val angleInDegree = endCircleAngle * 180 / PI
            val angleInDegree2 = startCircleAngle * 180 / PI
            Log.d(
                "angle angle yolo",
                "l'angle est de départ est $angleInDegree2, l'angle d'arrivée est $angleInDegree, x1 vaut $x1 et la distance $distance"
            )
            drawFirstCirclePartially(path, endCircleAngle, startCircleAngle)
            for (i in ((-b) * 100).toInt()..0) {
                val angle = i / 100.toDouble()
                val x = width / 2 + (cos(angle) * r / 2f) - x1
                val y = (r / -2f * sin(angle)) + (height / 2) - y1
                path.lineTo(x.toFloat(), y.toFloat())
            }
            for (i in (PI * 100).toInt()..(PI + b).toInt() * 100) {
                val angle = i / 100.toDouble()
                val x = width / 2 + (cos(angle) * r / 2f) + x1
                val y = (r / -2f * sin(angle)) + (height / 2) - y1
                path.lineTo(x.toFloat(), y.toFloat())
            }
            path.moveTo(width / 2f, height / 2f)
            path.close()
            canvas.drawPath(path, paint)
        }
    }

    private fun drawFirstCirclePartially(
        path: Path,
        endCircleAngle: Float,
        startCircleAngle: Double
    ) {
        path.moveTo(width / 2f, height / 2f)
        for (i in ((endCircleAngle + 2 * PI) * 100).toInt() downTo (startCircleAngle * 100).toInt()) {
            val angle = i / 100.toDouble()
            val x = width / 2f * (1 + cos(angle))
            val y = height / 2f * (1 - sin(angle))
            path.lineTo(x.toFloat(), y.toFloat())
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