package com.rdo.octo.motionlayouttest

import android.animation.ObjectAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.BounceInterpolator
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_blob.*
import kotlin.math.abs

class BlobScreenActivity : AppCompatActivity() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private lateinit var drawableBlob: BlobDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blob)
        paint.color = ContextCompat.getColor(this, R.color.colorAccent)
        containerBlob.post {
            drawableBlob = BlobDrawable(paint, containerBlob.width, containerBlob.height)
            drawableBlob.setThumbSize(seekBar2.thumb.intrinsicWidth)
            containerBlob.background = drawableBlob
        }
        seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                drawableBlob.setProgress(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing for now
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val progress = seekBar2.progress
                if (progress > 500) {
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

class BlobDrawable(private var paint: Paint, private val width: Int, private val height: Int) : Drawable() {
    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    private val yPeak = height.toFloat() * 0.75 - 30

    private var progress = 0
    private var offset = 0

    fun setProgress(progress: Int) {
        this.progress = progress
        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        val progressInPx = progress.toFloat() / 1000f * width
        val path = Path()
        path.moveTo(0f, 0f)
        val baseX = maxOf(20f, 2 * progressInPx - width)
        path.lineTo(baseX, 0f)
        for (i in 0.. height) {
            val y = i.toFloat()
            val value = ((progressInPx + offset + 50) *(20f / (20f + ((y / 20f - yPeak /20f) * (y/20f - yPeak/20f))))) + baseX
            path.lineTo(value.toFloat(), y)
        }
        path.lineTo(baseX, height.toFloat())
        path.lineTo(0f, height.toFloat())
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

    fun setThumbSize(width: Int) {
        this.offset = width
    }
}