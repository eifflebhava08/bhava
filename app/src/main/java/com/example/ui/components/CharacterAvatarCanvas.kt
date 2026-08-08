package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.model.CharacterModel
import com.example.model.MakeupCategory
import com.example.model.MakeupFinish
import com.example.model.MakeupOption
import kotlin.random.Random

@Composable
fun CharacterAvatarCanvas(
    character: CharacterModel,
    currentLook: Map<MakeupCategory, MakeupOption>,
    isSparkling: Boolean,
    modifier: Modifier = Modifier
) {
    val sparkleScale = remember { Animatable(0f) }

    LaunchedEffect(isSparkling) {
        if (isSparkling) {
            sparkleScale.snapTo(0.2f)
            sparkleScale.animateTo(1.2f, animationSpec = tween(durationMillis = 500))
            sparkleScale.animateTo(0f, animationSpec = tween(durationMillis = 300))
        }
    }

    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight * 0.48f

            // 1. Background Aura Glow & Ambient Sparkles
            drawBackgroundAura(centerX, centerY)

            // Extract active look choices
            val foundationColor = currentLook[MakeupCategory.FOUNDATION]?.parsedColor
                ?: parseColor(character.defaultSkinHex)

            val blushOption = currentLook[MakeupCategory.BLUSH]
            val eyeshadowOption = currentLook[MakeupCategory.EYESHADOW]
            val eyelinerOption = currentLook[MakeupCategory.EYELINER]
            val mascaraOption = currentLook[MakeupCategory.MASCARA]
            val lipstickOption = currentLook[MakeupCategory.LIPSTICK]
            val highlighterOption = currentLook[MakeupCategory.HIGHLIGHTER]
            val hairOption = currentLook[MakeupCategory.HAIR]
            val accessoryOption = currentLook[MakeupCategory.ACCESSORIES]

            val hairColor = hairOption?.parsedColor
                ?: parseColor(character.defaultHairColorHex)
            val hairStyleVariant = hairOption?.styleVariant ?: character.defaultHairStyle

            // 2. Draw Back Hair (Waves, Pigtails, Space Buns)
            drawHairBack(centerX, centerY, hairStyleVariant, hairColor)

            // 3. Draw Neck & Shoulders
            drawNeckAndShoulders(centerX, centerY, foundationColor)

            // 4. Draw Face Shape (Head, Chin, Cheeks)
            drawFaceShape(centerX, centerY, foundationColor)

            // 5. Draw Ear & Pearl Earrings
            drawEarsAndEarrings(centerX, centerY, foundationColor, accessoryOption)

            // 6. Draw Blush
            if (blushOption != null && blushOption.hexColor != "#00000000") {
                drawBlush(centerX, centerY, blushOption.parsedColor)
            }

            // 7. Draw Highlighter
            if (highlighterOption != null && highlighterOption.hexColor != "#00000000") {
                drawHighlighter(centerX, centerY, highlighterOption.parsedColor, highlighterOption.finish)
            }

            // 8. Draw Eyeshadow
            if (eyeshadowOption != null && eyeshadowOption.hexColor != "#00000000") {
                drawEyeshadow(centerX, centerY, eyeshadowOption.parsedColor, eyeshadowOption.finish)
            }

            // 9. Draw Anime Eyes & Eyebrows
            drawEyesAndEyebrows(centerX, centerY, character)

            // 10. Draw Eyeliner & Mascara
            drawEyelinerAndMascara(centerX, centerY, eyelinerOption, mascaraOption)

            // 11. Draw Nose
            drawNose(centerX, centerY)

            // 12. Draw Lips & Lipstick
            drawLips(centerX, centerY, lipstickOption)

            // 13. Draw Front Hair
            drawHairFront(centerX, centerY, hairStyleVariant, hairColor)

            // 14. Draw Accessories (Glasses, Tiara, Flower Crown, Bow, Face Gems, Heart Stickers)
            if (accessoryOption != null && accessoryOption.styleVariant != "NONE") {
                drawAccessories(centerX, centerY, accessoryOption)
            }

            // 15. Sparkles burst overlay when modified
            if (sparkleScale.value > 0f) {
                drawSparkleParticles(centerX, centerY, sparkleScale.value)
            }
        }
    }
}

