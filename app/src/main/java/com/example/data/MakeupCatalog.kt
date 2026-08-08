package com.example.data

import com.example.model.BeautyChallenge
import com.example.model.CharacterModel
import com.example.model.MakeupCategory
import com.example.model.MakeupFinish
import com.example.model.MakeupOption

object MakeupCatalog {

    val characters = listOf(
        CharacterModel(
            id = "mia",
            name = "Mia",
            personality = "Upbeat & energetic fashionista who loves bright pinks!",
            defaultSkinHex = "#FFE0D0",
            defaultEyeHex = "#1E90FF",
            defaultHairStyle = "SPACE_BUNS",
            defaultHairColorHex = "#FF69B4",
            favoriteColor = "pink"
        ),
        CharacterModel(
            id = "luna",
            name = "Luna",
            personality = "Dreamy fantasy artist who loves mystical purple styles!",
            defaultSkinHex = "#FCE4D6",
            defaultEyeHex = "#8A2BE2",
            defaultHairStyle = "WAVY_LONG",
            defaultHairColorHex = "#9C27B0",
            favoriteColor = "purple"
        ),
        CharacterModel(
            id = "chloe",
            name = "Chloe",
            personality = "Chic party queen loving dramatic liners and bold lips!",
            defaultSkinHex = "#E89D72",
            defaultEyeHex = "#2E8B57",
            defaultHairStyle = "PONYTAIL",
            defaultHairColorHex = "#2B1B17",
            favoriteColor = "black"
        ),
        CharacterModel(
            id = "ava",
            name = "Ava",
            personality = "Sun-kissed surfer girl rocking peach glow & golden hour!",
            defaultSkinHex = "#E2A76F",
            defaultEyeHex = "#DAA520",
            defaultHairStyle = "CURLY_UPDO",
            defaultHairColorHex = "#FFD700",
            favoriteColor = "orange"
        ),
        CharacterModel(
            id = "sophia",
            name = "Sophia",
            personality = "Classic glam diva who loves ruby red lips & elegance!",
            defaultSkinHex = "#FFD1B3",
            defaultEyeHex = "#8B4513",
            defaultHairStyle = "BRAIDED",
            defaultHairColorHex = "#A52A2A",
            favoriteColor = "red"
        )
    )

    val challenges = listOf(
        BeautyChallenge(
            id = "pink_glam",
            title = "Pink Glam Look 💖",
            description = "Create a dazzling pink glam look! Use pink eyeshadow, soft blush, glossy pink lips, and a shiny tiara or bow!",
            targetColors = listOf("pink", "rose", "magenta", "tiara", "bow"),
            preferredCategories = listOf(MakeupCategory.EYESHADOW, MakeupCategory.LIPSTICK, MakeupCategory.BLUSH, MakeupCategory.ACCESSORIES),
            targetStyleVibe = "GLAM"
        ),
        BeautyChallenge(
            id = "natural_look",
            title = "Natural Everyday Look 🌿",
            description = "Create a subtle, elegant natural look! Pick warm peach tones, nude balm, soft mascara, and natural highlights.",
            targetColors = listOf("nude", "peach", "natural", "brown", "balm"),
            preferredCategories = listOf(MakeupCategory.FOUNDATION, MakeupCategory.LIPSTICK, MakeupCategory.MASCARA, MakeupCategory.BLUSH),
            targetStyleVibe = "NATURAL"
        ),
        BeautyChallenge(
            id = "party_night",
            title = "Party Night Look 🎉",
            description = "Create an electric party look! Pick bold eyeshadow, cat eyeliner, glitter lipstick, and space buns or star gems!",
            targetColors = listOf("black", "cat_eye", "glitter", "violet", "star_gems", "space_buns"),
            preferredCategories = listOf(MakeupCategory.EYELINER, MakeupCategory.EYESHADOW, MakeupCategory.LIPSTICK, MakeupCategory.ACCESSORIES),
            targetStyleVibe = "PARTY"
        ),
        BeautyChallenge(
            id = "purple_fantasy",
            title = "Purple Fantasy Look 🦄",
            description = "Create an enchanting purple look! Choose violet eyeshadow, plum lipstick, purple hair, and holographic highlight.",
            targetColors = listOf("purple", "violet", "plum", "holo", "butterfly"),
            preferredCategories = listOf(MakeupCategory.EYESHADOW, MakeupCategory.HAIR, MakeupCategory.LIPSTICK, MakeupCategory.HIGHLIGHTER),
            targetStyleVibe = "FANTASY"
        ),
        BeautyChallenge(
            id = "sunset_goddess",
            title = "Sunset Goddess Look 🌅",
            description = "Create a warm sunset look! Combine golden hour & orange eyeshadow, coral blush, golden highlight, and warm waves.",
            targetColors = listOf("gold", "orange", "coral", "warm", "sunset"),
            preferredCategories = listOf(MakeupCategory.EYESHADOW, MakeupCategory.BLUSH, MakeupCategory.HIGHLIGHTER, MakeupCategory.HAIR),
            targetStyleVibe = "SUNSET"
        )
    )

