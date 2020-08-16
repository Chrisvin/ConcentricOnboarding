package com.example.concentriconboarding

import android.graphics.PointF
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jem.concentriconboardingdemo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager.adapter = CustomFragmentPagerAdapter(supportFragmentManager)

        // Create 20 times the number of actual pages, and start in the middle.
        // This way users can swipe left or right from the start.
        // Definitely not a good idea for production, but good enough for a demo app.
        viewpager.setCurrentItem(titleArray.count() * 10, false)

        previousButton.setOnClickListener {
            updateViewPagerReveal(
                centerX = previousButton.x + previousButton.width / 2,
                centerY = previousButton.y + previousButton.height / 2,
                radius = previousButton.width / 2
            )
            viewpager.setCurrentItem(viewpager.currentItem - 1, true)
        }

        nextButton.setOnClickListener {
            updateViewPagerReveal(
                centerX = nextButton.x + nextButton.width / 2,
                centerY = nextButton.y + nextButton.height / 2,
                radius = nextButton.width / 2
            )
            viewpager.setCurrentItem(viewpager.currentItem + 1, true)
        }
    }

    private fun updateViewPagerReveal(centerX: Float, centerY: Float, radius: Int) {
        viewpager.revealCenterPoint = PointF(centerX, centerY)
        viewpager.revealRadius = radius
    }
}
