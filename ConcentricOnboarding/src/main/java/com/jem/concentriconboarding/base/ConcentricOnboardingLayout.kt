package com.jem.concentriconboarding.base

import android.graphics.PointF
import com.jem.concentriconboarding.ConcentricOnboardingViewPager
import com.jem.concentriconboarding.clippathprovider.ConcentricOnboardingClipPathProvider

/**
 * ConcentricOnboardingLayout is the base interface for all the other ConcentricOnboardingLayouts
 */
interface ConcentricOnboardingLayout {

    /** ClipPathProvider provides the path used for clipping. */
    var clipPathProvider: ClipPathProvider

    /** Percentage of the view currently revealed. */
    var currentRevealPercent: Float

    /** Animation mode (REVEAL or SLIDE) used to animate views. Automatically set by viewpager. */
    var mode: ConcentricOnboardingViewPager.Mode

    /** X-axis translation applied to layout children alone in SLIDE mode. Automatically set by viewpager. */
    var childrenTranslateX: Float

    /** Y-axis translation applied to layout children alone in SLIDE mode. Automatically set by viewpager. */
    var childrenTranslateY: Float

    /** X-axis scale applied to layout children alone in SLIDE mode. Automatically set by viewpager. */
    var childrenScaleX: Float

    /** Y-axis scale applied to layout children alone in SLIDE mode. Automatically set by viewpager. */
    var childrenScaleY: Float

    /**
     * Update view to specified reveal percentage.
     * @param percent value should be between 0 and 100 (inclusive).
     */
    fun revealForPercentage(percent: Float): Unit

    /**
     * Set the center point of the reveal animation
     */
    fun setCenterPoint(centerPoint: PointF): Boolean {
        return if (clipPathProvider is ConcentricOnboardingClipPathProvider) {
            (clipPathProvider as ConcentricOnboardingClipPathProvider).centerPoint = centerPoint
            true
        } else {
            false
        }
    }

    /**
     * Set the radius of the initial/final reveal animation circle
     */
    fun setRadius(radius: Int) {
        if (clipPathProvider is ConcentricOnboardingClipPathProvider) {
            (clipPathProvider as ConcentricOnboardingClipPathProvider).radius = radius
        }
    }
}