package com.rdo.octo.motionlayouttest

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_garbage.*
import kotlin.math.PI
import kotlin.math.sin

class GarbageActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var rocketBackground: RocketBackground
    private var readyToLaunch = false

    override fun onShowPress(e: MotionEvent?) {
        //Toast.makeText(this, "onShowPress", Toast.LENGTH_LONG).show()
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        //Toast.makeText(this, "onDown", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val yStuff = e2.y - e1.y
        if (readyToLaunch) {
            launch()
        } else {
            moveBackToZero(yStuff)
        }
        return false
    }

    private fun moveBackToZero(yStuff: Float) {
        val animator = ValueAnimator.ofInt(yStuff.toInt(), 0)
        animator.duration = 1000
        animator.interpolator = BounceInterpolator()
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            containerLottie.translationY = (1f * value) - containerLottie.height
            rocketBackground.setTranslation(value)
        }
        animator.start()
    }

    private fun launch() {
        val animator = ValueAnimator.ofInt(containerLottie.height, 0)
        animator.duration = 200
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            containerLottie.translationY = (1f * value) - containerLottie.height
        }
        animator.start()
        rocketBackground.launch()
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        val xStuff = e2.x - e1.x
        val yStuff = e2.y - e1.y
        if (yStuff > 0 && yStuff < containerLottie.height) {
            containerLottie.translationY = (1f * yStuff) - containerLottie.height
            rocketBackground.setTranslation(yStuff.toInt())
            readyToLaunch = false
        } else if (yStuff > containerLottie.height) {
            readyToLaunch = true
        }
        testView.text = listOf(
            xStuff.toString(),
            yStuff.toString(),
            distanceX.toString(),
            distanceY.toString()
        ).joinToString("\n")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private lateinit var detector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_garbage)
        paint.color = ContextCompat.getColor(this, R.color.colorPrimary)
        detector = GestureDetectorCompat(this, this)
        containerLottie.post {
            //testView.background = SquareDrawable(testView.height, testView.width, paint)
            rocketBackground = RocketBackground(containerLottie.height, containerRocket.width, paint)
            containerRocket.background = rocketBackground
            containerLottie.translationY = -1f * containerLottie.height
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}

class SquareDrawable(private val height: Int, private val width: Int, private val paint: Paint) : Drawable() {

    override fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), 0f)
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}

class RocketBackground(private val height: Int, private val width: Int, private val paint: Paint) : Drawable() {

    private var translation = 0
    private var percent = 1f

    fun setTranslation(translation: Int) {
        this.translation = translation
        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        val baseY = translation / 2f
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, baseY)
        for (i in 1..width) {
            val angle = (i.toFloat() * PI) / width
            val y = (translation / 2) * (1 + (percent * sin(angle)))
            path.lineTo(i.toFloat(), y.toFloat())
        }
        path.lineTo(width.toFloat(), baseY)
        path.lineTo(width.toFloat(), 0f)
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun getIntrinsicHeight(): Int {
        return height
    }

    override fun getIntrinsicWidth(): Int {
        return width
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    fun launch() {
        val animator = ValueAnimator.ofFloat(0f, 4f)
        animator.addUpdateListener {
            val fl = it.animatedValue as Float
            percent = when {
                fl < 1f -> 1 - fl
                fl < 2f -> 1 - fl
                fl < 3f -> fl - 3f
                else -> fl - 3f
            }
            callback?.invalidateDrawable(this)
        }
        animator.interpolator = DecelerateInterpolator()
        animator.duration = 1000
        val animator2 = ValueAnimator.ofInt(translation, 0)
        animator2.duration = 1000
        animator2.addUpdateListener {
            val value = it.animatedValue as Int
            setTranslation(value)
        }
        animator2.startDelay = 250
        animator.start()
        animator2.start()
    }
}