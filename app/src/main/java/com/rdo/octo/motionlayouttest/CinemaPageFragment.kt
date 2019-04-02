package com.rdo.octo.motionlayouttest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_card_cinema.view.*

class CinemaPageFragment(private val position: Int) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = inflater.inflate(R.layout.fragment_card_cinema, container, false)
        inflate.cinemaImageView.setImageResource(position.toRessource())
        return inflate
    }
}

fun Int.toRessource() = when (this) {
    0 -> R.drawable.bond
    1 -> R.drawable.ring
    2 -> R.drawable.hp
    3 -> R.drawable.guardians
    4 -> R.drawable.lego
    5 -> R.drawable.starwars
    else -> R.drawable.goal
}

