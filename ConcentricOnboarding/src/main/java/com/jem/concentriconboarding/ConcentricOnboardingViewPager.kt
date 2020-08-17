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
import com.jem.concentriconboarding.util.MathUtil.lerp
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

    /**
     * Backing fields for the variables
     * (to be used when values need to be changed without triggering of `updatePageTransformer`)
     */
    private var _mode: ConcentricOnboardingViewPager.Mode = DEFAULT_MODE
    private var _revealCenterPoint: PointF = DEFAULT_REVEAL_CENTER_POINT
    private var _revealRadius: Int = DEFAULT_REVEAL_RADIUS
    private var _scaleXFactor: Float = DEFAULT_SCALE_FACTOR
    private var _scaleYFactor: Float = DEFAULT_SCALE_FACTOR
    private var _translationXFactor: Float = DEFAULT_TRANSLATION_X_FACTOR
    private var _translationYFactor: Float = DEFAULT_TRANSLATION_Y_FACTOR

    /**
     * Mode of the ConcentricOnboarding animation
     */
    var mode: ConcentricOnboardingViewPager.Mode
        get() = _mode
        set(value) {
            _mode = value
            updatePageTransformer()
        }

    /**
     * Center of the initial/final reveal circle
     */
    var revealCenterPoint: PointF?
        get() = _revealCenterPoint
        set(value) {
            if (value != null) {
                _revealCenterPoint = value
            } else {
                _revealCenterPoint = DEFAULT_REVEAL_CENTER_POINT
            }
            updatePageTransformer()
        }

    /**
     * Radius of the initial/final reveal circle
     */
    var revealRadius: Int
        get() = _revealRadius
        set(value) {
            _revealRadius = value
            updatePageTransformer()
        }

    /**
     * X-axis Scale of children after page has been scrolled away
     */
    var scaleXFactor: Float
        get() = _scaleXFactor
        set(value) {
            _scaleXFactor = value
            updatePageTransformer()
        }

    /**
     * Y-axis Scale of children after page has been scrolled away
     */
    var scaleYFactor: Float
        get() = _scaleYFactor
        set(value) {
            _scaleYFactor = value
            updatePageTransformer()
        }

    /**
     * X-axis Translation of children after page has been scrolled away.
     * Determines the speed with which the children are move when swiping.
     */
    var translationXFactor: Float
        get() = _translationXFactor
        set(value) {
            _translationXFactor = value
            updatePageTransformer()
        }

    /**
     * Y-axis Translation of children after page has been scrolled away.
     */
    var translationYFactor: Float
        get() = _translationYFactor
        set(value) {
            _translationYFactor = value
            updatePageTransformer()
        }

    private fun updatePageTransformer() {
        setPageTransformer(
            true,
            ConcentricOnboardingPageTransformer(
                mode,
                scaleXFactor,
                scaleYFactor,
                translationXFactor,
                translationYFactor,
                revealCenterPoint,
                revealRadius
            )
        )
    }

    private fun initialize(context: Context, attrs: AttributeSet?) {
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
                _mode = when (modeInteger) {
                    0 -> Mode.SLIDE
                    1 -> Mode.REVEAL
                    else -> DEFAULT_MODE
                }
                _scaleXFactor = getFloat(
                    R.styleable.ConcentricOnboardingViewPager_scaleXFactor,
                    DEFAULT_SCALE_FACTOR
                )
                _scaleYFactor = getFloat(
                    R.styleable.ConcentricOnboardingViewPager_scaleYFactor,
                    DEFAULT_SCALE_FACTOR
                )
                _translationXFactor = getFloat(
                    R.styleable.ConcentricOnboardingViewPager_translationXFactor,
                    DEFAULT_TRANSLATION_X_FACTOR
                )
                _translationYFactor = getFloat(
                    R.styleable.ConcentricOnboardingViewPager_translationYFactor,
                    DEFAULT_TRANSLATION_Y_FACTOR
                )
            }
        }
        setDuration(scrollerDuration)
        updatePageTransformer()
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

    /** ConcentricOnboarding animation mode */
    enum class Mode {
        SLIDE, REVEAL
    }

    internal companion object Constants {
        internal const val DEFAULT_SCROLLER_DURATION = 1000
        internal const val DEFAULT_SCALE_FACTOR = 0.5f
        internal const val DEFAULT_TRANSLATION_X_FACTOR = 2f
        internal const val DEFAULT_TRANSLATION_Y_FACTOR = 0.35f
        internal const val DEFAULT_REVEAL_RADIUS = 0

        // Center point get's converted to view center internally in COClipPathProvider,
        // by checking for Float.MIN_VALUE
        internal val DEFAULT_REVEAL_CENTER_POINT = PointF(Float.MIN_VALUE, Float.MIN_VALUE)
        internal val DEFAULT_MODE = Mode.SLIDE
    }

    /**
     * `ConcentricOnboardingPageTransformer` is a custom [ViewPager.PageTransformer] that is used for ConcentricOnboarding reveal.
     */
    class ConcentricOnboardingPageTransformer(
        val mode: ConcentricOnboardingViewPager.Mode,
        val scaleXFactor: Float,
        val scaleYFactor: Float,
        val translationXFactor: Float,
        val translationYFactor: Float,
        val revealCenterPoint: PointF?,
        val revealRadius: Int
    ) :
        ViewPager.PageTransformer {
        override fun transformPage(page: View, position: Float) {
            if (page is ConcentricOnboardingLayout) {
                page.mode = mode
                revealCenterPoint?.let {
                    page.setCenterPoint(it)
                }
                page.setRadius(revealRadius)
                when {
                    position < -1 -> {
                        page.revealForPercentage(0f)
                    }
                    position < 0 -> {
                        page.translationX = -(page.width * position)
                        if (mode == Mode.SLIDE) {
                            page.childrenTranslateX = (page.width * position) * translationXFactor
                            page.childrenTranslateY = -(page.height * position) * translationYFactor
                            page.childrenScaleX = lerp(1.0f, scaleXFactor, -position)
                            page.childrenScaleY = lerp(1.0f, scaleYFactor, -position)
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
                            page.childrenTranslateX = (page.width * position) * translationXFactor
                            page.childrenTranslateY = (page.height * position) * translationYFactor
                            page.childrenScaleX = lerp(1.0f, scaleXFactor, position)
                            page.childrenScaleY = lerp(1.0f, scaleYFactor, position)
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