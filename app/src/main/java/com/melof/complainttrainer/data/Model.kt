package com.melof.complainttrainer.data

enum class ResponseCategory(val label: String) {
    APOLOGY("謝罪"),
    CONFIRMATION("確認への移行"),
    ORGANIZATION("組織対応"),
    BOUNDARY_SETTING("境界設定")
}

data class Scenario(
    val id: String,
    val difficulty: Int, // 1=軽い苦情, 2=こじれ, 3=過大要求
    val situation: String,
    val complaint: String,
    val targetCategories: List<ResponseCategory>,
    val hint: String = ""
)

data class ScoreResult(
    val input: String,
    val categoryResults: Map<ResponseCategory, Boolean>,
    val ngWordsFound: List<String>,
    val tooLong: Boolean
) {
    val totalScore: Int
        get() {
            var s = categoryResults.values.count { it }
            s -= ngWordsFound.size
            if (tooLong) s--
            return s.coerceAtLeast(0)
        }
}
