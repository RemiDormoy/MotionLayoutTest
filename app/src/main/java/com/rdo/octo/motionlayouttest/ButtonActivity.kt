package com.rdo.octo.motionlayouttest

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_button.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class ButtonActivity : AppCompatActivity() {

    lateinit var drawable: MyDrawable
    lateinit var lavaDrawable: LavaDrawable

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private val paintLava = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)
        floatingActionButton.setOnClickListener {
            animateButton()
        }
        paint.color = ContextCompat.getColor(this, R.color.colorPrimary)
        bottomView.post {
            drawable = MyDrawable(paint, floatingActionButton.height).apply {
                width = bottomView.width
                height = bottomView.height
            }
            bottomView.setImageDrawable(drawable)
        }
        testLavaImageView.post {
            lavaDrawable = LavaDrawable(paintLava, testLavaImageView.width, testLavaImageView.height)
            testLavaImageView.setImageDrawable(lavaDrawable)
        }
    }

    private fun animateButton() {
        val down = getDownAnimation()
        val up = getUpAnimation(down)
        val scale = getScaleAnimation(up)
        scale.start()
    }

    private fun getScaleAnimation(up: ValueAnimator): ValueAnimator {
        val scale = ObjectAnimator.ofFloat(0f, 50f)
        scale.duration = 400
        scale.interpolator = AccelerateInterpolator()
        scale.addUpdateListener {
            val value = it.animatedValue as Float
            if (value > 25f) {
                up.start()
                val x = 1 + (0.25f * (50 - value) / 25)
                val y = 1 - (0.25f * (50 - value) / 25)
                floatingActionButton.scaleX = x
                floatingActionButton.scaleY = y
                drawable.buttonHeight = drawable.buttonHeightInit * y
                drawable.buttonWidth = drawable.buttonHeightInit * x
            } else {
                val x = 1 + (0.25f * value / 25)
                val y = 1 - (0.25f * value / 25)
                floatingActionButton.scaleX = x
                floatingActionButton.scaleY = y
                drawable.buttonHeight = drawable.buttonHeightInit * y
                drawable.buttonWidth = drawable.buttonHeightInit * x
                drawable.invalidateMaman()
            }
        }
        return scale
    }

    private fun getUpAnimation(down: ValueAnimator): ValueAnimator {
        val up = ObjectAnimator.ofFloat(0f, 100f)
        up.duration = 300
        up.interpolator = DecelerateInterpolator(2f)
        up.addUpdateListener {
            val value = it.animatedValue as Float
            floatingActionButton.translationY = floatingActionButton.height * (-value) / 120
            drawable.setTranslation(floatingActionButton.height * (-value) / 120)
            if (value == 100f) {
                down.start()
            }
        }
        return up
    }

    private fun getDownAnimation(): ValueAnimator {
        val down = ObjectAnimator.ofFloat(0f, 100f)
        down.duration = 400
        down.interpolator = BounceInterpolator()
        down.addUpdateListener {
            val value = it.animatedValue as Float
            floatingActionButton.translationY = floatingActionButton.height * (value - 100) / 120
            drawable.setTranslation(floatingActionButton.height * (value - 100) / 120)
        }
        return down
    }
}

class MyDrawable(private val paint: Paint, val buttonHeightInit: Int) : Drawable() {

    private var translation: Float = 0f
    var width = 1000
    var height = 200
    var buttonHeight = buttonHeightInit.toFloat()
    var buttonWidth = buttonHeightInit.toFloat()

    fun invalidateMaman() {
        callback?.invalidateDrawable(this)
    }

    fun setTranslation(translation: Float) {
        this.translation = translation
        callback?.invalidateDrawable(this)
    }

    override fun draw(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, height.toFloat())
        path.lineTo(0f, 0f)
        path.lineTo((width / 2f) - (buttonWidth / 2) - 30, 0f)
        //path.arcTo((width / 2f) - (buttonHeight / 2) - 20, 0f,(width / 2f) + (buttonHeight / 2) + 20,  buttonHeight + 20f, 180f, 360f, false)
        val x1 = (width / 2f) - (buttonWidth / 2) - 30
        val x2 = (width / 2f) + (buttonWidth / 2) + 30
        for (i in 0..314) {
            val progress = (cos((i + 314) / 100f) + 1f) / 2f
            path.lineTo(
                (x2 * progress) + ((1 - progress) * x1),
                maxOf(sin(i.toFloat() / 100f) * (((buttonHeight / 2) + 30f) + translation), 0f)
            )
        }
        path.lineTo((width / 2f) + (buttonWidth / 2) + 30, 0f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat(), height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getIntrinsicWidth() = width

    override fun getIntrinsicHeight() = height

    override fun getOpacity(): Int {
        return PixelFormat.OPAQUE
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

}