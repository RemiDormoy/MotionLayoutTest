package com.rdo.octo.motionlayouttest

import android.animation.ValueAnimator
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_cinema.*
import kotlinx.android.synthetic.main.ticket_layout.*
import kotlin.math.abs

class CinemaActivity : AppCompatActivity() {


    private var currentPosition = 0
    private var currentAlt = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema)
        supportActionBar?.hide()
        cinemaViewPager.setPadding(54.toDp(resources).toInt(), 0, 54.toDp(resources).toInt(), 0)
        cinemaViewPager.clipToPadding = false
        cinemaViewPager.pageMargin = 50
        cinemaViewPager.adapter = CinemaViewPagerAdapter(supportFragmentManager)
        cinemaViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val yolo = position + 1
                if (yolo != currentAlt) {
                    currentAlt = yolo
                    backgroundFilterView.setImageResource(position.toBackgroundRessource())
                    backgroundAltImageView.setImageResource(yolo.toBackgroundRessource())
                }
                backgroundAltImageView.alpha = positionOffset
                backgroundFilterView.alpha = 1 - abs(positionOffset)
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                backgroundFilterView.setImageResource(position.toBackgroundRessource())
                backgroundAltImageView.alpha = 0f
                backgroundFilterView.alpha = 1f
                contentCinemaTextView.animate().alpha(0f).withEndAction {
                    cinemaTitleTextView.text = position.getTitle()
                    cinemaSummaryTextView.text = position.getContent()
                    contentCinemaTextView.animate().alpha(1f).start()
                }.start()
            }

        })
        cinemaViewPager.currentItem = 3
        choseTimeButton.setOnClickListener {
            moveToTime()
        }
        listOf(buttonTime8, buttonTime10, buttonTime9).forEach {
            it.setOnClickListener {
                moveToSeats()
            }
        }
        listOf(
            seat1, seat2, seat3, seat4, seat5, seat6, seat7, seat8, seat9, seat10,
            seat11, seat12, seat13, seat14, seat15, seat16, seat17, seat18, seat19,
            seat21, seat22, seat23, seat24, seat25
        ).forEach {
            it.setOnClickListener {
                it.setBackgroundColor(ContextCompat.getColor(this@CinemaActivity, R.color.colorAccent))
            }
        }
        payButtonTextView.setOnClickListener {
            moveToTicket()
        }
        finishCinemaButton.setOnClickListener {
            this@CinemaActivity.finish()
        }
    }

    private fun moveToTicket() {
        blackLine.visibility = VISIBLE
        blackLine.translationY = fakeCardView.translationY
        val ofFloat = ValueAnimator.ofFloat(50f * 0.85f, 90f)
        ofFloat.addUpdateListener {
            val value = it.animatedValue as Float
            fakeCardView.rotationX = -value
        }
        ofFloat.duration = 500
        ofFloat.start()
        blackLine.animate().alpha(1f).withEndAction {
            Handler().postDelayed({ launchTicket() }, 500)
        }.start()
        payButton.animate().alpha(0f).withEndAction { payButton.visibility = GONE }.start()
        placesCardView.animate().alpha(0f).withEndAction { placesCardView.visibility = GONE }.start()
        leftLettersContainer.animate().alpha(0f).withEndAction { leftLettersContainer.visibility = GONE }.start()
        rightLettersContainer.animate().alpha(0f).withEndAction { rightLettersContainer.visibility = GONE }.start()
        bottomNumbersContainer.animate().alpha(0f).withEndAction { bottomNumbersContainer.visibility = GONE }.start()

    }

    private fun launchTicket() {
        ticketContainer.translationY = fakeCardView.translationY
        ticketDouble.translationY = -ticketContainer.height.toFloat()
        ticketContainer.visibility = VISIBLE
        yourticketTextView.visibility = VISIBLE
        yourticketTextView.animate().alpha(1f).start()
        ticketDouble.animate().translationY(0f).setDuration(1000).setInterpolator(DecelerateInterpolator()).start()
    }

    private fun moveToSeats() {
        placesCardView.visibility = VISIBLE
        fakeCardView.elevation = 0f
        fakeCardView.pivotY = 0f
        payButton.visibility = VISIBLE
        leftLettersContainer.visibility = VISIBLE
        rightLettersContainer.visibility = VISIBLE
        bottomNumbersContainer.visibility = VISIBLE
        val ofFloat = ValueAnimator.ofFloat(0f, 50f)
        ofFloat.addUpdateListener {
            val value = it.animatedValue as Float

            firstCardViewCinema.scaleX = (100f - (value / 3f)) / 100f
            firstCardViewCinema.scaleY = (100f - (value / 1.5f)) / 100f
            firstCardViewCinema.translationY = fakeCardView.height * value / -150f
            firstCardViewCinema.alpha = 1 - (value / 20f)

            placesCardView.scaleX = (100f - (value / 3f)) / 100f
            placesCardView.scaleY = (100f - (value / 1.5f)) / 100f
            placesCardView.translationY = fakeCardView.height * value / -150f
            placesCardView.alpha = value / 50f

            leftLettersContainer.alpha = value / 50f
            leftLettersContainer.scaleY = (100f - (value / 1.5f)) / 100f
            leftLettersContainer.translationY = fakeCardView.height * value / -150f

            rightLettersContainer.scaleY = (100f - (value / 1.5f)) / 100f
            rightLettersContainer.alpha = value / 50f
            rightLettersContainer.translationY = fakeCardView.height * value / -150f

            bottomNumbersContainer.scaleX = (100f - (value / 3f)) / 100f
            bottomNumbersContainer.alpha = value / 50f
            bottomNumbersContainer.translationY = fakeCardView.height * value / -100f

            payButton.alpha = value / 50f

            fakeCardView.rotationX = -value * 0.85f
            fakeCardView.translationY = fakeCardView.height * value / 150f
        }
        Handler().postDelayed({
            firstCardViewCinema.visibility = GONE
        }, 1000)
        ofFloat.duration = 1000
        ofFloat.start()
    }

    private fun moveToTime() {
        fakeImageView.setImageResource(cinemaViewPager.currentItem.toRessource())
        fakeCardView.visibility = VISIBLE
        chooseTimeContainer.visibility = VISIBLE
        cinemaViewPager.animate().alpha(0f).withEndAction { cinemaViewPager.visibility = GONE }.start()
        contentCinemaTextView.animate().alpha(0f).withEndAction { contentCinemaTextView.visibility = GONE }.start()
        chooseTimeContainer.animate().alpha(1f).start()
    }

}

