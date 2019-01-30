package com.rdo.octo.motionlayouttest

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
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
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // Do nothing
            }

        })
    }
}

class SeekDrawable(private var paint: Paint, private val width: Int, private val height: Int) : Drawable() {

    private var progress = 5000

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        val xPeak = (progress * width / 10000).toFloat()
        val path = Path()
        path.moveTo(0f, height.toFloat())
        for (i in 0..width) {
            val x = i.toFloat()
            val value = 20f / (20f + ((x / 20f - xPeak /20f) * (x/20f - xPeak/20f)))
            val heightWithOffset = value * height + 5f
            val y = height - minOf(heightWithOffset, height.toFloat())
            path.lineTo(i.toFloat(), y)
        }
        for (i in width downTo 0) {
            val x = i.toFloat()
            val value = 20f / (20f + ((x / 20f - xPeak /20f) * (x/20f - xPeak/20f)))
            val heightWithOffset = value * height - 5f
            val y = height - maxOf(minOf(heightWithOffset, height.toFloat() + 10f), 0f)
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