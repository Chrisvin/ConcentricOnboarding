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

        nextButton.post {
            viewpager.revealCenterPoint =
                PointF(
                    nextButton.x,
                    nextButton.y + nextButton.height / 2
                )
            viewpager.revealRadius = nextButton.width / 2
        }

        nextButton.setOnClickListener {
            viewpager.setCurrentItem(viewpager.currentItem + 1, true)
        }
    }
}