private fun DrawScope.drawBackgroundAura(centerX: Float, centerY: Float) {
    val radius = size.width * 0.45f
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFFFFE4EC),
                Color(0xFFF3E5F5),
                Color(0x00FFFFFF)
            ),
            center = Offset(centerX, centerY),
            radius = radius
        ),
        radius = radius,
        center = Offset(centerX, centerY)
    )

    // Ambient floating sparkle dots
    val random = Random(42)
    for (i in 0..12) {
        val sx = centerX + (random.nextFloat() - 0.5f) * size.width * 0.8f
        val sy = centerY + (random.nextFloat() - 0.5f) * size.height * 0.8f
        drawCircle(
            color = Color(0xFFFFD700).copy(alpha = 0.6f),
            radius = 3f + (i % 3) * 2f,
            center = Offset(sx, sy)
        )
    }
}

private fun DrawScope.drawNeckAndShoulders(centerX: Float, centerY: Float, skinColor: Color) {
    val neckWidth = size.width * 0.22f
    val neckTopY = centerY + size.height * 0.18f
    val neckBottomY = centerY + size.height * 0.32f

    // Neck
    val neckPath = Path().apply {
        moveTo(centerX - neckWidth / 2f, neckTopY)
        lineTo(centerX + neckWidth / 2f, neckTopY)
        lineTo(centerX + neckWidth * 0.6f, neckBottomY)
        lineTo(centerX - neckWidth * 0.6f, neckBottomY)
        close()
    }
    drawPath(path = neckPath, color = skinColor)

    // Shoulders Outfit (Cute Salon Top)
    val shoulderPath = Path().apply {
        moveTo(centerX - size.width * 0.45f, size.height)
        quadraticTo(
            centerX - size.width * 0.3f, neckBottomY - 10f,
            centerX - neckWidth * 0.5f, neckBottomY
        )
        lineTo(centerX + neckWidth * 0.5f, neckBottomY)
        quadraticTo(
            centerX + size.width * 0.3f, neckBottomY - 10f,
            centerX + size.width * 0.45f, size.height
        )
        close()
    }
    drawPath(
        path = shoulderPath,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFF4081), Color(0xFF9C27B0))
        )
    )
}

private fun DrawScope.drawFaceShape(centerX: Float, centerY: Float, skinColor: Color) {
    val faceWidth = size.width * 0.46f
    val faceHeight = size.height * 0.44f

    val facePath = Path().apply {
        moveTo(centerX - faceWidth / 2f, centerY - faceHeight * 0.25f)
        // Top forehead curve
        cubicTo(
            centerX - faceWidth / 2f, centerY - faceHeight * 0.6f,
            centerX + faceWidth / 2f, centerY - faceHeight * 0.6f,
            centerX + faceWidth / 2f, centerY - faceHeight * 0.25f
        )
        // Right cheek to chin curve
        cubicTo(
            centerX + faceWidth / 2f, centerY + faceHeight * 0.2f,
            centerX + faceWidth * 0.25f, centerY + faceHeight * 0.5f,
            centerX, centerY + faceHeight * 0.5f
        )
        // Left chin to cheek curve
        cubicTo(
            centerX - faceWidth * 0.25f, centerY + faceHeight * 0.5f,
            centerX - faceWidth / 2f, centerY + faceHeight * 0.2f,
            centerX - faceWidth / 2f, centerY - faceHeight * 0.25f
        )
        close()
    }

    drawPath(path = facePath, color = skinColor)

    // Subtle face outline stroke
    drawPath(
        path = facePath,
        color = Color(0x228B4513),
        style = Stroke(width = 3f)
    )
}

