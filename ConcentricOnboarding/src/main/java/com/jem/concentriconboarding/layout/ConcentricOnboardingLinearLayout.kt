package com.jem.concentriconboarding.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import com.jem.concentriconboarding.ConcentricOnboardingViewPager
import com.jem.concentriconboarding.base.ClipPathProvider
import com.jem.concentriconboarding.base.ConcentricOnboardingLayout
import com.jem.concentriconboarding.clippathprovider.ConcentricOnboardingClipPathProvider

/**
 * `ConcentricOnboardingLinearLayout` is a custom [LinearLayout] that implements [ConcentricOnboardingLayout].
 */
open class ConcentricOnboardingLinearLayout : LinearLayout, ConcentricOnboardingLayout {
    // Store path in local variable rather then getting it from ClipPathProvider each time
    private var path: Path? = null

    // Backing fields for ConcentricOnboardingLayout variables
    private var _clipPathProvider: ClipPathProvider = ConcentricOnboardingClipPathProvider()
    private var _currentRevealPercent: Float = 100f
    private var _childrenTranslateX: Float = 0f
    private var _childrenTranslateY: Float = 0f
    private var _childrenScaleX: Float = 1f
    private var _childrenScaleY: Float = 1f
    private var _mode: ConcentricOnboardingViewPager.Mode =
        ConcentricOnboardingViewPager.Constants.DEFAULT_MODE

    override var clipPathProvider = _clipPathProvider
    override var currentRevealPercent: Float
        get() = _currentRevealPercent
        set(value) {
            revealForPercentage(value)
        }
    override var childrenTranslateX: Float = _childrenTranslateX
    override var childrenTranslateY: Float = _childrenTranslateY
    override var childrenScaleX: Float = _childrenScaleX
    override var childrenScaleY: Float = _childrenScaleY
    override var mode: ConcentricOnboardingViewPager.Mode = _mode

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * Overriden from View
     */
    override fun draw(canvas: Canvas?) {
        try {
            canvas?.save()
            path?.let {
                canvas?.clipPath(it, clipPathProvider.op)
            }
            super.draw(canvas)
        } finally {
            canvas?.restore()
        }
    }

    /**
     * Overriden from View
     */
    override fun dispatchDraw(canvas: Canvas?) {
        if (mode == ConcentricOnboardingViewPager.Mode.SLIDE) {
            try {
                canvas?.restore()
                canvas?.save()
                canvas?.translate(childrenTranslateX, childrenTranslateY)
                canvas?.scale(childrenScaleX, childrenScaleY)
                super.dispatchDraw(canvas)
            } finally {
                canvas?.restore()
                canvas?.save()
                path?.let {
                    canvas?.clipPath(it, clipPathProvider.op)
                }
            }
        } else {
            super.dispatchDraw(canvas)
        }
    }

    override fun revealForPercentage(percent: Float) {
        if (percent == _currentRevealPercent) return
        _currentRevealPercent = percent
        path = clipPathProvider.getPath(percent, this@ConcentricOnboardingLinearLayout)
        invalidate()
    }
}