package com.melof.complainttrainer.data

object ScoringEngine {

    // 80文字を超えたら「長すぎる」と判定
    private const val LONG_THRESHOLD = 80

    fun evaluate(
        input: String,
        userPhrases: Map<ResponseCategory, List<String>> = emptyMap(),
        targetCategories: List<ResponseCategory> = emptyList()
    ): ScoreResult {
        val text = input.replace("　", " ").trim()

        fun matches(builtIn: List<String>, category: ResponseCategory): Boolean {
            val custom = userPhrases[category] ?: emptyList()
            return (builtIn + custom).any { text.contains(it) }
        }

        val categoryResults = mapOf(
            ResponseCategory.APOLOGY to
                matches(SynonymDictionary.apologyPhrases, ResponseCategory.APOLOGY),
            ResponseCategory.ACCEPTANCE to
                matches(SynonymDictionary.acceptancePhrases, ResponseCategory.ACCEPTANCE),
            ResponseCategory.CONFIRMATION to
                matches(SynonymDictionary.confirmationPhrases, ResponseCategory.CONFIRMATION),
            ResponseCategory.ORGANIZATION to
                matches(SynonymDictionary.organizationPhrases, ResponseCategory.ORGANIZATION),
            ResponseCategory.BOUNDARY_SETTING to
                matches(SynonymDictionary.boundaryPhrases, ResponseCategory.BOUNDARY_SETTING)
        )

        val foundNg = SynonymDictionary.ngWords.filter { text.contains(it) }
        val tooLong = text.length > LONG_THRESHOLD

        return ScoreResult(
            input = input,
            categoryResults = categoryResults,
            ngWordsFound = foundNg,
            tooLong = tooLong,
            targetCategories = targetCategories
        )
    }
}
