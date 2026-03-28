package com.melof.complainttrainer.data

object ScenarioRepository {

    val scenarios = listOf(

        // ── 難易度1：軽い苦情 ──────────────────────────────────────────
        Scenario(
            id = "easy_1",
            difficulty = 1,
            situation = "利用者の家族から電話がありました。先日のスタッフの対応について不満を持っているようです。",
            complaint = "この前のスタッフの言い方、ちょっと失礼じゃなかったですか？",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "謝罪 → 確認への移行 の2段構成で、短く返しましょう"
        ),
        Scenario(
            id = "easy_2",
            difficulty = 1,
            situation = "面会に来た家族が、利用者の最近の様子が気になっているようです。",
            complaint = "最近、母の様子がいつもと違うんですが、何かあったんですか？",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "気にかけている気持ちを受け止め、確認を約束しましょう"
        ),
        Scenario(
            id = "easy_3",
            difficulty = 1,
            situation = "利用者本人が食事の内容について不満を言っています。",
            complaint = "昨日の夕食、また同じメニューだったじゃないですか。いい加減にしてほしい。",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "不快な思いをまず受け止め、確認につなぎましょう"
        ),

        // ── 難易度2：こじれ ───────────────────────────────────────────
        Scenario(
            id = "medium_1",
            difficulty = 2,
            situation = "クレームの電話が続いており、相手の感情が高まっています。",
            complaint = "謝り方も言い訳っぽい。全然わかってないですよね。何回言えばわかるんですか。",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.ORGANIZATION),
            hint = "自己正当化せず、組織対応へ切り替えましょう"
        ),
        Scenario(
            id = "medium_2",
            difficulty = 2,
            situation = "以前の対応に対してまだ怒っている家族が面会に来ました。",
            complaint = "先週も同じことを言いましたよね？なんで改善されないんですか？担当者を変えてください。",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.ORGANIZATION),
            hint = "訂正したくなる気持ちを抑え、組織対応を前面に出しましょう"
        ),
        Scenario(
            id = "medium_3",
            difficulty = 2,
            situation = "利用者の家族が施設に来て、スタッフの態度を強く批判しています。",
            complaint = "あのスタッフはうちの母に冷たい態度をとっている。あなたはわかってないんですか？",
            targetCategories = listOf(
                ResponseCategory.APOLOGY,
                ResponseCategory.ORGANIZATION,
                ResponseCategory.CONFIRMATION
            ),
            hint = "感情を受け止めつつ、組織で対応することを示しましょう"
        ),

        // ── 難易度3：過大要求 ─────────────────────────────────────────
        Scenario(
            id = "hard_1",
            difficulty = 3,
            situation = "家族から電話があり、感情的な人事要求が出てきました。",
            complaint = "そんな人、辞めさせてください。責任者を出してください。今すぐ謝罪文を書いてください。",
            targetCategories = listOf(
                ResponseCategory.APOLOGY,
                ResponseCategory.ORGANIZATION,
                ResponseCategory.BOUNDARY_SETTING
            ),
            hint = "感情は受け止め、人事要求には乗らず、窓口を一本化しましょう"
        ),
        Scenario(
            id = "hard_2",
            difficulty = 3,
            situation = "家族が「ケアプランを全部自分の言う通りに変えてほしい」と要求しています。",
            complaint = "今のケアプランは全部間違っている。私が言った通りに全部変えてください。それだけです。",
            targetCategories = listOf(
                ResponseCategory.APOLOGY,
                ResponseCategory.ORGANIZATION,
                ResponseCategory.BOUNDARY_SETTING
            ),
            hint = "一方的な要求には即答せず、組織で確認・検討する形に持ち込みましょう"
        ),
        Scenario(
            id = "hard_3",
            difficulty = 3,
            situation = "特定スタッフの専属配置を要求されました。",
            complaint = "うちの父には毎日夕方5時から6時の間、必ず同じ人をつけてください。それができないなら施設を変えます。",
            targetCategories = listOf(
                ResponseCategory.APOLOGY,
                ResponseCategory.ORGANIZATION,
                ResponseCategory.BOUNDARY_SETTING
            ),
            hint = "要望は受け止めつつ、個別専属対応の限界を組織対応として伝えましょう"
        )
    )

    fun getById(id: String): Scenario? = scenarios.find { it.id == id }
}
