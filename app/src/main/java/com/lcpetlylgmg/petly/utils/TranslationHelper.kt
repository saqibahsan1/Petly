package com.lcpetlylgmg.petly.utils

class TranslationHelper {
    private val translationMap = hashMapOf(
        "Yes" to "Ja",
        "No" to "Nein",
        "Feminine" to "Weiblich",
        "Masculine" to "Männlich",
        "Doesn't Matter" to "Egal",
        "Small" to "Klein",
        "Middle" to "Mittel",
        "Large" to "Groß",
        "Puppy" to "Welpe",
        "Junior" to "Junghund",
        "Adult" to "Erwachsen",
        "Senior" to "Senior",

        )

    private val translationMapEnglish = hashMapOf(
        "Ja" to "Yes",
        "Nein" to "No",
        "Weiblich" to "Feminine",
        "Männlich" to "Masculine",
        "Egal" to "Doesn't Matter",
        "Klein" to "Small",
        "Mittel" to "Middle",
        "Groß" to "Large",
        "Welpe" to "Puppy",
        "Junghund" to "Junior",
        "Erwachsen" to "Adult",
        "Senior" to "Senior"
    )


    fun translateToGerman(englishText: String): String {
        return translationMap[englishText]
            ?: englishText // Get the translation or use the original text if not found
    }

    fun translateToEnglish(germanText: String): String {
        return translationMapEnglish[germanText]
            ?: germanText // Get the translation or use the original text if not found
    }
}
