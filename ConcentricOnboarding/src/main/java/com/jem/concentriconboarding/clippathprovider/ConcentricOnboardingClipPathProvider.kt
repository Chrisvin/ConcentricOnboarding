package com.jem.concentriconboarding.clippathprovider

import android.graphics.Path
import android.graphics.PointF
import android.graphics.Region
import android.view.View
import com.jem.concentriconboarding.base.ClipPathProvider
import kotlin.math.max
import kotlin.math.pow

/**
 * [ClipPathProvider] which provides ConcentricOnboarding path.
 */
class ConcentricOnboardingClipPathProvider : ClipPathProvider() {

    private val BASE_RADIUS = 0
    private val LIMIT = 50

    var radius = BASE_RADIUS
    var centerPoint = PointF()

    override fun getPath(percent: Float, view: View): Path {
        centerPoint.x = view.width.toFloat() / 2
        centerPoint.y = view.height.toFloat() / 2
        if (percent > LIMIT) {
            op = Region.Op.DIFFERENCE
            return getRightCirclePath(100f - percent, view)
        } else {
            op = Region.Op.INTERSECT
            return getLeftCirclePath(LIMIT - percent, view)
        }
    }

    private fun getRightCirclePath(progress: Float, view: View): Path {
        if (progress < 0.01f) {
            return Path()
        }
        val r = radius + 1.25.pow(progress.toDouble())
            .coerceAtMost(max(view.width, view.height).toDouble() * 100)
        val delta = ((1 - progress / LIMIT) * radius)
        return Path().apply {
            addCircle(
                /*x = */(centerPoint.x + (r + radius - delta)).toFloat(),
                /*y = */centerPoint.y,
                /*radius = */r.toFloat(),
                Path.Direction.CW
            )
        }
    }

    private fun getLeftCirclePath(progress: Float, view: View): Path {
        if (progress > LIMIT - 0.01f) {
            return Path()
        }
        val r = radius + 1.25.pow((LIMIT - progress).toDouble())
            .coerceAtMost(max(view.width, view.height).toDouble() * 100)
        val delta = (progress / LIMIT) * radius
        return Path().apply {
            addCircle(
                /*x = */(centerPoint.x - r + radius + delta).toFloat(),
                /*y = */centerPoint.y,
                /*radius = */r.toFloat(),
                Path.Direction.CW
            )
        }
    }
}