    val optionsByCategory: Map<MakeupCategory, List<MakeupOption>> = mapOf(
        MakeupCategory.FOUNDATION to listOf(
            MakeupOption("f_porcelain", MakeupCategory.FOUNDATION, "Fair Porcelain", "#FFE0D0", MakeupFinish.MATTE, "PORCELAIN"),
            MakeupOption("f_peach", MakeupCategory.FOUNDATION, "Warm Peach", "#FFD1B3", MakeupFinish.NATURAL, "PEACH"),
            MakeupOption("f_rose", MakeupCategory.FOUNDATION, "Rose Beige", "#FCE4D6", MakeupFinish.NATURAL, "ROSE"),
            MakeupOption("f_bronze", MakeupCategory.FOUNDATION, "Honey Bronze", "#E89D72", MakeupFinish.SHIMMER, "BRONZE"),
            MakeupOption("f_golden", MakeupCategory.FOUNDATION, "Golden Glow", "#E2A76F", MakeupFinish.SHIMMER, "GOLDEN"),
            MakeupOption("f_caramel", MakeupCategory.FOUNDATION, "Deep Caramel", "#A0522D", MakeupFinish.NATURAL, "CARAMEL"),
            MakeupOption("f_espresso", MakeupCategory.FOUNDATION, "Rich Espresso", "#5C3317", MakeupFinish.MATTE, "ESPRESSO")
        ),

        MakeupCategory.BLUSH to listOf(
            MakeupOption("b_none", MakeupCategory.BLUSH, "No Blush", "#00000000", MakeupFinish.NATURAL, "NONE"),
            MakeupOption("b_petal_pink", MakeupCategory.BLUSH, "Soft Petal Pink", "#FFB6C1", MakeupFinish.NATURAL, "PINK"),
            MakeupOption("b_coral", MakeupCategory.BLUSH, "Coral Sunset", "#FF7F50", MakeupFinish.NATURAL, "CORAL"),
            MakeupOption("b_peach", MakeupCategory.BLUSH, "Peach Glow", "#FFDAB9", MakeupFinish.SHIMMER, "PEACH"),
            MakeupOption("b_berry", MakeupCategory.BLUSH, "Berry Crush", "#C71585", MakeupFinish.MATTE, "BERRY"),
            MakeupOption("b_rose", MakeupCategory.BLUSH, "Rosy Radiance", "#E65C00", MakeupFinish.SHIMMER, "ROSE"),
            MakeupOption("b_bronze", MakeupCategory.BLUSH, "Bronze Blush", "#CD7F32", MakeupFinish.SHIMMER, "BRONZE")
        ),

        MakeupCategory.EYESHADOW to listOf(
            MakeupOption("es_none", MakeupCategory.EYESHADOW, "Natural Lids", "#00000000", MakeupFinish.NATURAL, "NONE"),
            MakeupOption("es_pink_glam", MakeupCategory.EYESHADOW, "Romantic Pink", "#FF69B4", MakeupFinish.SHIMMER, "PINK"),
            MakeupOption("es_gold_shimmer", MakeupCategory.EYESHADOW, "Golden Hour", "#FFD700", MakeupFinish.SHIMMER, "GOLD"),
            MakeupOption("es_violet_dream", MakeupCategory.EYESHADOW, "Violet Dream", "#8A2BE2", MakeupFinish.GLITTER, "PURPLE"),
            MakeupOption("es_nude_matte", MakeupCategory.EYESHADOW, "Soft Nude", "#D2B48C", MakeupFinish.MATTE, "NUDE"),
            MakeupOption("es_ocean_blue", MakeupCategory.EYESHADOW, "Ocean Blue", "#00BFFF", MakeupFinish.SHIMMER, "BLUE"),
            MakeupOption("es_sunset_orange", MakeupCategory.EYESHADOW, "Sunset Orange", "#FF4500", MakeupFinish.SHIMMER, "ORANGE"),
            MakeupOption("es_emerald_glitter", MakeupCategory.EYESHADOW, "Emerald Sparkle", "#50C878", MakeupFinish.GLITTER, "GREEN"),
            MakeupOption("es_smokey_black", MakeupCategory.EYESHADOW, "Smokey Drama", "#2F4F4F", MakeupFinish.MATTE, "SMOKEY")
        ),

        MakeupCategory.EYELINER to listOf(
            MakeupOption("el_none", MakeupCategory.EYELINER, "No Eyeliner", "#00000000", MakeupFinish.NATURAL, "NONE"),
            MakeupOption("el_classic_wing", MakeupCategory.EYELINER, "Classic Wing", "#111111", MakeupFinish.MATTE, "WINGED"),
            MakeupOption("el_cat_eye", MakeupCategory.EYELINER, "Dramatic Cat Eye", "#000000", MakeupFinish.MATTE, "CAT_EYE"),
            MakeupOption("el_neon_pink", MakeupCategory.EYELINER, "Neon Pink Pop", "#FF1493", MakeupFinish.MATTE, "WINGED"),
            MakeupOption("el_soft_brown", MakeupCategory.EYELINER, "Soft Brown", "#4A2C11", MakeupFinish.NATURAL, "DEFAULT"),
            MakeupOption("el_graphic_double", MakeupCategory.EYELINER, "Double Graphic", "#8B008B", MakeupFinish.MATTE, "DOUBLE"),
            MakeupOption("el_royal_blue", MakeupCategory.EYELINER, "Royal Blue Wing", "#0000FF", MakeupFinish.MATTE, "CAT_EYE")
        ),

        MakeupCategory.MASCARA to listOf(
            MakeupOption("m_none", MakeupCategory.MASCARA, "Natural Lashes", "#00000000", MakeupFinish.NATURAL, "NONE"),
            MakeupOption("m_volume_black", MakeupCategory.MASCARA, "Volume Jet Black", "#0B0B0B", MakeupFinish.MATTE, "VOLUME"),
            MakeupOption("m_fluttery_long", MakeupCategory.MASCARA, "Fluttery Extra Long", "#1C1C1C", MakeupFinish.MATTE, "FLUTTERY"),
            MakeupOption("m_chocolate_brown", MakeupCategory.MASCARA, "Chocolate Brown", "#3E2723", MakeupFinish.NATURAL, "NATURAL"),
            MakeupOption("m_glitter_tip", MakeupCategory.MASCARA, "Glitter Tip Lash", "#E0E0E0", MakeupFinish.GLITTER, "GLITTER"),
            MakeupOption("m_electric_purple", MakeupCategory.MASCARA, "Electric Purple", "#9C27B0", MakeupFinish.MATTE, "COLOR")
        ),

        MakeupCategory.LIPSTICK to listOf(
            MakeupOption("l_nude_peach", MakeupCategory.LIPSTICK, "Nude Peach Balm", "#FF8C69", MakeupFinish.NATURAL, "NATURAL"),
            MakeupOption("l_glossy_pink", MakeupCategory.LIPSTICK, "Glossy Pink Sugar", "#FF1493", MakeupFinish.GLOSSY, "GLOSS"),
            MakeupOption("l_ruby_red", MakeupCategory.LIPSTICK, "Ruby Red Velvet", "#DC143C", MakeupFinish.MATTE, "RED"),
            MakeupOption("l_plum_wine", MakeupCategory.LIPSTICK, "Deep Plum Wine", "#4A0033", MakeupFinish.MATTE, "PLUM"),
            MakeupOption("l_coral_shine", MakeupCategory.LIPSTICK, "Coral Shine", "#FF7F50", MakeupFinish.GLOSSY, "CORAL"),
            MakeupOption("l_glitter_magenta", MakeupCategory.LIPSTICK, "Glitter Magenta", "#FF00FF", MakeupFinish.GLITTER, "PARTY"),
            MakeupOption("l_matte_violet", MakeupCategory.LIPSTICK, "Matte Violet", "#8B008B", MakeupFinish.MATTE, "PURPLE"),
            MakeupOption("l_clear_gloss", MakeupCategory.LIPSTICK, "Crystal Clear Gloss", "#FFE4E1", MakeupFinish.GLOSSY, "NATURAL")
        ),

        MakeupCategory.HIGHLIGHTER to listOf(
            MakeupOption("h_none", MakeupCategory.HIGHLIGHTER, "No Highlighter", "#00000000", MakeupFinish.NATURAL, "NONE"),
            MakeupOption("h_champagne", MakeupCategory.HIGHLIGHTER, "Champagne Glow", "#FFF8DC", MakeupFinish.SHIMMER, "CHAMPAGNE"),
            MakeupOption("h_pearl", MakeupCategory.HIGHLIGHTER, "Pearl White Shimmer", "#F0F8FF", MakeupFinish.GLITTER, "PEARL"),
            MakeupOption("h_golden_hour", MakeupCategory.HIGHLIGHTER, "Golden Hour", "#FFD700", MakeupFinish.SHIMMER, "GOLD"),
            MakeupOption("h_rose_gold", MakeupCategory.HIGHLIGHTER, "Rose Gold Magic", "#B76E79", MakeupFinish.SHIMMER, "ROSE_GOLD"),
            MakeupOption("h_holo", MakeupCategory.HIGHLIGHTER, "Holographic Sparkle", "#E0FFFF", MakeupFinish.GLITTER, "HOLO")
        ),

        MakeupCategory.HAIR to listOf(
            MakeupOption("hr_space_buns", MakeupCategory.HAIR, "Pastel Space Buns", "#FF69B4", MakeupFinish.NATURAL, "SPACE_BUNS"),
            MakeupOption("hr_wavy_long", MakeupCategory.HAIR, "Lavender Waves", "#9C27B0", MakeupFinish.NATURAL, "WAVY_LONG"),
            MakeupOption("hr_sleek_pony", MakeupCategory.HAIR, "Sleek Dark Ponytail", "#2B1B17", MakeupFinish.NATURAL, "PONYTAIL"),
            MakeupOption("hr_curly_updo", MakeupCategory.HAIR, "Golden Curly Updo", "#DAA520", MakeupFinish.NATURAL, "CURLY_UPDO"),
            MakeupOption("hr_pigtails", MakeupCategory.HAIR, "Neon Anime Pigtails", "#00FFFF", MakeupFinish.NATURAL, "PIGTAILS"),
            MakeupOption("hr_braided", MakeupCategory.HAIR, "Auburn Braided Crown", "#A52A2A", MakeupFinish.NATURAL, "BRAIDED"),
            MakeupOption("hr_cute_bob", MakeupCategory.HAIR, "Honey Blonde Bob", "#F0E68C", MakeupFinish.NATURAL, "BOB")
        ),

        MakeupCategory.ACCESSORIES to listOf(
            MakeupOption("acc_none", MakeupCategory.ACCESSORIES, "No Accessory", "#00000000", MakeupFinish.NATURAL, "NONE"),
            MakeupOption("acc_tiara", MakeupCategory.ACCESSORIES, "Sparkle Tiara 👑", "#FFD700", MakeupFinish.GLITTER, "TIARA"),
            MakeupOption("acc_glasses", MakeupCategory.ACCESSORIES, "Chic Glasses 👓", "#C0C0C0", MakeupFinish.NATURAL, "GLASSES"),
            MakeupOption("acc_flower_crown", MakeupCategory.ACCESSORIES, "Flower Crown 🌸", "#FF69B4", MakeupFinish.NATURAL, "FLOWER_CROWN"),
            MakeupOption("acc_star_gems", MakeupCategory.ACCESSORIES, "Star Face Gems ✨", "#00FFFF", MakeupFinish.GLITTER, "STAR_GEMS"),
            MakeupOption("acc_pearl_earrings", MakeupCategory.ACCESSORIES, "Pearl Earrings 💎", "#FFFFFF", MakeupFinish.SHIMMER, "PEARL_EARRINGS"),
            MakeupOption("acc_bow", MakeupCategory.ACCESSORIES, "Cute Bow Headband 🎀", "#FF1493", MakeupFinish.NATURAL, "BOW"),
            MakeupOption("acc_heart_stickers", MakeupCategory.ACCESSORIES, "Heart Cheek Gems ❤️", "#FF4081", MakeupFinish.GLITTER, "HEART_STICKERS"),
            MakeupOption("acc_butterfly_clips", MakeupCategory.ACCESSORIES, "Butterfly Clips 🦋", "#9C27B0", MakeupFinish.NATURAL, "BUTTERFLY_CLIPS")
        )
    )
}
