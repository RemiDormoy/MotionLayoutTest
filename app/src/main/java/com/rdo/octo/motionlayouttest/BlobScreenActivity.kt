package com.rdo.octo.motionlayouttest

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_blob.*

class BlobScreenActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blob)
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Do nothing for now
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing for now
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar2.progress
                if (progress > 800) {
                    val animator = ObjectAnimator.ofInt(progress, 1000)
                    animator.duration = 300
                    animator.interpolator = BounceInterpolator()
                    animator.addUpdateListener {
                        val value = it.animatedValue as Int
                        seekBar2.progress = value
                    }
                    animator.start()
                } else {
                    val animator = ObjectAnimator.ofInt(progress, 0)
                    animator.duration = 300
                    animator.interpolator = BounceInterpolator()
                    animator.addUpdateListener {
                        val value = it.animatedValue as Int
                        seekBar2.progress = value
                    }
                    animator.start()
                }
            }

        })
    }
}