private fun Int.getTitle() = when (this) {
    0 -> "James Bond"
    1 -> "Le seigneur des anneaux"
    2 -> "Harry Potter"
    3 -> "Les gardiens de la galaxie"
    4 -> "Lego movie"
    5 -> "Star Wars"
    else -> "Goal"
}

private fun Int.getContent() = when (this) {
    0 -> "C'est l'histoire d'un agent secret ultra stylé qui flingue plein de gens et fini toujours par se faire faire prisonier puis par flinguer tout le monde."
    1 -> "7 mecs qui partent en vacances dans une région pas très hospitalière pour détruire une bague qui appartient à un mec pas très gentil"
    2 -> "Un mec qui a pris un sacré pain dans la tronche et en a récolté une cicatrice en forme d'éclair qui lui provoque de sacrées hallucinations"
    3 -> "Un raton laveur, un imbécile, une meuf verte et un gros tocard qui croient vraiment qu'ils arrivent à parler à un arbre."
    4 -> "Le meilleur film de tous les temps qui fait bouger des légo de partout"
    5 -> "Un film très très bien qui permet de se rendre compte que les gens ne manquent jamais d'imagination pour trouver des races aliens"
    else -> "L'histoire d'un mec qui est super fort au foot, joue à NewCastle puis au Réal de madrid et serre la meuf de Beckham"
}

fun Int.toDp(resources: Resources) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    resources.getDisplayMetrics()
)
