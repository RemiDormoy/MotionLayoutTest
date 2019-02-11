package com.rdo.octo.motionlayouttest

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_button.*
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.android.synthetic.main.cell_card_bubble.view.*


class ButtonActivity : AppCompatActivity() {

    lateinit var drawable: MyDrawable
    lateinit var lavaDrawable: LavaDrawable
    lateinit var lavaDrawableTop: LavaDrawable

    private val paint = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private val paintLavaTop = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    private val paintLavaBottom = Paint(ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = 4f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_button)
        floatingActionButton.setOnClickListener {
            animateButton()
        }
        val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val color2 = ContextCompat.getColor(this, R.color.color2)
        val colorMiddle = ContextCompat.getColor(this, R.color.colorMiddle)
        paint.color = primaryColor
        paintLavaBottom.color = color2
        paintLavaTop.color = ContextCompat.getColor(this, R.color.colorPrimary)
        bottomView.post {
            drawable = MyDrawable(paint, floatingActionButton.height).apply {
                width = bottomView.width
                height = bottomView.height
            }
            bottomView.setImageDrawable(drawable)
        }
        testLavaImageView.post {
            lavaDrawableTop = LavaDrawable(paintLavaTop, testLavaImageView.width, testLavaImageView.height)
            setLevel1paints()
            imageView26.setBackground(lavaDrawableTop)
            lavaDrawable = LavaDrawable(paintLavaBottom, testLavaImageView.width, testLavaImageView.height)
            testLavaImageView.setBackground(lavaDrawable)
        }

        container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                // Do nothing
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                // Do nothing
            }

            override fun onTransitionChange(p0: MotionLayout, startSet: Int, endSet: Int, progress: Float) {
                val value = testLavaImageView.height * progress * 1f
                lavaDrawable.setDistance(value.toInt())
                lavaDrawableTop.setDistance(-value.toInt())
                testLavaImageView.translationY = minOf(testLavaImageView.height.toFloat(), value) - 10
                if (progress > 0.95) {
                    isEnough = true
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout, constraintSet: Int) {
                if (!pending && isEnough) {
                    pending = true
                    testLavaImageView.translationX = 0f
                    testLavaImageView.translationY = 0f
                    container.progress = 0f
                    lavaDrawable.setDistance(0)
                    lavaDrawableTop.setDistance(0)
                    addOneCard()
                }
            }

        })
    }

    private fun setLevel1paints() {
        val primaryColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val color2 = ContextCompat.getColor(this, R.color.color2)
        val colorMiddle = ContextCompat.getColor(this, R.color.colorMiddle)
        paintLavaTop.shader = LinearGradient(
            0f,
            0f,
            0f,
            testLavaImageView.height.toFloat() * 1f,
            primaryColor,
            colorMiddle,
            Shader.TileMode.REPEAT
        )
        paintLavaBottom.shader = LinearGradient(
            0f,
            0f,
            0f,
            testLavaImageView.height * 1f,
            colorMiddle,
            color2,
            Shader.TileMode.REPEAT
        )
    }

    private fun setLevel2paints() {
        val primaryColor = ContextCompat.getColor(this, R.color.color2)
        val color2 = ContextCompat.getColor(this, R.color.color3)
        val colorMiddle = ContextCompat.getColor(this, R.color.colorMiddle2)
        paintLavaTop.shader = LinearGradient(
            0f,
            0f,
            0f,
            testLavaImageView.height.toFloat() * 1f,
            primaryColor,
            colorMiddle,
            Shader.TileMode.REPEAT
        )
        paintLavaBottom.shader = LinearGradient(
            0f,
            0f,
            0f,
            testLavaImageView.height * 1f,
            colorMiddle,
            color2,
            Shader.TileMode.REPEAT
        )
    }



    private fun setLevel3paints() {
        val primaryColor = ContextCompat.getColor(this, R.color.color3)
        val color2 = ContextCompat.getColor(this, R.color.color3)
        val colorMiddle = ContextCompat.getColor(this, R.color.color3)
        paintLavaTop.shader = LinearGradient(
            0f,
            0f,
            0f,
            testLavaImageView.height.toFloat() * 1f,
            primaryColor,
            colorMiddle,
            Shader.TileMode.REPEAT
        )
        paintLavaBottom.shader = LinearGradient(
            0f,
            0f,
            0f,
            testLavaImageView.height * 1f,
            colorMiddle,
            color2,
            Shader.TileMode.REPEAT
        )
    }

    private fun animateButton() {
        val down = getDownAnimation()
        val up = getUpAnimation(down)
        val scale = getScaleAnimation(up)
        scale.start()
    }

    private var cardsLoaded = 0
    private var pending = false
    private var isEnough = false

    private fun onCardDeleted() {
        if (cardsLoaded > 2) {
            testLavaImageView.background = lavaDrawable
            imageView26.background = lavaDrawableTop
        }
        cardsLoaded = cardsLoaded - 1
        setPaints()
        lavaDrawable.invalidate()
        lavaDrawableTop.invalidate()
    }

    private fun getColorForFrame() = when (cardsLoaded) {
        1 -> ContextCompat.getColor(this, R.color.colorPrimary)
        2 -> ContextCompat.getColor(this, R.color.color2)
        else -> ContextCompat.getColor(this, R.color.color3)
    }

    private fun addOneCard() {
        cardsLoaded += 1
        if (cardsLoaded > 3) {
            // Do nothing
        } else {
            val view = LayoutInflater.from(this).inflate(R.layout.cell_card_bubble, cardsContainer, false)
            cardsContainer.addView(view)
            view.colorFrame.setBackgroundColor(getColorForFrame())
            view.closeImage.setOnClickListener {
                cardsContainer.removeView(view)
                onCardDeleted()
            }
            if (cardsLoaded > 2) {
                cardsContainer.post {
                    testLavaImageView.background = null
                    imageView26.background = null
                }
            }
            setPaints()
        }
        pending = false
        isEnough = false
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

    private fun setPaints() {
        when (cardsLoaded) {
            0 -> setLevel1paints()
            1 -> setLevel2paints()
            2 -> setLevel3paints()
        }
        lavaDrawableTop.setPaint(paintLavaTop)
        lavaDrawable.setPaint(paintLavaBottom)
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
        for (i in 314..628) {
            val angle = i.toFloat() / 100f
            val radiusX = (buttonWidth / 2f) + 30
            val radiusY = (buttonHeight / 2f) + 30
            path.lineTo((width / 2f) + (cos(angle) * radiusX), (-sin(angle) * radiusY) + translation)
        }
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