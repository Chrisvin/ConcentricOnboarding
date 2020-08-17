package com.jem.concentriconboarding.util

object MathUtil {

    fun convertValueUsingRange(
        oldValue: Float,
        oldMin: Float,
        oldMax: Float,
        newMin: Float,
        newMax: Float
    ): Float {
        val oldRange = (oldMax - oldMin)
        return if (oldRange == 0f)
            newMin
        else {
            (((oldValue - oldMin) * (newMax - newMin)) / oldRange) + newMin
        }
    }

    fun lerp(a: Float, b: Float, t: Float): Float {
        return (a * (1f - t)) + (b * t)
    }
}