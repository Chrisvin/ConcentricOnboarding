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
        var mode = DEFAULT_MODE
        var scrollerDuration = DEFAULT_SCROLLER_DURATION
        attrs?.let {
            val typedArray =
                context.obtainStyledAttributes(it, R.styleable.ConcentricOnboardingViewPager, 0, 0)
            typedArray.apply {
                scrollerDuration = getInt(
                    R.styleable.ConcentricOnboardingViewPager_scrollerDuration,
                    DEFAULT_SCROLLER_DURATION
                )
                val modeInteger = getInt(
                    R.styleable.ConcentricOnboardingViewPager_mode,
                    0
                )
                mode = when (modeInteger) {
                    0 -> Mode.SLIDE
                    1 -> Mode.REVEAL
                    else -> DEFAULT_MODE
                }
            }
        }
        setDuration(scrollerDuration)
        setMode(mode)
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

    /**
     * Set the mode for the ConcentricOnboarding swipe animation
     * @param mode Can be either Mode.REVEAL or Mode.SLIDE
     */
    public fun setMode(mode: ConcentricOnboardingViewPager.Mode) {
        setPageTransformer(true, ConcentricOnboardingPageTransformer(mode))
    }

    /** ConcentricOnboarding animation mode */
    enum class Mode {
        SLIDE, REVEAL
    }

    internal companion object Constants {
        internal const val DEFAULT_SCROLLER_DURATION = 1000
        internal val DEFAULT_MODE = Mode.SLIDE
    }

    /**
     * `ConcentricOnboardingPageTransformer` is a custom [ViewPager.PageTransformer] that is used for ConcentricOnboarding reveal.
     */
    class ConcentricOnboardingPageTransformer(val mode: ConcentricOnboardingViewPager.Mode) :
        ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (page is ConcentricOnboardingLayout) {
                page.mode = mode
                when {
                    position < -1 -> {
                        page.revealForPercentage(0f)
                    }
                    position < 0 -> {
                        page.translationX = -(page.width * position)
                        if (mode == Mode.SLIDE) {
                            page.childrenTranslateX = (page.width * position) * 2
                            page.childrenTranslateY = -(page.height * position) * 0.35f
                            page.childrenScaleX = 1.0f + (0.5f * position)
                            page.childrenScaleY = 1.0f + (0.5f * position)
                        } else {
                            page.childrenTranslateX = 0f
                            page.childrenTranslateY = 0f
                            page.childrenScaleY = 1.0f
                            page.childrenScaleX = 1.0f
                        }
                        page.invalidate()
                        page.revealForPercentage(100 - abs(position * 100))
                    }
                    position <= 1 -> {
                        page.translationX = -(page.width * position)
                        if (mode == Mode.SLIDE) {
                            page.childrenTranslateX = (page.width * position) * 2
                            page.childrenTranslateY = (page.height * position) * 0.35f
                            page.childrenScaleX = 1.0f - (0.5f * position)
                            page.childrenScaleY = 1.0f - (0.5f * position)
                        } else {
                            page.childrenTranslateX = 0f
                            page.childrenTranslateY = 0f
                            page.childrenScaleY = 1.0f
                            page.childrenScaleX = 1.0f
                        }
                        page.invalidate()
                        page.revealForPercentage(100f)
                    }
                }
            }
        }
    }
}