private fun DrawScope.drawEarsAndEarrings(
    centerX: Float,
    centerY: Float,
    skinColor: Color,
    accessoryOption: MakeupOption?
) {
    val earY = centerY + 10f
    val earRadius = size.width * 0.05f

    // Left ear
    drawCircle(color = skinColor, radius = earRadius, center = Offset(centerX - size.width * 0.23f, earY))
    // Right ear
    drawCircle(color = skinColor, radius = earRadius, center = Offset(centerX + size.width * 0.23f, earY))

    if (accessoryOption?.styleVariant == "PEARL_EARRINGS") {
        drawCircle(color = Color.White, radius = 8f, center = Offset(centerX - size.width * 0.24f, earY + 15f))
        drawCircle(color = Color.White, radius = 8f, center = Offset(centerX + size.width * 0.24f, earY + 15f))
    }
}

private fun DrawScope.drawBlush(centerX: Float, centerY: Float, blushColor: Color) {
    val blushY = centerY + size.height * 0.08f
    val blushRadius = size.width * 0.09f
    val blushOpacity = 0.45f

    // Left cheek
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(blushColor.copy(alpha = blushOpacity), Color.Transparent),
            center = Offset(centerX - size.width * 0.14f, blushY),
            radius = blushRadius
        ),
        radius = blushRadius,
        center = Offset(centerX - size.width * 0.14f, blushY)
    )

    // Right cheek
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(blushColor.copy(alpha = blushOpacity), Color.Transparent),
            center = Offset(centerX + size.width * 0.14f, blushY),
            radius = blushRadius
        ),
        radius = blushRadius,
        center = Offset(centerX + size.width * 0.14f, blushY)
    )
}

private fun DrawScope.drawHighlighter(
    centerX: Float,
    centerY: Float,
    highlighterColor: Color,
    finish: MakeupFinish
) {
    val cheekY = centerY + size.height * 0.04f
    val glowRadius = size.width * 0.06f
    val highlightAlpha = if (finish == MakeupFinish.GLITTER) 0.6f else 0.4f

    // Cheekbones glow
    drawCircle(
        color = highlighterColor.copy(alpha = highlightAlpha),
        radius = glowRadius,
        center = Offset(centerX - size.width * 0.15f, cheekY)
    )
    drawCircle(
        color = highlighterColor.copy(alpha = highlightAlpha),
        radius = glowRadius,
        center = Offset(centerX + size.width * 0.15f, cheekY)
    )

    // Nose tip glow
    drawCircle(
        color = Color.White.copy(alpha = 0.7f),
        radius = 5f,
        center = Offset(centerX, centerY + size.height * 0.08f)
    )
}

private fun DrawScope.drawEyeshadow(
    centerX: Float,
    centerY: Float,
    eyeshadowColor: Color,
    finish: MakeupFinish
) {
    val eyeY = centerY - size.height * 0.02f
    val leftEyeX = centerX - size.width * 0.11f
    val rightEyeX = centerX + size.width * 0.11f
    val shadowWidth = size.width * 0.12f
    val shadowHeight = size.height * 0.06f

    val alpha = if (finish == MakeupFinish.GLITTER) 0.75f else 0.55f

    // Left eyeshadow wing arc
    drawArc(
        color = eyeshadowColor.copy(alpha = alpha),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(leftEyeX - shadowWidth / 2f, eyeY - shadowHeight * 0.8f),
        size = Size(shadowWidth, shadowHeight)
    )

    // Right eyeshadow wing arc
    drawArc(
        color = eyeshadowColor.copy(alpha = alpha),
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = true,
        topLeft = Offset(rightEyeX - shadowWidth / 2f, eyeY - shadowHeight * 0.8f),
        size = Size(shadowWidth, shadowHeight)
    )

    if (finish == MakeupFinish.GLITTER) {
        // Glitter dots over eyelids
        drawCircle(color = Color.White, radius = 3f, center = Offset(leftEyeX - 10f, eyeY - 15f))
        drawCircle(color = Color.White, radius = 3f, center = Offset(rightEyeX + 10f, eyeY - 15f))
        drawCircle(color = Color(0xFFFFD700), radius = 2.5f, center = Offset(leftEyeX + 5f, eyeY - 20f))
        drawCircle(color = Color(0xFFFFD700), radius = 2.5f, center = Offset(rightEyeX - 5f, eyeY - 20f))
    }
}

