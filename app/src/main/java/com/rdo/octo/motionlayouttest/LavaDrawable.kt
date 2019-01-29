package com.rdo.octo.motionlayouttest

import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import kotlin.math.*

class LavaDrawable(private var paint: Paint, private val width: Int, private val height: Int) : Drawable() {

    private var distance = 0

    fun setDistance(newDistance: Int = distance) {
        if (newDistance > 3 * height) {
            // Todo animate to 0
        } else if (abs(this.distance - newDistance) > 2) {
            this.distance = newDistance
            callback?.invalidateDrawable(this)
        }
    }

    override fun draw(canvas: Canvas) {
        if (abs(distance) < height / 10) {
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
        } else if (distance > 0) {
            val path = Path()
            val r = height / 2
            val y1 = (distance / 2).toFloat()
            val endCircleAngle = asin(y1 * 2 / (3 * r))
            val x1 = cos(endCircleAngle) * 3 / 2 * r
            val startCircleAngle = PI - endCircleAngle
            val b = endCircleAngle
            path.moveTo(width / 2f, height / 2f)
            for (i in ((endCircleAngle + 2 * PI) * 100).toInt() downTo (startCircleAngle * 100).toInt()) {
                val angle = i / 100.toDouble()
                val x = width / 2f * (1 + cos(angle))
                val y = height / 2f * (1 - sin(angle))
                path.lineTo(x.toFloat(), y.toFloat())
            }
            drawRightPartOfbubble(b, r, x1, y1, path)
            drawLeftPartOfBubble(b, r, x1, y1, path)
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
            path.moveTo(width / 2f, height / 2f)
            drawCircleDown(startCircleAngle, endCircleAngle, path)
            Log.d("YOLO", "x1 est egal a $x1, b vaut ${b / PI * 180}")
            for (i in ((PI - b - 1) * 100).toInt()..(PI * 100).toInt()) {
                val angle = i / 100.toDouble()
                val x = width / 2 + (cos(angle) * r / 2f) + x1
                val y = (r / -2f * sin(angle)) + (height / 2) - y1
                path.lineTo(x.toFloat(), y.toFloat())
            }
            for (i in 0..(-b + 1).toInt() * 100) {
                val angle = i / 100.toDouble()
                val x = maxOf(width / 2 + (cos(angle) * r / 2f) - x1, (width / 2 + (cos(-b) * r / 2f) - x1).toDouble())
                val y = (r / -2f * sin(angle)) + (height / 2) - y1
                path.lineTo(x.toFloat(), y.toFloat())
            }
            path.close()
            canvas.drawPath(path, paint)
        }
    }

    private fun drawCircleDown(startCircleAngle: Double, endCircleAngle: Float, path: Path) {
        for (i in (startCircleAngle * 100).toInt() downTo ((endCircleAngle) * 100).toInt()) {
            val angle = i / 100.toDouble()
            val x = width / 2f * (1 + cos(angle))
            val y = height / 2f * (1 - sin(angle))
            path.lineTo(x.toFloat(), y.toFloat())
        }
    }

    private fun drawLeftPartOfBubble(
        b: Float,
        r: Int,
        x1: Float,
        y1: Float,
        path: Path
    ) {
        for (j in (PI * 100 - 1).toInt()..(PI + b + 1).toInt() * 100) {
            val angle = j / 100.toDouble()
            val x = minOf(
                width / 2 + (cos(angle) * r / 2f) + x1,
                width / 2 + (cos((PI + b)) * r / 2f) + x1
            )
            val y = (r / -2f * sin(angle)) + (height / 2) - y1
            path.lineTo(x.toFloat(), y.toFloat())
        }
    }

    private fun drawRightPartOfbubble(
        b: Float,
        r: Int,
        x1: Float,
        y1: Float,
        path: Path
    ) {
        for (i in ((-b) * 100).toInt()..0) {
            val angle = i / 100.toDouble()
            val x = width / 2 + (cos(angle) * r / 2f) - x1
            val y = (r / -2f * sin(angle)) + (height / 2) - y1
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

    fun setPaint(paintLavaTop: Paint) {
        this.paint = paintLavaTop
    }
}