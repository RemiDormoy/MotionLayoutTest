package com.rdo.octo.motionlayouttest

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_cinema.*
import kotlinx.android.synthetic.main.comment_card_travel_content.*
import kotlinx.android.synthetic.main.content_comments_travel.*
import kotlinx.android.synthetic.main.fragment_card_travel.*
import kotlinx.android.synthetic.main.fragment_card_travel.view.*
import kotlinx.android.synthetic.main.travel_activity.*
import kotlin.math.sqrt
import android.util.Pair as UtilPair

class TravelActivity : AppCompatActivity() {

    private val adapter by lazy {
        TravelPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.travel_activity)
        travelViewPager.setPadding(54.toDp(resources).toInt(), 0, 54.toDp(resources).toInt(), 0)
        travelViewPager.clipToPadding = false
        travelViewPager.pageMargin = 0
        travelViewPager.adapter = adapter
        travelViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                (adapter.getItem(position) as TravelPageFragment).setStuff(maxOf(0.9f, 1f - sqrt(sqrt(positionOffset))))
                (adapter.getItem(position - 1) as TravelPageFragment).setStuff(maxOf(0.9f, sqrt(sqrt(positionOffset))))
            }

            override fun onPageSelected(position: Int) {
                (adapter.getItem(position) as TravelPageFragment).setStuff(1f)
                if (position > 0) {
                    (adapter.getItem(position - 1) as TravelPageFragment).setStuff(0.9f)
                }
                if (position < 5) {
                    (adapter.getItem(position + 1) as TravelPageFragment).setStuff(0.9f)
                }
            }

        })
    }

}

class TravelPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragmentMap = mutableMapOf<Int, TravelPageFragment>()

    override fun getItem(position: Int): Fragment {
        return fragmentMap[position] ?: TravelPageFragment(position).also { fragmentMap[position] = it }
    }

    override fun getCount(): Int {
        return 6
    }

}

class TravelPageFragment(private val position: Int) : Fragment() {

    private var isInflated = false
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = inflater.inflate(R.layout.fragment_card_travel, container, false)
        inflate.travelTextView.text = position.toTravelText()
        inflate.travelImageView.setImageResource(position.toTravelRessource())
        return inflate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isInflated = true
        travelImageView.setOnClickListener {
            if (isExpanded) {
                isExpanded = false
                imageTravelCardView.animate().translationY(0f).start()
                commentTravelCardView.animate().scaleX(0.7f).scaleY(0.3f).alpha(0f).start()
            } else {
                isExpanded = true
                imageTravelCardView.animate().translationY((-100).toDp(resources)).start()
                commentTravelCardView.animate().scaleX(1f).scaleY(1f).alpha(1f).start()
            }
        }
        commentsPicturesContainer.setOnClickListener {
            val makeSceneTransitionAnimation = ActivityOptions
                .makeSceneTransitionAnimation(this.requireActivity(),
                    UtilPair.create(travelImageView, "background"),
                    UtilPair.create(ratingBar, "rating"),
                    UtilPair.create(travelTextView, "title"),
                    UtilPair.create(image1ImageView, "image1"),
                    UtilPair.create(image2ImageView, "image2"),
                    UtilPair.create(image3ImageView, "image3"),
                    UtilPair.create(image4ImageView, "image4")
                )
            startActivity(Intent(this@TravelPageFragment.requireContext(), CommentsActivity::class.java).putExtra("position", position), makeSceneTransitionAnimation.toBundle())
        }
    }

    fun setStuff(value: Float) {
        if (isInflated) {
            imageTravelCardView.animate().scaleX(value).scaleY(value).alpha(value * value).start()
            imageTravelCardView.animate().translationY(0f).start()
            commentTravelCardView.animate().scaleX(0.7f).scaleY(0.3f).alpha(0f).start()
        }
    }
}

class CommentsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)
        supportActionBar?.hide()
        val position = intent.getIntExtra("position", 0)
        imageView29.setImageResource(position.toTravelRessource())
        textView31.setText(position.toTravelText())
    }

}

fun Int.toTravelRessource() = when (this) {
    0 -> R.drawable.canyon
    1 -> R.drawable.hawai
    2 -> R.drawable.ski
    3 -> R.drawable.niagara
    4 -> R.drawable.savana
    else -> R.drawable.sanfrancisco
}

fun Int.toTravelText() = when (this) {
    0 -> "Grand canyon"
    1 -> "Hawai"
    2 -> "Ski"
    3 -> "Chutes du Niagara"
    4 -> "Savane en Afrique"
    else -> "San Fransisco"
}
