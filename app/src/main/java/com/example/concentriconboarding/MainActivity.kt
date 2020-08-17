package com.example.concentriconboarding

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jem.concentriconboarding.ConcentricOnboardingViewPager
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

        modeSpinner.adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                listOf("Slide Mode", "Reveal Mode")
            )
        modeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val textView = (p0?.getChildAt(0) as? TextView?)
                textView?.setTextColor(Color.WHITE)
                viewpager.mode = when (p2) {
                    1 -> ConcentricOnboardingViewPager.Mode.REVEAL
                    else -> ConcentricOnboardingViewPager.Mode.SLIDE
                }
            }
        }

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

        nextButton.post {
            updateViewPagerReveal(
                centerX = nextButton.x + nextButton.width / 2,
                centerY = nextButton.y + nextButton.height / 2,
                radius = nextButton.width / 2
            )
        }
    }

    private fun updateViewPagerReveal(centerX: Float, centerY: Float, radius: Int) {
        viewpager.revealCenterPoint = PointF(centerX, centerY)
        viewpager.revealRadius = radius
    }
}
