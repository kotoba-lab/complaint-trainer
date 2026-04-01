package com.melof.complainttrainer.data

import kotlin.random.Random

object ChoiceOptionFactory {

    fun buildOptions(scenario: Scenario): List<ChoiceOption> {
        val seed = scenario.id.hashCode()
        val random = Random(seed)
        val targets = scenario.targetCategories

        val correctText = listOf(
            scenario.sampleResponse,
            composeFromCategories(targets)
        )
            .map { it.trim() }
            .firstOrNull { isCorrectForScenario(it, scenario) }
            ?: composeFromCategories(targets)

        val distractorTexts = buildDistractorTexts(scenario)
            .distinct()
            .filterNot { it == correctText }
            .filterNot { isCorrectForScenario(it, scenario) }
            .take(3)

        val options = buildList {
            add(ChoiceOption(correctText, true))
            distractorTexts.forEach { add(ChoiceOption(it, false)) }
        }

        // 何らかの理由で候補が不足した場合も、必ず1正解+3不正解を返す。
        val padded = if (options.size < 4) {
            options + fallbackDistractors(scenario)
                .distinct()
                .filter { candidate -> options.none { it.text == candidate } }
                .filterNot { isCorrectForScenario(candidate, scenario) }
                .take(4 - options.size)
                .map { ChoiceOption(it, false) }
        } else {
            options
        }

        return padded
            .shuffled(random)
            .take(4)
    }

    private fun buildDistractorTexts(scenario: Scenario): List<String> {
        val targets = scenario.targetCategories
        val nonTargets = ResponseCategory.entries.filter { it !in targets }
        val missingOne = if (targets.size >= 2) targets.dropLast(1) else emptyList()
        val wrongCategory = nonTargets.firstOrNull()
        val bluntBoundary = if (ResponseCategory.BOUNDARY_SETTING in targets) {
            "規則なのでできません。以上です。"
        } else {
            "それは対応できません。"
        }

        return listOfNotNull(
            missingOne.takeIf { it.isNotEmpty() }?.let { composeFromCategories(it) },
            wrongCategory?.let { composeFromCategories(listOf(it)) },
            "でも、こちらにも事情があります。${confirmationPhrase()}",
            if (ResponseCategory.ORGANIZATION in targets) "担当に伝えておきます。" else null,
            if (ResponseCategory.CONFIRMATION in targets) "お気持ちはわかります。少し様子を見てください。" else null,
            if (ResponseCategory.ACCEPTANCE in targets) "申し訳ありません。確認します。" else null,
            bluntBoundary,
            "ひとまず様子を見てください。"
        )
    }

    private fun fallbackDistractors(scenario: Scenario): List<String> = listOf(
        "申し訳ありませんが、今はわかりません。",
        "お気持ちはわかりますが、できません。",
        "確認しておきます。",
        "でも、それは誤解です。"
    )

    private fun isCorrectForScenario(text: String, scenario: Scenario): Boolean {
        val result = ScoringEngine.evaluate(
            input = text,
            userPhrases = emptyMap(),
            targetCategories = scenario.targetCategories
        )
        return result.requiredAchieved == scenario.targetCategories.size &&
            result.ngWordsFound.isEmpty() &&
            !result.tooLong
    }

    private fun composeFromCategories(categories: List<ResponseCategory>): String {
        return categories
            .distinct()
            .joinToString(separator = "") { phraseFor(it) }
    }

    private fun phraseFor(category: ResponseCategory): String = when (category) {
        ResponseCategory.APOLOGY -> "ご不快な思いをさせてしまい、申し訳ありません。"
        ResponseCategory.ACCEPTANCE -> "そのようなお気持ちを受け止めます。"
        ResponseCategory.CONFIRMATION -> confirmationPhrase()
        ResponseCategory.ORGANIZATION -> "事業所として確認し、改めて対応いたします。"
        ResponseCategory.BOUNDARY_SETTING -> "その件はこの場でお答えできかねます。"
    }

    private fun confirmationPhrase(): String = "詳しい状況を確認させてください。"
}