private fun DrawScope.drawEyesAndEyebrows(centerX: Float, centerY: Float, character: CharacterModel) {
    val eyeY = centerY - size.height * 0.01f
    val leftEyeX = centerX - size.width * 0.11f
    val rightEyeX = centerX + size.width * 0.11f

    val eyeWidth = size.width * 0.09f
    val eyeHeight = size.height * 0.07f

    val irisColor = parseColor(character.defaultEyeHex)

    // Draw Eyebrows
    val eyebrowY = eyeY - eyeHeight * 0.9f
    val eyebrowPathLeft = Path().apply {
        moveTo(leftEyeX - eyeWidth * 0.6f, eyebrowY + 5f)
        quadraticTo(leftEyeX, eyebrowY - 8f, leftEyeX + eyeWidth * 0.6f, eyebrowY + 2f)
    }
    val eyebrowPathRight = Path().apply {
        moveTo(rightEyeX - eyeWidth * 0.6f, eyebrowY + 2f)
        quadraticTo(rightEyeX, eyebrowY - 8f, rightEyeX + eyeWidth * 0.6f, eyebrowY + 5f)
    }

    drawPath(path = eyebrowPathLeft, color = Color(0xFF3E2723), style = Stroke(width = 4f, cap = StrokeCap.Round))
    drawPath(path = eyebrowPathRight, color = Color(0xFF3E2723), style = Stroke(width = 4f, cap = StrokeCap.Round))

    // Eye whites
    drawOval(color = Color.White, topLeft = Offset(leftEyeX - eyeWidth / 2f, eyeY - eyeHeight / 2f), size = Size(eyeWidth, eyeHeight))
    drawOval(color = Color.White, topLeft = Offset(rightEyeX - eyeWidth / 2f, eyeY - eyeHeight / 2f), size = Size(eyeWidth, eyeHeight))

    // Irises
    val irisSize = Size(eyeWidth * 0.65f, eyeHeight * 0.8f)
    drawOval(
        brush = Brush.verticalGradient(listOf(irisColor, Color(0xFF111111))),
        topLeft = Offset(leftEyeX - irisSize.width / 2f, eyeY - irisSize.height / 2f),
        size = irisSize
    )
    drawOval(
        brush = Brush.verticalGradient(listOf(irisColor, Color(0xFF111111))),
        topLeft = Offset(rightEyeX - irisSize.width / 2f, eyeY - irisSize.height / 2f),
        size = irisSize
    )

    // Pupils
    drawCircle(color = Color.Black, radius = irisSize.width * 0.25f, center = Offset(leftEyeX, eyeY))
    drawCircle(color = Color.Black, radius = irisSize.width * 0.25f, center = Offset(rightEyeX, eyeY))

    // Shiny Eye Highlights (Anime Sparkle glint)
    drawCircle(color = Color.White, radius = 4.5f, center = Offset(leftEyeX - 4f, eyeY - 4f))
    drawCircle(color = Color.White, radius = 4.5f, center = Offset(rightEyeX - 4f, eyeY - 4f))
    drawCircle(color = Color.White, radius = 2.5f, center = Offset(leftEyeX + 3f, eyeY + 3f))
    drawCircle(color = Color.White, radius = 2.5f, center = Offset(rightEyeX + 3f, eyeY + 3f))
}

