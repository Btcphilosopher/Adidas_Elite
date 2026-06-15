package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.SportsNeon
import com.example.ui.theme.CharcoalGray
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ProductVectorRenderer(
    silhouetteId: String,
    angleDegrees: Float, // 0 to 360
    visualMode: String, // "Solid", "Material Close-Up", "Wireframe X-Ray"
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_radar")
    val pulseRatio by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_ratio"
    )

    Canvas(modifier = modifier.background(Color.Transparent)) {
        val width = size.width
        val height = size.height
        val centerX = width / 2
        val centerY = height / 2

        // Rotate & Shear based on 360-degree angle to simulate three dimensions!
        val scaleX = cos(Math.toRadians(angleDegrees.toDouble())).toFloat()
        val shearY = (sin(Math.toRadians(angleDegrees.toDouble())) * 0.15f).toFloat()

        // Background tech gird circle
        drawCircle(
            color = ElectricBlue.copy(alpha = 0.08f),
            radius = size.minDimension * 0.45f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
        )

        drawCircle(
            color = ElectricBlue.copy(alpha = 0.04f),
            radius = size.minDimension * 0.45f * pulseRatio,
            center = Offset(centerX, centerY),
            style = Stroke(width = 2.dp.toPx())
        )

        withTransform({
            // Scale and shear on the X plane to create a beautiful horizontal depth rotation
            scale(scaleX = if (scaleX in 0f..0.1f) 0.1f else if (scaleX in -0.1f..0f) -0.1f else scaleX, scaleY = 1.0f, pivot = Offset(centerX, centerY))
            inset(left = 20f, right = 20f, top = 20f, bottom = 20f)
        }) {
            when (silhouetteId) {
                "runner" -> drawRunner(centerX, centerY, visualMode, pulseRatio)
                "cleat" -> drawCleat(centerX, centerY, visualMode, pulseRatio)
                "jacket" -> drawJacket(centerX, centerY, visualMode, pulseRatio)
                "hoodie" -> drawHoodie(centerX, centerY, visualMode, pulseRatio)
                else -> drawRunner(centerX, centerY, visualMode, pulseRatio)
            }
        }

        // Draw HUD overlay specs labels (Porsche dashboard style)
        drawHUDLabels(width, height, visualMode)
    }
}

