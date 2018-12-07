package com.rdo.octo.motionlayouttest

import android.animation.ObjectAnimator
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.motion.MotionLayout
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isImageTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        floatingActionButton.setOnClickListener {
            animateButton()
        }
        button.setOnClickListener {
            startActivity(Intent(this, Rotation3DActivity::class.java))
        }
        buttonTime.setOnClickListener {
            startActivity(Intent(this, TimeActivity::class.java))
        }
        motion.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> isImageTouched = true
                ACTION_UP -> isImageTouched = false
            }
            false
        }
        motion.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionChange(p0: MotionLayout, startSet: Int, endSet: Int, progress: Float) {
                if (!isImageTouched) {
                    if (progress < 0.55 && progress > 0.4) {
                        p0.progress = 0.5f
                    }
                }
                when {
                    progress == 0.5f -> {
                        likeImage.visibility = View.GONE
                        dislikeImage.visibility = View.GONE
                    }
                    progress > 0.5 -> {
                        likeImage.visibility = View.GONE
                        dislikeImage.visibility = View.VISIBLE
                        dislikeImage.progress = (progress * 2) - 1
                    }
                    else -> {
                        likeImage.visibility = View.VISIBLE
                        dislikeImage.visibility = View.GONE
                        likeImage.progress = 1 - (progress * 2)
                    }
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout, constraintSet: Int) {
                val text: String = when (constraintSet) {
                    R.id.endLeft -> "Ça te fait kiffer !"
                    R.id.endRight -> "Ça te fait vomir !"
                    else -> "Inconnu"
                }
                //Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
                if (name1.text.toString() == "Paris") {
                    picture1.setImageResource(R.drawable.sanfrancisco)
                    name1.text = "New York"
                    picture1.post {
                        motion.progress = 0.5f
                        name2.text = "Paris"
                        picture2.setImageResource(R.drawable.paris)
                    }
                } else {
                    name1.text = "Paris"
                    picture1.setImageResource(R.drawable.paris)
                    picture1.post {
                        motion.progress = 0.5f
                        name2.text = "New York"
                        picture2.setImageResource(R.drawable.sanfrancisco)
                    }
                }
            }

        })
    }

    private fun animateButton() {
        val down = ObjectAnimator.ofFloat(0f, 100f)
        down.duration = 400
        down.interpolator = BounceInterpolator()
        down.addUpdateListener {
            val value = it.animatedValue as Float
            floatingActionButton.translationY = floatingActionButton.height * (value - 100) / 120
        }


        val up = ObjectAnimator.ofFloat(0f, 100f)
        up.duration = 300
        up.interpolator = DecelerateInterpolator(2f)
        up.addUpdateListener {
            val value = it.animatedValue as Float
            floatingActionButton.translationY = floatingActionButton.height * (-value) / 120
            if (value == 100f) {
                down.start()
            }
        }
        val scale = ObjectAnimator.ofFloat(0f, 50f)
        scale.duration = 400
        scale.interpolator = AccelerateInterpolator()
        scale.addUpdateListener {
            val value = it.animatedValue as Float
            if (value > 25f) {
                up.start()
                floatingActionButton.scaleX = 1 + (0.25f * (50 - value) / 25)
                floatingActionButton.scaleY = 1 - (0.25f * (50 - value) / 25)
            } else {
                floatingActionButton.scaleX = 1 + (0.25f * value / 25)
                floatingActionButton.scaleY = 1 - (0.25f * value / 25)
            }
        }
        scale.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> isImageTouched = true
            ACTION_UP -> isImageTouched = false
        }
        return super.onTouchEvent(event)
    }
}
