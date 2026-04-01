package com.melof.complainttrainer.data

enum class ResponseCategory(val label: String) {
    APOLOGY("謝罪"),
    ACCEPTANCE("受け止め"),
    CONFIRMATION("確認への移行"),
    ORGANIZATION("組織対応"),
    BOUNDARY_SETTING("境界設定")
}

enum class ComplaintDifficulty(
    val level: Int,
    val label: String,
    val stars: String,
) {
    BEGINNER(1, "軽い苦情", "★"),
    INTERMEDIATE(2, "こじれ", "★★"),
    ADVANCED(3, "過大要求", "★★★");

    companion object {
        fun fromLevel(level: Int): ComplaintDifficulty? =
            entries.firstOrNull { it.level == level }
    }
}

enum class PracticeMode(
    val label: String,
    val description: String,
    val shortLabel: String,
) {
    CHOICE("選択式", "4択から無理のない返しを選ぶ", "4択"),
    GUIDED("ガイド付き", "ねらいを見ながら自由に返す", "ガイド"),
    FREE("自由回答", "ヒントなしで自分の言葉で返す", "自由"),
}

data class ChoiceOption(
    val text: String,
    val isCorrect: Boolean,
)

data class Scenario(
    val id: String,
    val difficulty: Int, // 1=軽い苦情, 2=こじれ, 3=過大要求
    val situation: String,
    val complaint: String,
    val targetCategories: List<ResponseCategory>,
    val hint: String = "",
    val sampleResponse: String = ""
)

data class ScoreResult(
    val input: String,
    val categoryResults: Map<ResponseCategory, Boolean>,
    val ngWordsFound: List<String>,
    val tooLong: Boolean,
    val targetCategories: List<ResponseCategory> = emptyList()
) {
    /** 必須カテゴリのうち達成した数 */
    val requiredAchieved: Int
        get() = targetCategories.count { categoryResults[it] == true }

    /** 必須達成数からペナルティを引いたスコア */
    val totalScore: Int
        get() {
            var s = requiredAchieved
            s -= ngWordsFound.size
            if (tooLong) s--
            return s.coerceAtLeast(0)
        }
}