private fun DrawScope.drawRunner(cx: Float, cy: Float, mode: String, pulse: Float) {
    val shoeWidth = size.width * 0.75f
    val shoeHeight = size.height * 0.4f
    val left = cx - shoeWidth / 2
    val top = cy - shoeHeight / 2

    // Color definitions for modes
    val strokeColor = when (mode) {
        "Wireframe X-Ray" -> ElectricBlue
        "Material Close-Up" -> SportsNeon
        else -> Color.White
    }

    val fillColor = when (mode) {
        "Wireframe X-Ray" -> Color.Transparent
        "Material Close-Up" -> ElectricBlue.copy(alpha = 0.15f)
        else -> Color(0xFF1B1D25)
    }

    // Path for the shoe upper
    val upperPath = Path().apply {
        // Heel counter back
        moveTo(left + shoeWidth * 0.1f, cy + shoeHeight * 0.1f)
        // Collar / ankle opening
        quadraticTo(
            left + shoeWidth * 0.12f, cy - shoeHeight * 0.45f,
            left + shoeWidth * 0.35f, cy - shoeHeight * 0.45f
        )
        // Tongue climb
        lineTo(left + shoeWidth * 0.52f, cy - shoeHeight * 0.3f)
        // Instep down to toe
        quadraticTo(
            left + shoeWidth * 0.7f, cy + shoeHeight * 0.05f,
            left + shoeWidth * 0.95f, cy + shoeHeight * 0.1f
        )
        // Toe bumper curved tip
        quadraticTo(
            left + shoeWidth * 0.98f, cy + shoeHeight * 0.22f,
            left + shoeWidth * 0.92f, cy + shoeHeight * 0.25f
        )
        // Midsole upper attachment line back
        lineTo(left + shoeWidth * 0.08f, cy + shoeHeight * 0.2f)
        close()
    }

    // Midsole (Boost energy core)
    val midsolePath = Path().apply {
        moveTo(left + shoeWidth * 0.06f, cy + shoeHeight * 0.21f)
        lineTo(left + shoeWidth * 0.94f, cy + shoeHeight * 0.26f)
        quadraticTo(
            left + shoeWidth * 0.96f, cy + shoeHeight * 0.38f,
            left + shoeWidth * 0.90f, cy + shoeHeight * 0.4f
        )
        // Curved arch support bottom
        quadraticTo(
            left + shoeWidth * 0.5f, cy + shoeHeight * 0.3f,
            left + shoeWidth * 0.08f, cy + shoeHeight * 0.38f
        )
        close()
    }

    // DRAW BASE SOLID
    if (mode != "Wireframe X-Ray") {
        drawPath(path = upperPath, color = fillColor)
        drawPath(path = midsolePath, color = when (mode) {
            "Material Close-Up" -> SportsNeon.copy(alpha = 0.25f)
            else -> Color(0xFF282C37)
        })
    }

    // DRAW PATH BOUNDARIES
    drawPath(path = upperPath, color = strokeColor, style = Stroke(width = 2.dp.toPx()))
    drawPath(path = midsolePath, color = if (mode == "Material Close-Up") ElectricBlue else strokeColor, style = Stroke(width = 2.dp.toPx()))

    // Adidas Stripes details (Asymmetrical European design)
    val stripePath1 = Path().apply {
        moveTo(left + shoeWidth * 0.42f, cy - shoeHeight * 0.15f)
        lineTo(left + shoeWidth * 0.36f, cy + shoeHeight * 0.18f)
    }
    val stripePath2 = Path().apply {
        moveTo(left + shoeWidth * 0.48f, cy - shoeHeight * 0.14f)
        lineTo(left + shoeWidth * 0.42f, cy + shoeHeight * 0.19f)
    }
    val stripePath3 = Path().apply {
        moveTo(left + shoeWidth * 0.54f, cy - shoeHeight * 0.13f)
        lineTo(left + shoeWidth * 0.48f, cy + shoeHeight * 0.20f)
    }

    drawPath(stripePath1, color = strokeColor, style = Stroke(width = 7f))
    drawPath(stripePath2, color = strokeColor, style = Stroke(width = 7f))
    drawPath(stripePath3, color = strokeColor, style = Stroke(width = 7f))

    // Tech accents if Wireframe or Thermal
    if (mode == "Wireframe X-Ray") {
        // Digital 4D mesh lattice cell grids (lines)
        for (i in 1..8) {
            val ratio = i / 9f
            val x = left + shoeWidth * (0.1f + ratio * 0.8f)
            drawLine(
                color = SportsNeon.copy(alpha = 0.5f),
                start = Offset(x, cy - shoeHeight * 0.2f),
                end = Offset(x - 15f, cy + shoeHeight * 0.25f),
                strokeWidth = 1f
            )
        }
    } else if (mode == "Material Close-Up") {
        // Pulsating thermal hotpoints
        drawCircle(
            color = SportsNeon,
            radius = 12f * pulse,
            center = Offset(left + shoeWidth * 0.85f, cy + shoeHeight * 0.18f)
        )
        drawCircle(
            color = SportsNeon.copy(alpha = 0.4f),
            radius = 24f * pulse,
            center = Offset(left + shoeWidth * 0.85f, cy + shoeHeight * 0.18f),
            style = Stroke(width = 1.dp.toPx())
        )

        // Continental rubber bottom line
        drawLine(
            color = SportsNeon,
            start = Offset(left + shoeWidth * 0.12f, cy + shoeHeight * 0.36f),
            end = Offset(left + shoeWidth * 0.88f, cy + shoeHeight * 0.38f),
            strokeWidth = 3f
        )
    } else {
        // Solid accents - Premium silver heel piece
        val heelPiece = Path().apply {
            moveTo(left + shoeWidth * 0.08f, cy + shoeHeight * 0.05f)
            quadraticTo(
                left + shoeWidth * 0.15f, cy + shoeHeight * 0.03f,
                left + shoeWidth * 0.18f, cy + shoeHeight * 0.18f
            )
            lineTo(left + shoeWidth * 0.07f, cy + shoeHeight * 0.18f)
            close()
        }
        drawPath(heelPiece, color = ElectricBlue)
    }
}

