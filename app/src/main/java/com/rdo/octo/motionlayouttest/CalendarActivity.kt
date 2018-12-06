package com.rdo.octo.motionlayouttest

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.View.VISIBLE
import android.view.animation.BounceInterpolator
import kotlinx.android.synthetic.main.activity_calendar.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val imageViews = arrayOf(
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
        )
        val drawables = arrayOf(
            R.drawable.p1,
            R.drawable.p2,
            R.drawable.p3,
            R.drawable.p4,
            R.drawable.p5,
            R.drawable.p6,
            R.drawable.p7,
            R.drawable.p8,
            R.drawable.p9,
            R.drawable.p10,
            R.drawable.p11,
            R.drawable.p12,
            R.drawable.p13,
            R.drawable.p14,
            R.drawable.p15,
            R.drawable.p16,
            R.drawable.p17,
            R.drawable.p18,
            R.drawable.p19,
            R.drawable.p20,
            R.drawable.p21,
            R.drawable.p22,
            R.drawable.p23,
            R.drawable.p24
        )
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
        ).forEachIndexed { index, textView ->
            textView.setOnClickListener {
                val image = imageViews[index]
                val bitmap = resize(BitmapFactory.decodeResource(resources,drawables[index]), image.width, image.height)
                image.setImageBitmap(bitmap)
                animateView(textView)
            }
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

    private fun resize(image: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var image = image
        if (maxHeight > 0 && maxWidth > 0) {
            val width = image.width
            val height = image.height
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

            var finalWidth = maxWidth
            var finalHeight = maxHeight
            if (ratioMax > ratioBitmap) {
                finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
            } else {
                finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true)
            return image
        } else {
            return image
        }
    }
}