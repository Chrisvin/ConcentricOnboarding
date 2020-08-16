package com.jem.concentriconboarding.base

import android.graphics.PointF
import com.jem.concentriconboarding.clippathprovider.ConcentricOnboardingClipPathProvider

/**
 * ConcentricOnboardingLayout is the base interface for all the other ConcentricOnboardingLayouts
 */
interface ConcentricOnboardingLayout {

    /** ClipPathProvider provides the path used for clipping. */
    var clipPathProvider: ClipPathProvider

    /** Percentage of the view currently revealed. */
    var currentRevealPercent: Float

    var canvasTranslateX: Float

    /**
     * Update view to specified reveal percentage.
     * @param percent value should be between 0 and 100 (inclusive).
     */
    fun revealForPercentage(percent: Float): Unit

    /**
     * Set the center point of the reveal animation
     */
    private fun setCenterPoint(centerPoint: PointF): Boolean {
        return if (clipPathProvider is ConcentricOnboardingClipPathProvider) {
            (clipPathProvider as ConcentricOnboardingClipPathProvider).centerPoint = centerPoint
            true
        } else {
            false
        }
    }
}