package com.jem.concentriconboarding.layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
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
    private var _canvasTranslateX: Float = 0f

    override var clipPathProvider = _clipPathProvider
    override var currentRevealPercent: Float
        get() = _currentRevealPercent
        set(value) {
            revealForPercentage(value)
        }
    override var canvasTranslateX: Float
        get() = _canvasTranslateX
        set(value) {
            _canvasTranslateX = value
            invalidate()
        }

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
        try {
            canvas?.restore()
            canvas?.save()
            canvas?.translate(canvasTranslateX, 0f)
            super.dispatchDraw(canvas)
        } finally {
            canvas?.restore()
            canvas?.save()
            path?.let {
                canvas?.clipPath(it, clipPathProvider.op)
            }
        }
    }

    override fun revealForPercentage(percent: Float) {
        if (percent == _currentRevealPercent) return
        _currentRevealPercent = percent
        path = clipPathProvider.getPath(percent, this@ConcentricOnboardingLinearLayout)
        invalidate()
    }
}