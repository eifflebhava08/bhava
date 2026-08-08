package com.example.util

import com.example.model.BeautyChallenge
import com.example.model.MakeupCategory
import com.example.model.MakeupOption
import com.example.model.ScoreResult

object ScoringEngine {

    fun evaluateLook(
        currentLook: Map<MakeupCategory, MakeupOption>,
        challenge: BeautyChallenge
    ): ScoreResult {
        var challengeMatchScore = 0
        var completenessScore = 0
        var colorHarmonyScore = 0
        var glamFactorScore = 0

        val feedback = mutableListOf<String>()

        // 1. Completeness (up to 30 pts)
        val selectedCategoriesCount = currentLook.count { (_, option) ->
            option.hexColor != "#00000000" && option.styleVariant != "NONE"
        }

        completenessScore = when {
            selectedCategoriesCount >= 7 -> 30
            selectedCategoriesCount >= 5 -> 25
            selectedCategoriesCount >= 3 -> 18
            else -> 10
        }

        if (selectedCategoriesCount >= 6) {
            feedback.add("✨ Complete Makeover: Styled $selectedCategoriesCount categories for a full glam transform!")
        } else {
            feedback.add("💡 Tip: Style more categories (highlighter, mascara, accessories) for extra completeness points!")
        }

        // 2. Challenge Match (up to 40 pts)
        var matchedKeywordsCount = 0
        val targetKeywords = challenge.targetColors.map { it.lowercase() }

        currentLook.values.forEach { option ->
            val optionText = "${option.name} ${option.styleVariant} ${option.hexColor}".lowercase()
            targetKeywords.forEach { keyword ->
                if (optionText.contains(keyword)) {
                    matchedKeywordsCount++
                }
            }
        }

        // Additional points if preferred categories are styled
        challenge.preferredCategories.forEach { category ->
            if (currentLook.containsKey(category) && currentLook[category]?.styleVariant != "NONE") {
                matchedKeywordsCount++
            }
        }

        challengeMatchScore = (matchedKeywordsCount * 8).coerceIn(15, 40)

        if (challengeMatchScore >= 32) {
            feedback.add("🎯 Challenge Master: Perfect alignment with '${challenge.title}' prompt!")
        } else {
            feedback.add("🎨 Try using more colors/styles matching '${challenge.targetColors.joinToString(", ")}'!")
        }

        // 3. Color Harmony (up to 20 pts)
        val activeColors = currentLook.values
            .filter { it.hexColor != "#00000000" }
            .map { it.hexColor }

        colorHarmonyScore = if (activeColors.size >= 3) {
            20
        } else {
            12
        }
        feedback.add("🌸 Color Balance: Beautiful tone coordination across eyes, cheeks & lips!")

        // 4. Glam & Detail Factor (up to 10 pts)
        val hasGlitterOrShimmer = currentLook.values.any {
            it.finish == com.example.model.MakeupFinish.GLITTER || it.finish == com.example.model.MakeupFinish.SHIMMER
        }
        val hasAccessory = currentLook[MakeupCategory.ACCESSORIES]?.styleVariant != "NONE"

        glamFactorScore = 5 + (if (hasGlitterOrShimmer) 3 else 0) + (if (hasAccessory) 2 else 0)

        if (hasAccessory) {
            feedback.add("👑 Accessory Polish: Added extra flair with fabulous hair accessories!")
        }

        val totalScore = (challengeMatchScore + completenessScore + colorHarmonyScore + glamFactorScore).coerceIn(0, 100)

        val (grade, title) = when {
            totalScore >= 92 -> "S+" to "🌟 Absolute Makeup Masterpiece!"
            totalScore >= 82 -> "A" to "✨ Fabulous Beauty Stylist!"
            totalScore >= 70 -> "B" to "💄 Chic & Stylish Look!"
            else -> "C" to "🌸 Cute Starter Glam!"
        }

        return ScoreResult(
            score = totalScore,
            grade = grade,
            titleMessage = title,
            breakdownFeedback = feedback,
            colorHarmonyScore = colorHarmonyScore,
            challengeMatchScore = challengeMatchScore,
            completenessScore = completenessScore,
            glamFactorScore = glamFactorScore
        )
    }
}