private fun DrawScope.drawCleat(cx: Float, cy: Float, mode: String, pulse: Float) {
    val w = size.width * 0.70f
    val h = size.height * 0.38f
    val left = cx - w / 2
    val top = cy - h / 2

    val strokeColor = if (mode == "Wireframe X-Ray") ElectricBlue else Color.White
    val fillColor = if (mode == "Wireframe X-Ray") Color.Transparent else CharcoalGray

    // Cleat upper profile path
    val mainPath = Path().apply {
        moveTo(left + w * 0.05f, cy - h * 0.1f)
        quadraticTo(left + w * 0.05f, cy - h * 0.35f, left + w * 0.25f, cy - h * 0.4f)
        quadraticTo(left + w * 0.45f, cy - h * 0.38f, left + w * 0.60f, cy - h * 0.2f)
        quadraticTo(left + w * 0.88f, cy + h * 0.08f, left + w * 0.98f, cy + h * 0.12f)
        lineTo(left + w * 0.92f, cy + h * 0.3f)
        lineTo(left + w * 0.05f, cy + h * 0.28f)
        close()
    }

    if (mode != "Wireframe X-Ray") {
        drawPath(mainPath, color = fillColor)
    }
    drawPath(mainPath, color = strokeColor, style = Stroke(width = 2.dp.toPx()))

    // Studs / Spikes (Precision traction systems) at bottom
    val studs = listOf(0.12f, 0.28f, 0.42f, 0.62f, 0.78f, 0.88f)
    studs.forEach { rx ->
        val studX = left + w * rx
        val studY = cy + h * 0.29f
        val spikePath = Path().apply {
            moveTo(studX - 8f, studY)
            lineTo(studX, studY + 18f)
            lineTo(studX + 8f, studY)
            close()
        }
        drawPath(spikePath, color = if (mode == "Material Close-Up") SportsNeon else ElectricBlue)
    }

    // Sleek details (K-Knit visual compression bands)
    drawLine(
        color = strokeColor.copy(alpha = 0.5f),
        start = Offset(left + w * 0.15f, cy - h * 0.38f),
        end = Offset(left + w * 0.32f, cy + h * 0.28f),
        strokeWidth = 2f
    )
    drawLine(
        color = strokeColor.copy(alpha = 0.5f),
        start = Offset(left + w * 0.22f, cy - h * 0.39f),
        end = Offset(left + w * 0.39f, cy + h * 0.28f),
        strokeWidth = 2f
    )
}

