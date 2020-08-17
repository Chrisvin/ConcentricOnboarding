package com.jem.concentriconboarding.clippathprovider

import android.graphics.Path
import android.graphics.PointF
import android.graphics.Region
import android.view.View
import com.jem.concentriconboarding.base.ClipPathProvider
import com.jem.concentriconboarding.util.MathUtil.convertValueUsingRange
import kotlin.math.max
import kotlin.math.pow

/**
 * [ClipPathProvider] which provides ConcentricOnboarding path.
 */
class ConcentricOnboardingClipPathProvider : ClipPathProvider() {

    private val BASE_RADIUS = 0
    private val LIMIT = 50
    private val INITIAL_CIRCLE = 5

    var radius = BASE_RADIUS
    var centerPoint = PointF(Float.MIN_VALUE, Float.MIN_VALUE)

    override fun getPath(percent: Float, view: View): Path {
        if (centerPoint.x == Float.MIN_VALUE) {
            centerPoint.x = view.width.toFloat() / 2
        }
        if (centerPoint.y == Float.MIN_VALUE) {
            centerPoint.y = view.height.toFloat() / 2
        }
        if (percent > LIMIT) {
            op = Region.Op.DIFFERENCE
            return getRightCirclePath(100f - percent, view)
        } else {
            op = Region.Op.INTERSECT
            return getLeftCirclePath(LIMIT - percent, view)
        }
    }

    private fun getRightCirclePath(originalProgress: Float, view: View): Path {
        if (originalProgress < 0.01f) {
            return Path()
        }
        if (originalProgress < INITIAL_CIRCLE) {
            return Path().apply {
                addCircle(
                    /*x = */centerPoint.x,
                    /*y = */centerPoint.y,
                    /*radius = */radius * originalProgress * (1f / INITIAL_CIRCLE),
                    Path.Direction.CW
                )
            }
        }
        val progress = convertValueUsingRange(
            originalProgress,
            INITIAL_CIRCLE.toFloat(), LIMIT.toFloat(),
            0f, LIMIT.toFloat()
        )
        val r = radius + 1.25.pow(progress.toDouble())
            .coerceAtMost(max(view.width, view.height).toDouble() * 100)
        val delta = ((1 - progress / LIMIT) * radius)
        return Path().apply {
            addCircle(
                /*x = */(centerPoint.x + (r - delta)).toFloat(),
                /*y = */centerPoint.y,
                /*radius = */r.toFloat(),
                Path.Direction.CW
            )
        }
    }

    private fun getLeftCirclePath(originalProgress: Float, view: View): Path {
        if (originalProgress > LIMIT - 0.01f) {
            return Path()
        }
        if (originalProgress > LIMIT - INITIAL_CIRCLE) {
            return Path().apply {
                addCircle(
                    /*x = */centerPoint.x,
                    /*y = */centerPoint.y,
                    /*radius = */radius * (LIMIT - originalProgress) * (1f / INITIAL_CIRCLE),
                    Path.Direction.CW
                )
            }
        }
        val progress = convertValueUsingRange(
            originalProgress,
            0f, LIMIT.toFloat() - INITIAL_CIRCLE,
            0f, LIMIT.toFloat()
        )
        val r = radius + 1.25.pow((LIMIT - progress).toDouble())
            .coerceAtMost(max(view.width, view.height).toDouble() * 100)
        val delta = (progress / LIMIT) * radius
        return Path().apply {
            addCircle(
                /*x = */(centerPoint.x - r + delta).toFloat(),
                /*y = */centerPoint.y,
                /*radius = */r.toFloat(),
                Path.Direction.CW
            )
        }
    }
}