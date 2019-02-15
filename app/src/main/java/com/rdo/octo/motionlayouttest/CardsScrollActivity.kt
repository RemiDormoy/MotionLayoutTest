package com.rdo.octo.motionlayouttest

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.NestedScrollingChildHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_cards.*

class CardsScrollActivity : AppCompatActivity() {

    val cardsAdapter = CardsAdapter()
    private var translation = 0f
    private var maxTranslation: Float = 0f
    private var loadingTranslation: Float = 0f
    private var randomYolo = 0
    private var isInAnim = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = cardsAdapter
        cardsAdapter.notifyDataSetChanged()
        yoloViewGroup.post {
            maxTranslation = yoloViewGroup.height / 4f
            loadingTranslation = yoloViewGroup.height / 5f
            listOf(progressBar, textView28).forEach { it.translationY = -loadingTranslation }
        }
        yoloViewGroup.callback = {
            processTranslation(it)
            randomYolo++
            val yoloyolo = randomYolo
            yoloViewGroup.postDelayed({
                if (randomYolo == yoloyolo) {
                    animateToBottom()
                }
            }, 100)
        }
    }

    private fun processTranslation(it: Int) {
        translation = minOf(cardsRecyclerView.translationY - it, maxTranslation)
        textViewToto.text = it.toString()
        val yolo = translation
        zob(yolo)
    }

    private fun zob(yolo: Float) {
        cardsRecyclerView.translationY = yolo
        listOf(progressBar, textView28).forEach { it.translationY = yolo - loadingTranslation }
        cardsAdapter.viewList.forEach {
            it.pivotY = 0f
            it.rotationX = 20f * (yolo / maxTranslation)
            it.scaleX = 1f - (yolo / yoloViewGroup.height)
            it.scaleY = 1f - (yolo / yoloViewGroup.height)
        }
    }

    private fun animateToBottom() {
        if (isInAnim) {
            return
        }
        isInAnim = true
        if (translation > loadingTranslation) {
            val animator = ValueAnimator.ofFloat(translation, loadingTranslation)
            animator.duration = 500
            animator.addUpdateListener {
                zob(it.animatedValue as Float)
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    yoloViewGroup.postDelayed({
                        backToNormal()
                    }, 2000)
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            animator.interpolator = DecelerateInterpolator()
            animator.start()
        } else {
            backToNormal()
        }
    }

    private fun backToNormal() {
        val animator2 = ValueAnimator.ofFloat(translation, 0f)
        animator2.duration = 500
        animator2.addUpdateListener {
            zob(it.animatedValue as Float)
        }
        animator2.interpolator = BounceInterpolator()
        animator2.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                isInAnim = false
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }
        })
        animator2.start()
    }
}

class YoloViewGroup @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    style: Int = 0,
    yolo: Int = 0
) : FrameLayout(context, attr, style, yolo) {

    private var scroll: Int = 0
    var callback: (Int) -> Unit = { /*Do nothing*/ }
    private val childHelper = NestedScrollingChildHelper(this)

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
        scroll = dyUnconsumed
        callback(scroll)
    }

    override fun onNestedScrollAccepted(child: View?, target: View?, axes: Int) {
        childHelper.startNestedScroll(axes)
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        return true
    }
}

class YoloRecyclerView @JvmOverloads constructor(context: Context, attr: AttributeSet? = null, style: Int = 0) :
    RecyclerView(context, attr, style) {

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        Log.d("onNestedScroll", "xUnconsumed : $dxUnconsumed || yUnconsumed : $dyUnconsumed")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onOverScrolled(scrollX: Int, scrollY: Int, clampedX: Boolean, clampedY: Boolean) {
        Log.d("onNestedScroll", "xUnconsumed : $scrollY || yUnconsumed : $clampedY")
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        Log.d("onNestedScroll", "l : $l || t : $t || oldl : $oldl || oldt : $oldt")
        super.onScrollChanged(l, t, oldl, oldt)
    }

}

class CardsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    val viewList = mutableListOf<View>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.cell_card_scroll,
            parent,
            false
        )
        viewList.add(view)
        return object : RecyclerView.ViewHolder(
            view
        ) {}
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // Do nothing
    }
}