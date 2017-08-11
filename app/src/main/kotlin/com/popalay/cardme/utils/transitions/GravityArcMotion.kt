/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popalay.cardme.utils.transitions

import android.graphics.Path
import android.transition.ArcMotion

/**
 * A tweak to [ArcMotion] which slightly alters the path calculation. In the real world
 * gravity slows upward motion and accelerates downward motion. This class emulates this behavior
 * to make motion paths appear more natural.
 *
 * See https://www.google.com/design/spec/motion/movement.html#movement-movement-within-screen-bounds
 */
class GravityArcMotion : ArcMotion() {

    companion object {

        private val DEFAULT_MAX_ANGLE_DEGREES = 70f
        private val DEFAULT_MAX_TANGENT = Math.tan(Math.toRadians((DEFAULT_MAX_ANGLE_DEGREES / 2).toDouble())).toFloat()

        private fun toTangent(arcInDegrees: Float): Float {
            if (arcInDegrees < 0 || arcInDegrees > 90) throw IllegalArgumentException("Arc must be between 0 and 90 degrees")
            return Math.tan(Math.toRadians((arcInDegrees / 2).toDouble())).toFloat()
        }
    }

    private var minimumHorizontalAngle = 0f
    private var minimumVerticalAngle = 0f
    private var maximumAngle = DEFAULT_MAX_ANGLE_DEGREES
    private var minimumHorizontalTangent = 0f
    private var minimumVerticalTangent = 0f
    private var maximumTangent = DEFAULT_MAX_TANGENT

    override fun setMinimumHorizontalAngle(angleInDegrees: Float) {
        minimumHorizontalAngle = angleInDegrees
        minimumHorizontalTangent = toTangent(angleInDegrees)
    }

    override fun getMinimumHorizontalAngle(): Float {
        return minimumHorizontalAngle
    }

    override fun setMinimumVerticalAngle(angleInDegrees: Float) {
        minimumVerticalAngle = angleInDegrees
        minimumVerticalTangent = toTangent(angleInDegrees)
    }

    override fun getMinimumVerticalAngle(): Float {
        return minimumVerticalAngle
    }

    override fun setMaximumAngle(angleInDegrees: Float) {
        maximumAngle = angleInDegrees
        maximumTangent = toTangent(angleInDegrees)
    }

    override fun getMaximumAngle(): Float {
        return maximumAngle
    }

    override fun getPath(startX: Float, startY: Float, endX: Float, endY: Float): Path {

        val path = Path()
        path.moveTo(startX, startY)

        var ex: Float
        var ey: Float
        if (startY == endY) {
            ex = (startX + endX) / 2
            ey = startY + minimumHorizontalTangent * Math.abs(endX - startX) / 2
        } else if (startX == endX) {
            ex = startX + minimumVerticalTangent * Math.abs(endY - startY) / 2
            ey = (startY + endY) / 2
        } else {
            val deltaX = endX - startX

            val deltaY: Float
            if (endY < startY) {
                deltaY = startY - endY
            } else {
                deltaY = endY - startY
            }

            val h2 = deltaX * deltaX + deltaY * deltaY

            val dx = (startX + endX) / 2
            val dy = (startY + endY) / 2

            val midDist2 = h2 * 0.25f

            val minimumArcDist2: Float

            if (Math.abs(deltaX) < Math.abs(deltaY)) {
                val eDistY = h2 / (2 * deltaY)
                ey = endY + eDistY
                ex = endX

                minimumArcDist2 = midDist2 * minimumVerticalTangent * minimumVerticalTangent
            } else {
                val eDistX = h2 / (2 * deltaX)
                ex = endX + eDistX
                ey = endY

                minimumArcDist2 = midDist2 * minimumHorizontalTangent * minimumHorizontalTangent
            }
            val arcDistX = dx - ex
            val arcDistY = dy - ey
            val arcDist2 = arcDistX * arcDistX + arcDistY * arcDistY

            val maximumArcDist2 = midDist2 * maximumTangent * maximumTangent

            var newArcDistance2 = 0f
            if (arcDist2 < minimumArcDist2) {
                newArcDistance2 = minimumArcDist2
            } else if (arcDist2 > maximumArcDist2) {
                newArcDistance2 = maximumArcDist2
            }
            if (newArcDistance2 != 0f) {
                val ratio2 = newArcDistance2 / arcDist2
                val ratio = Math.sqrt(ratio2.toDouble()).toFloat()
                ex = dx + ratio * (ex - dx)
                ey = dy + ratio * (ey - dy)
            }
        }
        val controlX1 = (startX + ex) / 2
        val controlY1 = (startY + ey) / 2
        val controlX2 = (ex + endX) / 2
        val controlY2 = (ey + endY) / 2
        path.cubicTo(controlX1, controlY1, controlX2, controlY2, endX, endY)
        return path
    }

}
