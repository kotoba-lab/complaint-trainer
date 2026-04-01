package com.melof.complainttrainer.data

object SampleResponseFactory {

    fun buildSamples(scenario: Scenario): List<String> {
        val base = scenario.sampleResponse.trim()
        val variants = listOfNotNull(
            base.takeIf { it.isNotBlank() },
            composeVariant(
                targets = scenario.targetCategories,
                apology = "ご不快な思いをさせてしまい、申し訳ありません。",
                acceptance = "そのようなお気持ちを受け止めます。",
                confirmation = "まず状況を確認させてください。",
                organization = "事業所として確認し、改めて対応いたします。",
                boundary = "その件はこの場でお答えできかねます。"
            ),
            composeVariant(
                targets = scenario.targetCategories,
                apology = "ご迷惑をおかけし、申し訳ありません。",
                acceptance = "ご不満なお気持ちは承っています。",
                confirmation = "経緯を確認したうえでご説明します。",
                organization = "施設として責任を持って対応いたします。",
                boundary = "ご要望すべてに今すぐお答えするのは難しいです。"
            )
        )

        return variants
            .map { it.replace(Regex("\\s+"), " ").trim() }
            .distinct()
            .take(3)
    }

    private fun composeVariant(
        targets: List<ResponseCategory>,
        apology: String,
        acceptance: String,
        confirmation: String,
        organization: String,
        boundary: String,
    ): String {
        return targets.distinct().joinToString(separator = "") { category ->
            when (category) {
                ResponseCategory.APOLOGY -> apology
                ResponseCategory.ACCEPTANCE -> acceptance
                ResponseCategory.CONFIRMATION -> confirmation
                ResponseCategory.ORGANIZATION -> organization
                ResponseCategory.BOUNDARY_SETTING -> boundary
            }
        }
    }
}
