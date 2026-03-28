package com.melof.complainttrainer.data

object ScenarioRepository {

    val scenarios = listOf(

        // ── 難易度1：軽い苦情 ──────────────────────────────────────────
        Scenario(
            id = "case_002",
            difficulty = 1,
            situation = "利用者がナースコールを押したが、職員がなかなか来なかった場面です。",
            complaint = "さっきから呼んでるのに、全然来てくれないじゃない",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "謝罪 → 確認 の2文で返しましょう。言い訳は不要",
            sampleResponse = "お呼びに気づかず、大変申し訳ありませんでした。何かご不便がございましたか、確認させてください。"
        ),
        Scenario(
            id = "case_003",
            difficulty = 1,
            situation = "利用者が職員の声かけの仕方に不快感を示しています。",
            complaint = "さっきの言い方、ちょっときつくなかった？",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.ACCEPTANCE),
            hint = "感じさせてしまったことをまず受け止め、謝罪に繋げましょう",
            sampleResponse = "そのように感じさせてしまい、申し訳ありませんでした。おっしゃることを受け止めます。"
        ),
        Scenario(
            id = "case_004",
            difficulty = 1,
            situation = "利用者が自分の私物が動かされていたことに気づき、不満を言っています。",
            complaint = "私の上着、勝手に場所変えましたよね？",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "不安な気持ちへの謝罪 → 保管場所の確認 の流れで",
            sampleResponse = "ご不安をおかけし、申し訳ありません。上着の保管場所をすぐに確認いたします。"
        ),
        Scenario(
            id = "case_005",
            difficulty = 1,
            situation = "利用者の家族から電話があり、請求書がまだ届いていないと言っています。",
            complaint = "今月の請求書、まだ届いていませんけど",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "ご不便をおかけしたことへの謝罪 → 発送状況を確認する約束",
            sampleResponse = "ご不便をおかけし、申し訳ありません。発送状況を確認して、改めてご連絡いたします。"
        ),
        Scenario(
            id = "case_006",
            difficulty = 1,
            situation = "利用者が入浴の順番が最後だったことに不満を持っています。",
            complaint = "なんで今日は私が最後なの？",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.CONFIRMATION),
            hint = "不満な気持ちを受け止め、今日の流れを確認しましょう",
            sampleResponse = "最後になってしまったお気持ちを受け止めます。今日の順番の経緯を確認させてください。"
        ),
        Scenario(
            id = "easy_1",
            difficulty = 1,
            situation = "利用者の家族から電話がありました。先日のスタッフの対応について不満を持っているようです。",
            complaint = "この前のスタッフの言い方、ちょっと失礼じゃなかったですか？",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "謝罪 → 確認への移行 の2段構成で、短く返しましょう",
            sampleResponse = "ご不快な思いをさせてしまい、申し訳ありません。詳しい状況を確認させてください。"
        ),
        Scenario(
            id = "easy_2",
            difficulty = 1,
            situation = "面会に来た家族が、利用者の最近の様子が気になっているようです。",
            complaint = "最近、母の様子がいつもと違うんですが、何かあったんですか？",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "気にかけている気持ちを受け止め、確認を約束しましょう",
            sampleResponse = "ご心配をおかけし申し訳ありません。最近の様子を確認して、改めてお伝えします。"
        ),
        Scenario(
            id = "easy_3",
            difficulty = 1,
            situation = "利用者本人が食事の内容について不満を言っています。",
            complaint = "昨日の夕食、また同じメニューだったじゃないですか。いい加減にしてほしい。",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "不快な思いをまず受け止め、確認につなぎましょう",
            sampleResponse = "同じメニューが続いてしまい、申し訳ありません。献立の状況を確認させてください。"
        ),

        // ── 難易度2：こじれ ───────────────────────────────────────────
        Scenario(
            id = "case_010",
            difficulty = 2,
            situation = "家族が、予定変更について事前に説明がなかったと怒っています。",
            complaint = "何も聞いていません。どうして事前に説明がないんですか",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION),
            hint = "説明不足だった点を謝罪し、どこで共有できていなかったか確認を約束しましょう",
            sampleResponse = "事前にご説明できなかった点、大変申し訳ありません。どこで共有が止まっていたか確認いたします。"
        ),
        Scenario(
            id = "case_011",
            difficulty = 2,
            situation = "家族が、職員に不適切な発言をされたと強く訴えています。",
            complaint = "見た目が怖いって言われたんですけど",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.CONFIRMATION, ResponseCategory.ORGANIZATION),
            hint = "傷つけた点を謝罪し、事業所として確認する姿勢を示しましょう",
            sampleResponse = "そのような発言で傷つけてしまい、大変申し訳ありません。事業所として事実を確認し、改めてご報告いたします。"
        ),
        Scenario(
            id = "case_012",
            difficulty = 2,
            situation = "電話で家族が感情的になっており、やり取りが堂々巡りになっています。",
            complaint = "あなたじゃ話にならない。何回同じ説明するんですか",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.ORGANIZATION),
            hint = "訂正・反論せず、組織対応に切り替えましょう",
            sampleResponse = "ご不満が強いこと、受け止めます。事業所として担当者を立て、改めて対応いたします。"
        ),
        Scenario(
            id = "medium_1",
            difficulty = 2,
            situation = "クレームの電話が続いており、相手の感情が高まっています。",
            complaint = "謝り方も言い訳っぽい。全然わかってないですよね。何回言えばわかるんですか。",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.ORGANIZATION),
            hint = "自己正当化せず、組織対応へ切り替えましょう",
            sampleResponse = "伝わらず、大変申し訳ありません。事業所として責任者から改めてご説明いたします。"
        ),
        Scenario(
            id = "medium_2",
            difficulty = 2,
            situation = "以前の対応に対してまだ怒っている家族が面会に来ました。",
            complaint = "先週も同じことを言いましたよね？なんで改善されないんですか？担当者を変えてください。",
            targetCategories = listOf(ResponseCategory.APOLOGY, ResponseCategory.ORGANIZATION),
            hint = "訂正したくなる気持ちを抑え、組織対応を前面に出しましょう",
            sampleResponse = "改善できておらず、大変申し訳ありません。事業所として担当者を立て、改めて対応いたします。"
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
            hint = "感情を受け止めつつ、組織で対応することを示しましょう",
            sampleResponse = "ご不快をおかけし、申し訳ありません。事業所として状況を確認し、対応いたします。"
        ),

        // ── 難易度3：過大要求 ─────────────────────────────────────────
        Scenario(
            id = "case_014",
            difficulty = 3,
            situation = "興奮した家族が、施設長をすぐに電話に出すよう要求しています。",
            complaint = "今すぐ施設長を電話に出してください",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.ORGANIZATION, ResponseCategory.BOUNDARY_SETTING),
            hint = "お怒りを受け止め、責任者対応は手順を確認してからお伝えする形にしましょう",
            sampleResponse = "強いご不満を受け止めます。責任者への取り次ぎは確認の上ご連絡いたします。少々お待ちいただけますか。"
        ),
        Scenario(
            id = "case_015",
            difficulty = 3,
            situation = "相手がSNSへの拡散を示唆して圧力をかけています。",
            complaint = "このままならSNSに全部書きますから",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.ORGANIZATION),
            hint = "脅しに反応せず、強いご不満を受け止めて事業所対応へ移行しましょう",
            sampleResponse = "強いご不満があること、受け止めます。事業所として対応いたしますので、改めてお時間をいただけますか。"
        ),
        Scenario(
            id = "case_016",
            difficulty = 3,
            situation = "相手が週刊誌・行政への通報を示唆して圧力をかけています。",
            complaint = "週刊誌にも行政にも全部言います",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.CONFIRMATION, ResponseCategory.ORGANIZATION),
            hint = "外部通報の示唆には直接反応せず、不信感を受け止めて事実確認・組織対応へ",
            sampleResponse = "ご不信感を受け止めます。事実を確認した上で、事業所として改めてご説明いたします。"
        ),
        Scenario(
            id = "case_017",
            difficulty = 3,
            situation = "苦情が金銭要求に発展しました。",
            complaint = "こんな対応をされたんだから、慰謝料を払ってください",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.BOUNDARY_SETTING, ResponseCategory.ORGANIZATION),
            hint = "強いご不満は受け止め、金銭要求にはこの場でお答えできないと境界を示しましょう",
            sampleResponse = "強いご不満を受け止めます。金銭的な件は今すぐお答えできかねます。事業所として確認いたします。"
        ),
        Scenario(
            id = "case_018",
            difficulty = 3,
            situation = "家族が施設内で居座り始め、その場を離れようとしません。",
            complaint = "納得いくまでここを動きません",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.BOUNDARY_SETTING, ResponseCategory.ORGANIZATION),
            hint = "納得できない気持ちを受け止め、事業所の手順で対応することを示しましょう",
            sampleResponse = "納得できないお気持ちを受け止めます。この場での対応は難しい状況です。改めて担当者が対応いたします。"
        ),
        Scenario(
            id = "case_019",
            difficulty = 3,
            situation = "相手が特定の職員への不信感を強め、別の人に代わるよう要求しています。",
            complaint = "あなたでは話にならない。別の人に代わって",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.ORGANIZATION),
            hint = "個人を守ろうとせず、組織として引き継ぐことを伝えましょう",
            sampleResponse = "ご不満を受け止めます。事業所として別の担当者が改めて対応いたします。"
        ),
        Scenario(
            id = "case_020",
            difficulty = 3,
            situation = "相手が「謝るだけでなく責任をとれ」と繰り返し圧をかけています。",
            complaint = "だから謝るだけじゃなくて、どう責任とるのか聞いてるんですよ",
            targetCategories = listOf(ResponseCategory.ACCEPTANCE, ResponseCategory.BOUNDARY_SETTING, ResponseCategory.ORGANIZATION),
            hint = "納得できない気持ちを承り、確認できる内容は確認して組織として説明する形に持ち込みましょう",
            sampleResponse = "納得できないお気持ちを承ります。責任の取り方はこの場ではお答えできかねます。事業所として確認いたします。"
        ),
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
            hint = "感情は受け止め、人事要求には乗らず、窓口を一本化しましょう",
            sampleResponse = "ご不快をおかけし申し訳ありません。人事についてはお答えできかねますが、事業所として窓口を立てます。"
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
            hint = "一方的な要求には即答せず、組織で確認・検討する形に持ち込みましょう",
            sampleResponse = "ご不満をおかけし申し訳ありません。全てのご要望への即答はできかねます。事業所として内容を確認いたします。"
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
            hint = "要望は受け止めつつ、個別専属対応の限界を組織対応として伝えましょう",
            sampleResponse = "ご希望に添いかねる部分があり申し訳ありません。専属配置は難しい状況ですが、事業所として検討いたします。"
        )
    )

    fun getById(id: String): Scenario? = scenarios.find { it.id == id }
}