private fun DrawScope.drawEyelinerAndMascara(
    centerX: Float,
    centerY: Float,
    eyelinerOption: MakeupOption?,
    mascaraOption: MakeupOption?
) {
    val eyeY = centerY - size.height * 0.01f
    val leftEyeX = centerX - size.width * 0.11f
    val rightEyeX = centerX + size.width * 0.11f
    val eyeWidth = size.width * 0.09f
    val eyeHeight = size.height * 0.07f

    val eyelinerColor = eyelinerOption?.parsedColor ?: Color(0xFF222222)
    val hasEyeliner = eyelinerOption != null && eyelinerOption.hexColor != "#00000000"

    val mascaraColor = mascaraOption?.parsedColor ?: Color(0xFF111111)
    val hasMascara = mascaraOption != null && mascaraOption.hexColor != "#00000000"

    val linerStyle = eyelinerOption?.styleVariant ?: "NONE"

    if (hasEyeliner) {
        val topLinerLeft = Path().apply {
            moveTo(leftEyeX - eyeWidth * 0.6f, eyeY - eyeHeight * 0.1f)
            quadraticTo(leftEyeX, eyeY - eyeHeight * 0.6f, leftEyeX + eyeWidth * 0.6f, eyeY - eyeHeight * 0.2f)
            if (linerStyle == "WINGED" || linerStyle == "CAT_EYE") {
                lineTo(leftEyeX - eyeWidth * 0.85f, eyeY - eyeHeight * 0.65f)
            }
        }

        val topLinerRight = Path().apply {
            moveTo(rightEyeX - eyeWidth * 0.6f, eyeY - eyeHeight * 0.2f)
            quadraticTo(rightEyeX, eyeY - eyeHeight * 0.6f, rightEyeX + eyeWidth * 0.6f, eyeY - eyeHeight * 0.1f)
            if (linerStyle == "WINGED" || linerStyle == "CAT_EYE") {
                lineTo(rightEyeX + eyeWidth * 0.85f, eyeY - eyeHeight * 0.65f)
            }
        }

        drawPath(path = topLinerLeft, color = eyelinerColor, style = Stroke(width = 5f, cap = StrokeCap.Round))
        drawPath(path = topLinerRight, color = eyelinerColor, style = Stroke(width = 5f, cap = StrokeCap.Round))
    }

    if (hasMascara) {
        // Fluttery Eyelashes
        val lashCount = if (mascaraOption?.styleVariant == "VOLUME") 5 else 4
        for (i in 0 until lashCount) {
            val angle = -120f + i * 20f
            val rad = Math.toRadians(angle.toDouble())
            val lashLen = 14f

            // Left eye lashes
            val lx1 = leftEyeX + (i - 2) * 5f
            val ly1 = eyeY - eyeHeight * 0.45f
            val lx2 = lx1 + (Math.cos(rad) * lashLen).toFloat()
            val ly2 = ly1 + (Math.sin(rad) * lashLen).toFloat()
            drawLine(color = mascaraColor, start = Offset(lx1, ly1), end = Offset(lx2, ly2), strokeWidth = 3.5f)

            // Right eye lashes
            val rx1 = rightEyeX + (i - 2) * 5f
            val ry1 = eyeY - eyeHeight * 0.45f
            val rx2 = rx1 - (Math.cos(rad) * lashLen).toFloat()
            val ry2 = ry1 + (Math.sin(rad) * lashLen).toFloat()
            drawLine(color = mascaraColor, start = Offset(rx1, ry1), end = Offset(rx2, ry2), strokeWidth = 3.5f)
        }
    }
}

private fun DrawScope.drawNose(centerX: Float, centerY: Float) {
    val noseY = centerY + size.height * 0.08f
    val nosePath = Path().apply {
        moveTo(centerX - 4f, noseY)
        quadraticTo(centerX, noseY + 6f, centerX + 4f, noseY)
    }
    drawPath(path = nosePath, color = Color(0x668B4513), style = Stroke(width = 3f, cap = StrokeCap.Round))
}

