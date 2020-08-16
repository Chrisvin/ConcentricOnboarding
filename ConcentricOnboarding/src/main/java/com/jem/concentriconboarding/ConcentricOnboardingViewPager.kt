package com.jem.concentriconboarding

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.viewpager.widget.ViewPager
import com.jem.concentriconboarding.ConcentricOnboardingViewPager.ConcentricOnboardingPageTransformer
import com.jem.concentriconboarding.base.ConcentricOnboardingLayout
import com.jem.concentriconboarding.util.FixedSpeedScroller
import kotlin.math.abs

/**
 * `ConcentricOnboardingViewPager` is a custom [ViewPager] that uses a fixed scroller and uses a [ConcentricOnboardingPageTransformer] as it's page transformer.
 *
 * Note: Setting another page transformer to [ConcentricOnboardingViewPager] would remove the [ConcentricOnboardingPageTransformer].
 */
class ConcentricOnboardingViewPager : ViewPager {
    constructor(context: Context) : super(context) {
        initialize(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initialize(context, attrs)
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
        setPageTransformer(true, ConcentricOnboardingPageTransformer())

        var scrollerDuration = DEFAULT_SCROLLER_DURATION
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.ConcentricOnboardingViewPager, 0, 0)
            typedArray.apply {
                scrollerDuration = getInt(
                    R.styleable.ConcentricOnboardingViewPager_scrollerDuration,
                    DEFAULT_SCROLLER_DURATION
                )
            }
        }
        setDuration(scrollerDuration)
    }

    /**
     * Sets the fixed scroller duration for the [ConcentricOnboardingViewPager].
     * @param duration Duration taken for viewpager to settle into position.
     */
    public fun setDuration(duration: Int) {
        val mScroller = ViewPager::class.java.getDeclaredField("mScroller")
        mScroller.isAccessible = true
        val scroller = FixedSpeedScroller(context, DecelerateInterpolator())
        scroller.scrollerDuration = duration
        mScroller.set(this, scroller)
    }

    companion object Constants {
        private const val DEFAULT_SCROLLER_DURATION = 1000
    }

    /**
     * `ConcentricOnboardingPageTransformer` is a custom [ViewPager.PageTransformer] that is used for ConcentricOnboarding reveal.
     */
    class ConcentricOnboardingPageTransformer : ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (page is ConcentricOnboardingLayout) {
                when {
                    position < -1 -> {
                        page.revealForPercentage(0f)
                    }
                    position < 0 -> {
                        page.translationX = -(page.width * position)
                        page.canvasTranslateX = (page.width * position) * 2
                        page.revealForPercentage(100 - abs(position * 100))
                    }
                    position <= 1 -> {
                        page.translationX = -(page.width * position)
                        page.canvasTranslateX = (page.width * position) * 2
                        page.revealForPercentage(100f)
                    }
                }
            }
        }
    }
}