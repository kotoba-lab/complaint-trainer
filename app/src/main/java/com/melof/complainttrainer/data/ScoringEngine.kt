package com.melof.complainttrainer.data

object ScoringEngine {

    // 80文字を超えたら「長すぎる」と判定
    private const val LONG_THRESHOLD = 80

    fun evaluate(input: String): ScoreResult {
        val text = input.replace("　", " ").trim()

        val categoryResults = mapOf(
            ResponseCategory.APOLOGY to
                SynonymDictionary.apologyPhrases.any { text.contains(it) },
            ResponseCategory.CONFIRMATION to
                SynonymDictionary.confirmationPhrases.any { text.contains(it) },
            ResponseCategory.ORGANIZATION to
                SynonymDictionary.organizationPhrases.any { text.contains(it) },
            ResponseCategory.BOUNDARY_SETTING to
                SynonymDictionary.boundaryPhrases.any { text.contains(it) }
        )

        val foundNg = SynonymDictionary.ngWords.filter { text.contains(it) }
        val tooLong = text.length > LONG_THRESHOLD

        return ScoreResult(
            input = input,
            categoryResults = categoryResults,
            ngWordsFound = foundNg,
            tooLong = tooLong
        )
    }
}
