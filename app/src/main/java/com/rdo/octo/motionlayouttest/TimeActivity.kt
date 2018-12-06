package com.rdo.octo.motionlayouttest

import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintSet
import android.support.v7.app.AppCompatActivity
import android.transition.TransitionManager
import android.view.MotionEvent
import kotlinx.android.synthetic.main.state_1.*
import android.view.ScaleGestureDetector
import android.widget.Toast


class TimeActivity : AppCompatActivity() {

    private val set1 = ConstraintSet()
    private val set2 = ConstraintSet()
    private val detector : ScaleGestureDetector by lazy {
        ScaleGestureDetector(this,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

                private var scaleFactor = 1f

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    // store scale factor for detect zoom "direction" on end
                    scaleFactor = detector.scaleFactor
                    //Toast.makeText(this@TimeActivity, "scale : ${detector.scaleFactor}", Toast.LENGTH_SHORT).show()
                    return true
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {
                    if (scaleFactor > 1) {
                        TransitionManager.beginDelayedTransition(container)
                        set1.applyTo(container)
                    } else {
                        TransitionManager.beginDelayedTransition(container)
                        set2.applyTo(container)
                    }
                    super.onScaleEnd(detector)
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)
        set1.clone(this, R.layout.state_1)
        set2.clone(this, R.layout.state_2)
        //moveTo2()
    }

    private fun moveTo2() {
        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(container)
            set2.applyTo(container)
            moveTo1()
        }, 5000)
    }

    private fun moveTo1() {
        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(container)
            set1.applyTo(container)
            moveTo2()
        }, 5000)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        detector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }
}