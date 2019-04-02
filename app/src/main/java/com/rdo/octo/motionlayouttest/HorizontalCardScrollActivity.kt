package com.rdo.octo.motionlayouttest

import android.animation.Animator
import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import kotlinx.android.synthetic.main.activity_horizontal_scroll.*

class HorizontalCardScrollActivity : AppCompatActivity() {

    val adapter : CardsHorizontalAdapter by lazy {  CardsHorizontalAdapter(cardsContainer, horizontalRecyclerView) }
    var draggingCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll)
        horizontalRecyclerView.viewTreeObserver.addOnScrollChangedListener {
            adapter.animateToScroll()
            updateDraggingEnd()
        }
        adapter.init()
    }

    private fun updateDraggingEnd() {
        val yolo = draggingCount + 1
        draggingCount = yolo
        horizontalRecyclerView.postDelayed({
            if(draggingCount == yolo) {
                draggingCount = 0
                adapter.animateToRest()
            }
        }, 200)
    }

}

class CardsHorizontalAdapter(private val container: LinearLayout, private val scrollView: HorizontalScrollView) {

    var isDragging = false
    var isReseting = false
    var currentAngle = 0f

    private val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11)
    val viewList = mutableListOf<Pair<Int, View>>()

    fun init() {
        list.forEach {
            val view = LayoutInflater.from(container.context).inflate(
                R.layout.cell_card_scroll_horizontal,
                container,
                false
            )
            val resources = view.context.resources
            view.elevation = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8f + (2 * (list.size - it)),
                resources.getDisplayMetrics()
            )
            viewList.add(it to view)
            container.addView(view)
        }
    }

    fun animateToScroll() {
        if (isDragging.not() && isReseting.not()) {
            isDragging = true
            val initView = viewList.map { it.second }.find { it.x >= scrollView.scrollX } ?: viewList[viewList.size - 1].second
            val distance = initView.x - scrollView.scrollX
            val animator = ValueAnimator.ofFloat(currentAngle, 30f)
            animator.duration = 200
            animator.addUpdateListener {
                currentAngle = (it.animatedValue as Float)
                viewList.forEach {
                    it.second.rotationY = currentAngle
                    it.second.scaleX = 1f - (currentAngle / 150f)
                    it.second.scaleY = 1f - (currentAngle / 150f)
                    it.second.translationX = -it.second.width * it.first / 90f * currentAngle
                }
                scrollView.scrollX = (initView.x - distance).toInt()
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    container.postDelayed({
                        isDragging = false
                    }, 50)
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            animator.start()
        }

    }

    fun animateToRest() {
        if (isReseting.not()) {
            isReseting = true
            val animator = ValueAnimator.ofFloat(currentAngle, 0f)
            val initView = viewList.map { it.second }.find { it.x >= scrollView.scrollX } ?: viewList[viewList.size - 1].second
            val distance = initView.x - scrollView.scrollX
            animator.duration = 200
            animator.addUpdateListener {
                currentAngle = (it.animatedValue as Float)
                viewList.forEach {
                    it.second.rotationY = currentAngle
                    it.second.scaleX = 1f - (currentAngle / 150f)
                    it.second.scaleY = 1f - (currentAngle / 150f)
                    it.second.translationX = -it.second.width * it.first / 90f * currentAngle
                }
                scrollView.scrollX = (initView.x - distance).toInt()
            }
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    container.postDelayed({
                        isReseting = false
                    }, 50)
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
            animator.start()
        }
    }
}