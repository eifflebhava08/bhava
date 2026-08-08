package com.example.model

import androidx.compose.ui.graphics.Color

enum class MakeupCategory(val displayName: String, val iconRes: String) {
    FOUNDATION("Foundation", "✨"),
    BLUSH("Blush", "🌸"),
    EYESHADOW("Eyeshadow", "🎨"),
    EYELINER("Eyeliner", "👁️"),
    MASCARA("Mascara", "✨"),
    LIPSTICK("Lipstick", "💄"),
    HIGHLIGHTER("Highlighter", "💎"),
    HAIR("Hair Styles", "💇‍♀️"),
    ACCESSORIES("Accessories", "👑")
}

enum class MakeupFinish {
    NATURAL, MATTE, SHIMMER, GLOSSY, GLITTER
}

data class MakeupOption(
    val id: String,
    val category: MakeupCategory,
    val name: String,
    val hexColor: String, // ARGB or RGB string e.g. "#FFB6C1"
    val finish: MakeupFinish = MakeupFinish.NATURAL,
    val styleVariant: String = "DEFAULT" // E.g., WINGED, SMOKEY, CAT_EYE, SPACE_BUNS, TIARA, GLASSES, etc.
) {
    val parsedColor: Color
        get() = try {
            val cleanHex = hexColor.replace("#", "")
            if (cleanHex.length == 6) {
                Color(android.graphics.Color.parseColor("#FF$cleanHex"))
            } else {
                Color(android.graphics.Color.parseColor(hexColor))
            }
        } catch (e: Exception) {
            Color.LightGray
        }
}

data class CharacterModel(
    val id: String,
    val name: String,
    val personality: String,
    val defaultSkinHex: String,
    val defaultEyeHex: String,
    val defaultHairStyle: String,
    val defaultHairColorHex: String,
    val favoriteColor: String
)

data class BeautyChallenge(
    val id: String,
    val title: String,
    val description: String,
    val targetColors: List<String>, // Keywords or hexes like "pink", "purple", "nude", "orange"
    val preferredCategories: List<MakeupCategory>,
    val targetStyleVibe: String // "GLAM", "NATURAL", "PARTY", "FANTASY", "SUNSET"
)

data class SavedLook(
    val id: String,
    val characterName: String,
    val challengeTitle: String,
    val score: Int,
    val grade: String,
    val dateString: String,
    val activeOptionsSummary: Map<String, String> // Category Name -> Option Name
)

data class ScoreResult(
    val score: Int,
    val grade: String,
    val titleMessage: String,
    val breakdownFeedback: List<String>,
    val colorHarmonyScore: Int,
    val challengeMatchScore: Int,
    val completenessScore: Int,
    val glamFactorScore: Int
)