private fun DrawScope.drawJacket(cx: Float, cy: Float, mode: String, pulse: Float) {
    val w = size.width * 0.5f
    val h = size.height * 0.6f
    val left = cx - w / 2
    val top = cy - h / 2

    val strokeColor = if (mode == "Wireframe X-Ray") ElectricBlue else Color.White
    val fillColor = if (mode == "Wireframe X-Ray") Color.Transparent else CharcoalGray

    // Alpine Shell Path
    val jacketPath = Path().apply {
        // Hood
        moveTo(cx - 30f, top + 15f)
        quadraticTo(cx, top - 35f, cx + 30f, top + 15f)
        // Collar
        lineTo(cx + 40f, top + 55f)
        // Sleeve right
        lineTo(left + w * 0.95f, top + h * 0.3f)
        lineTo(left + w * 0.82f, top + h * 0.42f)
        // Armpit right back down
        lineTo(left + w * 0.75f, top + h * 0.4f)
        // Body right bottom
        lineTo(left + w * 0.78f, top + h * 0.95f)
        // Waist
        lineTo(left + w * 0.22f, top + h * 0.95f)
        // Body left bottom
        lineTo(left + w * 0.25f, top + h * 0.4f)
        // Armpit left up
        lineTo(left + w * 0.18f, top + h * 0.42f)
        // Sleeve left
        lineTo(left + w * 0.05f, top + h * 0.3f)
        // Collar left
        lineTo(cx - 40f, top + 55f)
        close()
    }

    if (mode != "Wireframe X-Ray") {
        drawPath(jacketPath, color = fillColor)
    }
    drawPath(jacketPath, color = strokeColor, style = Stroke(width = 2.dp.toPx()))

    // Main thermo-zipper line
    drawLine(
        color = if (mode == "Material Close-Up") SportsNeon else ElectricBlue,
        start = Offset(cx, top + 55f),
        end = Offset(cx, top + h * 0.95f),
        strokeWidth = 3f
    )

    // Heat sensors / pockets in Alpine mode
    if (mode == "Material Close-Up") {
        drawRoundRect(
            color = SportsNeon.copy(alpha = 0.3f),
            topLeft = Offset(left + w * 0.55f, top + h * 0.42f),
            size = Size(40f, 60f),
            cornerRadius = CornerRadius(8f, 8f),
            style = Stroke(width = 2f)
        )
    }
}

private fun DrawScope.drawHoodie(cx: Float, cy: Float, mode: String, pulse: Float) {
    val w = size.width * 0.52f
    val h = size.height * 0.58f
    val left = cx - w / 2
    val top = cy - h / 2

    val strokeColor = if (mode == "Wireframe X-Ray") ElectricBlue else Color.White
    val fillColor = if (mode == "Wireframe X-Ray") Color.Transparent else CharcoalGray

    val upperPath = Path().apply {
        // Hood
        moveTo(cx - 45f, top + 40f)
        quadraticTo(cx, top - 20f, cx + 45f, top + 40f)
        // Collar
        lineTo(cx + 45f, top + 60f)
        // Shoulders & Sleeves
        lineTo(left + w, top + h * 0.32f)
        lineTo(left + w * 0.85f, top + h * 0.38f)
        // Underarm down
        lineTo(left + w * 0.72f, top + h * 0.92f)
        // Hem
        lineTo(left + w * 0.28f, top + h * 0.92f)
        // Underarm left up
        lineTo(left + w * 0.15f, top + h * 0.38f)
        // Sleeve left
        lineTo(left, top + h * 0.32f)
        close()
    }

    if (mode != "Wireframe X-Ray") {
        drawPath(upperPath, color = fillColor)
    }
    drawPath(upperPath, color = strokeColor, style = Stroke(width = 2.dp.toPx()))

    // Ribbed cuffs details
    drawLine(
        color = strokeColor,
        start = Offset(left + w * 0.28f, top + h * 0.92f),
        end = Offset(left + w * 0.72f, top + h * 0.92f),
        strokeWidth = 6f
    )
}

private fun DrawScope.drawHUDLabels(width: Float, height: Float, mode: String) {
    // Elegant tiny corners to feel like Porsche instrument panels or tech screens
    val offset = 10f
    val len = 25f

    // Top-left
    drawLine(Color.White.copy(alpha = 0.3f), Offset(offset, offset), Offset(offset + len, offset), 2f)
    drawLine(Color.White.copy(alpha = 0.3f), Offset(offset, offset), Offset(offset, offset + len), 2f)

    // Bottom-right
    drawLine(Color.White.copy(alpha = 0.3f), Offset(width - offset, height - offset), Offset(width - offset - len, height - offset), 2f)
    drawLine(Color.White.copy(alpha = 0.3f), Offset(width - offset, height - offset), Offset(width - offset, height - offset - len), 2f)
}
