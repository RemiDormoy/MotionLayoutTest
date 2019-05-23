package com.rdo.octo.motionlayouttest

import android.app.Activity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import androidx.core.view.GestureDetectorCompat
import kotlinx.android.synthetic.main.activity_food.*

class FoodActivity : Activity(), GestureDetector.OnGestureListener {

    private val heights: List<Float> by lazy {
        views.map { it.height.toFloat() }
    }

    private val views: List<View> by lazy {
        listOf(
            cardViewFood1,
            cardViewFood2,
            cardViewFood3,
            cardViewFood4,
            cardViewFood5
        )
    }

    private var currentPosition = 0

    private var currentScroll = 0f

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        val scroll = e2.y - e1.y
        if (currentPosition > 0 && scroll > heights[currentPosition - 1] / 3) {
            views[currentPosition - 1].animate().translationY(0f).setInterpolator(AccelerateDecelerateInterpolator())
                .start()
            currentPosition--
        } else if (currentPosition > 0 && scroll > 0) {
            views[currentPosition - 1].animate().translationY(heights[currentPosition - 1])
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        } else if (scroll < heights[currentPosition] / -3) {
            views[currentPosition].animate().translationY(-heights[currentPosition])
                .setInterpolator(AccelerateDecelerateInterpolator()).start()
            currentPosition++
        } else {
            views[currentPosition].animate().translationY(0f).setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
        return false
    }

    override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
        currentScroll = e2.y - e1.y
        if (currentScroll < 0) {
            views[currentPosition].translationY = currentScroll
        } else if (currentPosition > 0) {
            views[currentPosition - 1].translationY = -heights[currentPosition - 1] + currentScroll
        }
        return true
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    private lateinit var detector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food)
        detector = GestureDetectorCompat(this, this)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (detector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }
}