private fun DrawScope.drawLips(
    centerX: Float,
    centerY: Float,
    lipstickOption: MakeupOption?
) {
    val lipY = centerY + size.height * 0.16f
    val lipWidth = size.width * 0.15f
    val lipHeight = size.height * 0.06f

    val lipColor = lipstickOption?.parsedColor ?: Color(0xFFFFB6C1)
    val finish = lipstickOption?.finish ?: MakeupFinish.NATURAL

    // Upper Lip
    val upperLip = Path().apply {
        moveTo(centerX - lipWidth / 2f, lipY)
        quadraticTo(centerX - lipWidth / 4f, lipY - lipHeight / 2f, centerX, lipY - lipHeight / 4f)
        quadraticTo(centerX + lipWidth / 4f, lipY - lipHeight / 2f, centerX + lipWidth / 2f, lipY)
        quadraticTo(centerX, lipY + lipHeight * 0.1f, centerX - lipWidth / 2f, lipY)
        close()
    }

    // Lower Lip
    val lowerLip = Path().apply {
        moveTo(centerX - lipWidth / 2f, lipY)
        quadraticTo(centerX, lipY + lipHeight * 0.8f, centerX + lipWidth / 2f, lipY)
        quadraticTo(centerX, lipY + lipHeight * 0.1f, centerX - lipWidth / 2f, lipY)
        close()
    }

    drawPath(path = upperLip, color = lipColor)
    drawPath(path = lowerLip, color = lipColor)

    // Lip highlight gloss
    if (finish == MakeupFinish.GLOSSY || finish == MakeupFinish.GLITTER) {
        drawOval(
            color = Color.White.copy(alpha = 0.6f),
            topLeft = Offset(centerX - lipWidth * 0.2f, lipY + 3f),
            size = Size(lipWidth * 0.4f, lipHeight * 0.3f)
        )
    }
}

private fun DrawScope.drawHairBack(
    centerX: Float,
    centerY: Float,
    hairStyleVariant: String,
    hairColor: Color
) {
    val headTopY = centerY - size.height * 0.25f

    when (hairStyleVariant) {
        "WAVY_LONG", "CURLY_UPDO" -> {
            // Long flowing back hair
            val backHairPath = Path().apply {
                moveTo(centerX - size.width * 0.32f, headTopY)
                quadraticTo(centerX - size.width * 0.42f, centerY + size.height * 0.2f, centerX - size.width * 0.25f, size.height * 0.85f)
                lineTo(centerX + size.width * 0.25f, size.height * 0.85f)
                quadraticTo(centerX + size.width * 0.42f, centerY + size.height * 0.2f, centerX + size.width * 0.32f, headTopY)
                close()
            }
            drawPath(path = backHairPath, color = hairColor)
        }
        "SPACE_BUNS" -> {
            // Back space buns circles
            drawCircle(color = hairColor, radius = size.width * 0.12f, center = Offset(centerX - size.width * 0.26f, headTopY - 10f))
            drawCircle(color = hairColor, radius = size.width * 0.12f, center = Offset(centerX + size.width * 0.26f, headTopY - 10f))
        }
        "PIGTAILS" -> {
            // Twin pigtails
            val leftPigtail = Path().apply {
                moveTo(centerX - size.width * 0.24f, headTopY + 20f)
                cubicTo(
                    centerX - size.width * 0.45f, centerY,
                    centerX - size.width * 0.4f, size.height * 0.75f,
                    centerX - size.width * 0.2f, size.height * 0.8f
                )
            }
            val rightPigtail = Path().apply {
                moveTo(centerX + size.width * 0.24f, headTopY + 20f)
                cubicTo(
                    centerX + size.width * 0.45f, centerY,
                    centerX + size.width * 0.4f, size.height * 0.75f,
                    centerX + size.width * 0.2f, size.height * 0.8f
                )
            }
            drawPath(path = leftPigtail, color = hairColor, style = Stroke(width = size.width * 0.14f, cap = StrokeCap.Round))
            drawPath(path = rightPigtail, color = hairColor, style = Stroke(width = size.width * 0.14f, cap = StrokeCap.Round))
        }
    }
}

