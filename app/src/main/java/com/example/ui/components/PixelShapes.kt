package com.example.ui.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.cos
import kotlin.math.sin

/**
 * Custom 4-lobe / 4-petal flower shape iconic to Google Pixel Material You clock widgets.
 */
class PixelScallopShape(private val petalCount: Int = 4, private val depthRatio: Float = 0.16f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val cx = size.width / 2f
        val cy = size.height / 2f
        val baseRadius = (minOf(size.width, size.height) / 2f) * 0.94f

        val points = 72
        val step = (2 * Math.PI / points).toFloat()

        for (i in 0..points) {
            val angle = i * step
            // Modulate radius with cos(petalCount * angle)
            val modulation = 1f + depthRatio * cos(petalCount * angle)
            val r = baseRadius * modulation
            val x = cx + r * cos(angle)
            val y = cy + r * sin(angle)

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }
        path.close()
        return Outline.Generic(path)
    }
}

/**
 * Pixel Super-ellipse Squircle shape for Material You cards
 */
class PixelSquircleShape(val cornerRadiusPercent: Float = 0.35f) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val w = size.width
        val h = size.height
        val r = minOf(w, h) * cornerRadiusPercent

        path.moveTo(r, 0f)
        path.lineTo(w - r, 0f)
        path.cubicTo(w - r * 0.4f, 0f, w, r * 0.4f, w, r)
        path.lineTo(w, h - r)
        path.cubicTo(w, h - r * 0.4f, w - r * 0.4f, h, w - r, h)
        path.lineTo(r, h)
        path.cubicTo(r * 0.4f, h, 0f, h - r * 0.4f, 0f, h - r)
        path.lineTo(0f, r)
        path.cubicTo(0f, r * 0.4f, r * 0.4f, 0f, r, 0f)
        path.close()

        return Outline.Generic(path)
    }
}
