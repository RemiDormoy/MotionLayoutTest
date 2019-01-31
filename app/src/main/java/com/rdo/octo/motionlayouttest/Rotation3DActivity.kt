package com.rdo.octo.motionlayouttest

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.animation.ObjectAnimator
import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import kotlinx.android.synthetic.main.rotation_activity.*


class Rotation3DActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rotation_activity)
        image2.setOnClickListener {
            animateView()
        }
        image1.setOnClickListener {
            animateView1()
        }
        image3.setOnClickListener {
            animateView3()
        }
        image4.setOnClickListener {
            animateView4()
        }
    }

    private fun animateView() {
        val rotation = ObjectAnimator.ofFloat(0f, 90f)
        rotation.duration = 1000
        rotation.interpolator = BounceInterpolator()
        rotation.addUpdateListener {
            val value = it.animatedValue as Float
            image2.pivotX = 0f
            image2.rotationY = value
        }
        rotation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                image2.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                image2.cardElevation = 0f
            }

        })
        rotation.start()
    }


    private fun animateView4() {
        val rotation = ObjectAnimator.ofFloat(0f, 90f)
        rotation.duration = 1000
        rotation.interpolator = BounceInterpolator()
        rotation.addUpdateListener {
            val value = it.animatedValue as Float
            image4.pivotX = image4.width.toFloat()
            image4.rotationY = -value
        }
        rotation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                image4.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                image4.cardElevation = 0f
            }

        })
        rotation.start()
    }

    private fun animateView1() {
        val rotation = ObjectAnimator.ofFloat(0f, 90f)
        rotation.duration = 1000
        rotation.interpolator = BounceInterpolator()
        rotation.addUpdateListener {
            val value = it.animatedValue as Float
            image1.pivotY = image1.height.toFloat()
            image1.rotationX = value
        }

        rotation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                image1.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                image1.cardElevation = 0f
            }

        })
        rotation.start()
    }


    private fun animateView3() {
        val rotation = ObjectAnimator.ofFloat(0f, 90f)
        rotation.duration = 1000
        rotation.interpolator = BounceInterpolator()
        rotation.addUpdateListener {
            val value = it.animatedValue as Float
            image3.pivotY = 0f
            image3.rotationX = -value
        }

        rotation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationEnd(animation: Animator?) {
                image3.visibility = View.GONE
            }

            override fun onAnimationCancel(animation: Animator?) {
                // Do nothing
            }

            override fun onAnimationStart(animation: Animator?) {
                image3.cardElevation = 0f
            }

        })
        rotation.start()
    }
}