private fun DrawScope.drawHairFront(
    centerX: Float,
    centerY: Float,
    hairStyleVariant: String,
    hairColor: Color
) {
    val headTopY = centerY - size.height * 0.25f
    val faceWidth = size.width * 0.46f

    // Top hair volume dome
    val topHairDome = Path().apply {
        moveTo(centerX - faceWidth * 0.55f, centerY - size.height * 0.1f)
        cubicTo(
            centerX - faceWidth * 0.6f, headTopY - size.height * 0.12f,
            centerX + faceWidth * 0.6f, headTopY - size.height * 0.12f,
            centerX + faceWidth * 0.55f, centerY - size.height * 0.1f
        )
        cubicTo(
            centerX + faceWidth * 0.3f, headTopY - 20f,
            centerX - faceWidth * 0.3f, headTopY - 20f,
            centerX - faceWidth * 0.55f, centerY - size.height * 0.1f
        )
        close()
    }
    drawPath(path = topHairDome, color = hairColor)

    // Front Bangs / Strands
    val bangsPath = Path().apply {
        moveTo(centerX - faceWidth * 0.5f, headTopY)
        quadraticTo(centerX - faceWidth * 0.2f, headTopY + 35f, centerX, headTopY + 10f)
        quadraticTo(centerX + faceWidth * 0.2f, headTopY + 35f, centerX + faceWidth * 0.5f, headTopY)
        quadraticTo(centerX, headTopY - 15f, centerX - faceWidth * 0.5f, headTopY)
        close()
    }
    drawPath(path = bangsPath, color = hairColor)

    // Side frame strands
    drawArc(
        color = hairColor,
        startAngle = 140f,
        sweepAngle = 70f,
        useCenter = false,
        topLeft = Offset(centerX - faceWidth * 0.6f, headTopY),
        size = Size(faceWidth * 0.3f, size.height * 0.3f),
        style = Stroke(width = 18f, cap = StrokeCap.Round)
    )
    drawArc(
        color = hairColor,
        startAngle = 330f,
        sweepAngle = 70f,
        useCenter = false,
        topLeft = Offset(centerX + faceWidth * 0.3f, headTopY),
        size = Size(faceWidth * 0.3f, size.height * 0.3f),
        style = Stroke(width = 18f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawAccessories(
    centerX: Float,
    centerY: Float,
    accessoryOption: MakeupOption
) {
    val headTopY = centerY - size.height * 0.25f

    when (accessoryOption.styleVariant) {
        "TIARA" -> {
            val tiaraY = headTopY - 10f
            val tiaraPath = Path().apply {
                moveTo(centerX - 40f, tiaraY + 10f)
                lineTo(centerX - 20f, tiaraY - 25f)
                lineTo(centerX, tiaraY - 40f)
                lineTo(centerX + 20f, tiaraY - 25f)
                lineTo(centerX + 40f, tiaraY + 10f)
                close()
            }
            drawPath(path = tiaraPath, color = Color(0xFFFFD700))
            drawCircle(color = Color.Cyan, radius = 5f, center = Offset(centerX, tiaraY - 25f))
            drawCircle(color = Color(0xFFFF4081), radius = 4f, center = Offset(centerX - 20f, tiaraY - 12f))
            drawCircle(color = Color(0xFFFF4081), radius = 4f, center = Offset(centerX + 20f, tiaraY - 12f))
        }

        "GLASSES" -> {
            val eyeY = centerY - size.height * 0.01f
            val glassWidth = size.width * 0.16f
            val glassHeight = size.height * 0.08f

            val leftCenter = Offset(centerX - size.width * 0.11f, eyeY)
            val rightCenter = Offset(centerX + size.width * 0.11f, eyeY)

            // Glasses frame circles
            drawCircle(color = Color(0x33FFFFFF), radius = glassWidth / 2f, center = leftCenter)
            drawCircle(color = Color(0x33FFFFFF), radius = glassWidth / 2f, center = rightCenter)

            drawCircle(color = Color(0xFFC0C0C0), radius = glassWidth / 2f, center = leftCenter, style = Stroke(width = 5f))
            drawCircle(color = Color(0xFFC0C0C0), radius = glassWidth / 2f, center = rightCenter, style = Stroke(width = 5f))

            // Bridge line
            drawLine(color = Color(0xFFC0C0C0), start = Offset(leftCenter.x + glassWidth / 2f, eyeY), end = Offset(rightCenter.x - glassWidth / 2f, eyeY), strokeWidth = 5f)
        }

        "BOW" -> {
            val bowY = headTopY - 15f
            val bowColor = accessoryOption.parsedColor
            // Left bow wing
            val leftWing = Path().apply {
                moveTo(centerX, bowY)
                lineTo(centerX - 35f, bowY - 20f)
                lineTo(centerX - 35f, bowY + 20f)
                close()
            }
            // Right bow wing
            val rightWing = Path().apply {
                moveTo(centerX, bowY)
                lineTo(centerX + 35f, bowY - 20f)
                lineTo(centerX + 35f, bowY + 20f)
                close()
            }
            drawPath(path = leftWing, color = bowColor)
            drawPath(path = rightWing, color = bowColor)
            drawCircle(color = Color.White, radius = 8f, center = Offset(centerX, bowY))
        }

        "FLOWER_CROWN" -> {
            val crownY = headTopY
            val flowerColors = listOf(Color(0xFFFF4081), Color(0xFFFFD700), Color(0xFF9C27B0), Color(0xFFFF80AB))
            for (i in -2..2) {
                val fx = centerX + i * 22f
                val fy = crownY + Math.abs(i) * 3f
                val color = flowerColors[(i + 2) % flowerColors.size]
                drawCircle(color = color, radius = 10f, center = Offset(fx, fy))
                drawCircle(color = Color.Yellow, radius = 3.5f, center = Offset(fx, fy))
            }
        }

        "STAR_GEMS" -> {
            val eyeY = centerY - size.height * 0.01f
            val gemColor = Color(0xFF00FFFF)
            drawCircle(color = gemColor, radius = 4f, center = Offset(centerX - size.width * 0.18f, eyeY - 10f))
            drawCircle(color = gemColor, radius = 4f, center = Offset(centerX + size.width * 0.18f, eyeY - 10f))
            drawCircle(color = Color.White, radius = 3f, center = Offset(centerX - size.width * 0.20f, eyeY + 5f))
            drawCircle(color = Color.White, radius = 3f, center = Offset(centerX + size.width * 0.20f, eyeY + 5f))
        }

        "HEART_STICKERS" -> {
            val cheekY = centerY + size.height * 0.08f
            val heartColor = Color(0xFFFF4081)
            drawCircle(color = heartColor, radius = 6f, center = Offset(centerX - size.width * 0.18f, cheekY))
            drawCircle(color = heartColor, radius = 6f, center = Offset(centerX + size.width * 0.18f, cheekY))
        }
    }
}

private fun DrawScope.drawSparkleParticles(centerX: Float, centerY: Float, scale: Float) {
    val particleCount = 10
    val radius = size.width * 0.35f * scale

    for (i in 0 until particleCount) {
        val angle = (i * 36f)
        val rad = Math.toRadians(angle.toDouble())
        val px = centerX + (Math.cos(rad) * radius).toFloat()
        val py = centerY + (Math.sin(rad) * radius).toFloat()

        // Draw star sparkle shape
        drawCircle(color = Color(0xFFFFD700), radius = 5f * scale, center = Offset(px, py))
        drawCircle(color = Color.White, radius = 2.5f * scale, center = Offset(px, py))
    }
}

private fun parseColor(hex: String): Color {
    return try {
        val cleanHex = hex.replace("#", "")
        if (cleanHex.length == 6) {
            Color(android.graphics.Color.parseColor("#FF$cleanHex"))
        } else {
            Color(android.graphics.Color.parseColor(hex))
        }
    } catch (e: Exception) {
        Color.LightGray
    }
}
