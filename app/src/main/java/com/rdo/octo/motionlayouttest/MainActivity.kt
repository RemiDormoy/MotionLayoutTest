package com.rdo.octo.motionlayouttest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isImageTouched = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        navigationView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.nav_button -> startActivity(Intent(this, ButtonActivity::class.java))
                R.id.nav_tuto -> startActivity(Intent(this, BlobScreenActivity::class.java))
                R.id.nav_seekbar -> startActivity(Intent(this, SeekActivity::class.java))
                R.id.nav_calendar -> startActivity(Intent(this, CalendarActivity::class.java))
                R.id.nav_rotation -> startActivity(Intent(this, Rotation3DActivity::class.java))
                R.id.nav_zoom -> startActivity(Intent(this, TimeActivity::class.java))
                R.id.nav_garbage -> startActivity(Intent(this, GarbageActivity::class.java))
            }
            true
        }
        motion.setOnTouchListener { v, event ->
            when (event.action) {
                ACTION_DOWN -> isImageTouched = true
                ACTION_UP -> isImageTouched = false
            }
            false
        }
        motion.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
                // Do nothing
            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                // Do nothing
            }

            override fun onTransitionChange(p0: MotionLayout, startSet: Int, endSet: Int, progress: Float) {
                if (!isImageTouched) {
                    if (progress < 0.55 && progress > 0.4) {
                        p0.progress = 0.5f
                    }
                }
                when {
                    progress == 0.5f -> {
                        likeImage.visibility = View.GONE
                        dislikeImage.visibility = View.GONE
                    }
                    progress > 0.5 -> {
                        likeImage.visibility = View.GONE
                        dislikeImage.visibility = View.VISIBLE
                        dislikeImage.progress = (progress * 2) - 1
                    }
                    else -> {
                        likeImage.visibility = View.VISIBLE
                        dislikeImage.visibility = View.GONE
                        likeImage.progress = 1 - (progress * 2)
                    }
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout, constraintSet: Int) {
                val text: String = when (constraintSet) {
                    R.id.endLeft -> "Ça te fait kiffer !"
                    R.id.endRight -> "Ça te fait vomir !"
                    else -> "Inconnu"
                }
                //Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
                if (name1.text.toString() == "Paris") {
                    picture1.setImageResource(R.drawable.sanfrancisco)
                    name1.text = "New York"
                    picture1.post {
                        motion.progress = 0.5f
                        name2.text = "Paris"
                        picture2.setImageResource(R.drawable.paris)
                    }
                } else {
                    name1.text = "Paris"
                    picture1.setImageResource(R.drawable.paris)
                    picture1.post {
                        motion.progress = 0.5f
                        name2.text = "New York"
                        picture2.setImageResource(R.drawable.sanfrancisco)
                    }
                }
            }

        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> isImageTouched = true
            ACTION_UP -> isImageTouched = false
        }
        return super.onTouchEvent(event)
    }
}
