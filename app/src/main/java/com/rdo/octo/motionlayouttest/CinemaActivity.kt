package com.rdo.octo.motionlayouttest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_cinema.*

class CinemaActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema)
        cinemaViewPager.setPadding(150, 0, 150, 50)
        cinemaViewPager.clipToPadding = false
        cinemaViewPager.pageMargin = 50
        cinemaViewPager.adapter = CinemaViewPagerAdapter(supportFragmentManager)
        cinemaViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                contentCinemaTextView.animate().alpha(0f).withEndAction {
                    cinemaTitleTextView.text = position.getTitle()
                    cinemaSummaryTextView.text = position.getContent()
                    contentCinemaTextView.animate().alpha(1f).start()
                }.start()
            }

        })
        cinemaViewPager.currentItem = 3
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
