package com.example.concentriconboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.jem.concentriconboarding.clippathprovider.ConcentricOnboardingClipPathProvider
import com.jem.concentriconboarding.base.ConcentricOnboardingLayout
import com.jem.concentriconboardingdemo.R

class CustomPagerAdapter(
    private val context: Context,
    private val concentricOnboardingClipPathProviders: Array<ConcentricOnboardingClipPathProvider>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layout = LayoutInflater.from(context).inflate(R.layout.fragment_dummy, container, false)

        layout.setBackgroundColor(backgroundColorArray[(position % titleArray.count())])

        layout.findViewById<LottieAnimationView>(R.id.lottieAnimationView).setAnimation(
            resourceArray[(position % titleArray.count())]
        )
        layout.findViewById<LottieAnimationView>(R.id.lottieAnimationView).repeatCount =
            LottieDrawable.INFINITE
        layout.findViewById<LottieAnimationView>(R.id.lottieAnimationView).repeatMode =
            LottieDrawable.REVERSE
        layout.findViewById<LottieAnimationView>(R.id.lottieAnimationView).playAnimation()

        layout.findViewById<TextView>(R.id.fragment_textview).text =
            titleArray[(position % titleArray.count())]

        (layout as? ConcentricOnboardingLayout)?.clipPathProvider =
            concentricOnboardingClipPathProviders[(position % titleArray.count())]

        container.addView(layout)
        return layout
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return `object` == view
    }

    override fun getCount(): Int {
        return titleArray.count() * 20
    }
}