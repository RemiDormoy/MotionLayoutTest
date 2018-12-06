package com.rdo.octo.motionlayouttest

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.BounceInterpolator
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        listOf(
            textView1,
            textView2,
            textView3,
            textView4,
            textView5,
            textView6,
            textView7,
            textView8,
            textView9,
            textView10,
            textView11,
            textView12,
            textView13,
            textView14,
            textView15,
            textView16,
            textView17,
            textView18,
            textView19,
            textView20,
            textView21,
            textView22,
            textView23,
            textView24
        ).forEach { textView ->
            textView.setOnClickListener {
                animateView(textView)
            }
        }

        listOf(
            imageView1,
            imageView2,
            imageView3,
            imageView4,
            imageView5,
            imageView6,
            imageView7,
            imageView8,
            imageView9,
            imageView10,
            imageView11,
            imageView12,
            imageView13,
            imageView14,
            imageView15,
            imageView16,
            imageView17,
            imageView18,
            imageView19,
            imageView20,
            imageView21,
            imageView22,
            imageView23,
            imageView24
        ).forEachIndexed { index, imageView ->
            imageView.postDelayed(
                {
                    imageView.visibility = VISIBLE
                },
                index * 100L
            )
        }
    }

    private fun animateView(view: View) {
        val rotation = ObjectAnimator.ofFloat(0f, 90f)
        rotation.duration = 1000
        rotation.interpolator = BounceInterpolator()
        rotation.addUpdateListener {
            val value = it.animatedValue as Float
            view.pivotX = 0f
            view.rotationY = value
        }
        rotation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                // Do nothin
            }

        })
        rotation.start()
    }
}