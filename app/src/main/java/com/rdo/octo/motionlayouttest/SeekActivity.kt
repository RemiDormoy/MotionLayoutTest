package com.rdo.octo.motionlayouttest

import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_seek.*

class SeekActivity : AppCompatActivity() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private lateinit var drawable: SeekDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seek)
        paint.color = ContextCompat.getColor(this, R.color.colorPrimary)
        drawView.post {
            drawable = SeekDrawable(paint, drawView.width, drawView.height)
            drawView.background = drawable
        }
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawable.setProgress(progress)
                val weight = 50 + progress / 100
                weightTextView.text = "$weight kg"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val animator = ObjectAnimator.ofInt(0, drawView.height - 20)
                animator.duration = 300
                animator.interpolator = AccelerateDecelerateInterpolator()
                animator.addUpdateListener {
                    val value = it.animatedValue as Int
                    drawable.setMaxHeight(value)
                }
                animator.start()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val animator = ObjectAnimator.ofInt(drawView.height - 20, 0)
                animator.duration = 500
                animator.interpolator = BounceInterpolator()
                animator.addUpdateListener {
                    val value = it.animatedValue as Int
                    drawable.setMaxHeight(value)
                }
                animator.start()
            }

        })
    }
}

class SeekDrawable(private var paint: Paint, private val width: Int, private val height: Int) : Drawable() {

    private var progress = 5000
    private var maxHeight = 0

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    fun setMaxHeight(maxHeight: Int) {
        this.maxHeight = maxHeight
        callback?.invalidateDrawable(this)
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        val xPeak = (progress * width / 10000).toFloat()
        val path = Path()
        path.moveTo(0f, maxHeight.toFloat())
        for (i in 0..width) {
            val x = i.toFloat()
            val value = 20f / (20f + ((x / 20f - xPeak /20f) * (x/20f - xPeak/20f)))
            val heightWithOffset = value * maxHeight + 5f
            val y = maxHeight - heightWithOffset + 10
            path.lineTo(i.toFloat(), y)
        }
        for (i in width downTo 0) {
            val x = i.toFloat()
            val value = 20f / (20f + ((x / 20f - xPeak /20f) * (x/20f - xPeak/20f)))
            val heightWithOffset = value * maxHeight - 5f
            val y = maxHeight - heightWithOffset + 10
            path.lineTo(i.toFloat(), y)
        }
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    fun setPaint(paint: Paint) {
        this.paint = paint
    }
}