package com.melof.complainttrainer.data

import kotlin.random.Random

object ChoiceOptionFactory {

    fun buildOptions(scenario: Scenario): List<ChoiceOption> {
        val seed = scenario.id.hashCode()
        val random = Random(seed)

        val correct = ChoiceOption(
            text = scenario.sampleResponse.ifBlank { composeFromCategories(scenario.targetCategories) },
            isCorrect = true
        )

        val targetCategories = scenario.targetCategories
        val nonTargets = ResponseCategory.entries.filter { it !in targetCategories }

        val partialCategories = when {
            targetCategories.size >= 2 -> targetCategories.dropLast(1)
            nonTargets.isNotEmpty() -> listOf(nonTargets.first())
            else -> emptyList()
        }

        val candidates = listOf(
            correct,
            ChoiceOption(
                text = composeFromCategories(partialCategories.ifEmpty { listOf(ResponseCategory.CONFIRMATION) }),
                isCorrect = false
            ),
            ChoiceOption(
                text = "でも、こちらにも事情があります。${phraseFor(ResponseCategory.CONFIRMATION)}",
                isCorrect = false
            ),
            ChoiceOption(
                text = when {
                    ResponseCategory.BOUNDARY_SETTING in targetCategories ->
                        "規則なのでできません。以上です。"
                    ResponseCategory.ORGANIZATION in targetCategories ->
                        "担当に伝えておきます。"
                    else ->
                        "ひとまず様子を見てください。"
                },
                isCorrect = false
            ),
            ChoiceOption(
                text = composeFromCategories(listOf(ResponseCategory.ACCEPTANCE)) +
                    phraseFor(nonTargets.firstOrNull() ?: ResponseCategory.CONFIRMATION),
                isCorrect = false
            )
        )

        return candidates
            .distinctBy { it.text }
            .shuffled(random)
            .take(4)
    }

    private fun composeFromCategories(categories: List<ResponseCategory>): String {
        return categories.joinToString(separator = "") { phraseFor(it) }
    }

    private fun phraseFor(category: ResponseCategory): String = when (category) {
        ResponseCategory.APOLOGY -> "ご不快な思いをさせてしまい、申し訳ありません。"
        ResponseCategory.ACCEPTANCE -> "そのようなお気持ちを受け止めます。"
        ResponseCategory.CONFIRMATION -> "詳しい状況を確認させてください。"
        ResponseCategory.ORGANIZATION -> "事業所として確認し、改めて対応いたします。"
        ResponseCategory.BOUNDARY_SETTING -> "その件はこの場でお答えできかねます。"
    }
}
