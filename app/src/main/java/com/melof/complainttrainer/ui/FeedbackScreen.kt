package com.melof.complainttrainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melof.complainttrainer.data.PracticeMode
import com.melof.complainttrainer.data.ResponseCategory
import com.melof.complainttrainer.data.SampleResponseFactory
import com.melof.complainttrainer.data.ScoreResult
import com.melof.complainttrainer.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(
    vm: TrainerViewModel,
    onRetry: () -> Unit,
    onNext: () -> Unit
) {
    val result by vm.scoreResult.collectAsStateWithLifecycle()
    val scenario by vm.currentScenario.collectAsStateWithLifecycle()
    val playMode by vm.currentPlayMode.collectAsStateWithLifecycle()
    val userPhrases by vm.userPhrases.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("フィードバック") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A6FA5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        result?.let { score ->
            val requiredCategories = score.targetCategories

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // あなたの返答
                Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "あなたの返答",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = score.input, fontSize = 15.sp, lineHeight = 24.sp)
                        if (score.tooLong) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "⚠ 少し長いです（${score.input.length}文字）。2〜3文を目安に。",
                                color = Color(0xFFE65100),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // カテゴリ判定
                Text("評価", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // 必須カテゴリ
                        requiredCategories.forEachIndexed { index, category ->
                            val achieved = score.categoryResults[category] == true
                            CategoryRow(
                                label = category.label,
                                achieved = achieved,
                                required = true,
                                showRegister = !achieved,
                                suggestedPhrase = suggestPhrase(category),
                                isAlreadyRegistered = userPhrases[category]?.isNotEmpty() == true,
                                onRegister = { phrase -> vm.registerPhrase(category, phrase) }
                            )
                            if (index != requiredCategories.lastIndex) {
                                Divider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
                            }
                        }

                        // 必須でないが達成したカテゴリ（ボーナス）
                        ResponseCategory.values()
                            .filter { it !in requiredCategories && score.categoryResults[it] == true }
                            .forEach { category ->
                                Divider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
                                CategoryRow(
                                    label = "${category.label}（ボーナス）",
                                    achieved = true,
                                    required = false,
                                    showRegister = false,
                                    suggestedPhrase = "",
                                    isAlreadyRegistered = false,
                                    onRegister = {}
                                )
                            }
                    }
                }

                // NGワード検出
                if (score.ngWordsFound.isNotEmpty()) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "NGワード検出",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB00020),
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            score.ngWordsFound.forEach { ng ->
                                Text(
                                    text = "・「$ng」",
                                    color = Color(0xFFB00020),
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }

                // スコアバッジ（必須カテゴリ達成率ベース）
                val requiredTotal = score.targetCategories.size
                val requiredAchieved = score.requiredAchieved
                val allClear = requiredAchieved == requiredTotal &&
                        score.ngWordsFound.isEmpty() && !score.tooLong
                val halfOrMore = requiredTotal > 0 &&
                        requiredAchieved >= (requiredTotal + 1) / 2
                val scoreColor = when {
                    allClear -> Color(0xFF2E7D32)
                    halfOrMore -> Color(0xFFF57F17)
                    else -> Color(0xFFB00020)
                }
                val scoreLabel = when {
                    allClear -> "よくできました"
                    halfOrMore -> "もう少し"
                    else -> "要練習"
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = scoreColor.copy(alpha = 0.1f))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "必須 $requiredAchieved / $requiredTotal",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = scoreColor
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = scoreLabel, fontSize = 16.sp, color = scoreColor)
                    }
                }

                // 文例カード
                val samples = scenario?.let { SampleResponseFactory.buildSamples(it) }.orEmpty()
                if (playMode != PracticeMode.CHOICE && samples.isNotEmpty()) {
                    var sampleExpanded by rememberSaveable { mutableStateOf(false) }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "文例を見る",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF6A1B9A)
                                )
                                TextButton(
                                    onClick = { sampleExpanded = !sampleExpanded },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                                ) {
                                    Text(
                                        text = if (sampleExpanded) "閉じる" else "表示",
                                        fontSize = 13.sp,
                                        color = Color(0xFF6A1B9A)
                                    )
                                }
                            }
                            if (sampleExpanded) {
                                Spacer(modifier = Modifier.height(8.dp))
                                samples.forEachIndexed { index, sample ->
                                    Text(
                                        text = "例${index + 1}　$sample",
                                        fontSize = 14.sp,
                                        lineHeight = 24.sp,
                                        color = Color(0xFF212121)
                                    )
                                    if (index != samples.lastIndex) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "※ 相性のよい言い回しを選びつつ、自分の言葉に置き換えてみましょう。",
                                    fontSize = 11.sp,
                                    color = Color(0xFF9E9E9E)
                                )
                            }
                        }
                    }
                }

                // 次の練習ポイント
                val advice = buildAdvice(score, requiredCategories.filter { score.categoryResults[it] != true })
                if (advice.isNotEmpty()) {
                    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "次の練習ポイント",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF1565C0)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            advice.forEach { point ->
                                Text(text = "・$point", fontSize = 13.sp, lineHeight = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }

                // ボタン
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onRetry,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Text("もう一度", fontSize = 16.sp)
                    }
                    Button(
                        onClick = onNext,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Text("シナリオ一覧", fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CategoryRow(
    label: String,
    achieved: Boolean,
    required: Boolean,
    showRegister: Boolean,
    suggestedPhrase: String,
    isAlreadyRegistered: Boolean,
    onRegister: (String) -> Unit
) {
    var showEditor by rememberSaveable { mutableStateOf(false) }
    var phraseText by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = if (required) FontWeight.Medium else FontWeight.Normal,
                color = if (required) Color(0xFF212121) else Color(0xFF757575)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (required && !achieved) {
                    Text(text = "必須", fontSize = 10.sp, color = Color(0xFFB00020))
                }
                Icon(
                    imageVector = if (achieved) Icons.Default.Check else Icons.Default.Close,
                    contentDescription = null,
                    tint = if (achieved) Color(0xFF2E7D32) else Color(0xFFB00020),
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        // 未達かつ未登録の場合のみ登録UIを表示
        if (showRegister) {
            when {
                isAlreadyRegistered -> {
                    Text(
                        text = "✓ 登録済み",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                    )
                }
                !showEditor -> {
                    TextButton(
                        onClick = {
                            phraseText = suggestedPhrase
                            showEditor = true
                        },
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            Icons.Default.AddCircleOutline,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = Color(0xFF4A6FA5)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OK表現を登録する",
                            fontSize = 12.sp,
                            color = Color(0xFF4A6FA5)
                        )
                    }
                }
                else -> {
                    // インライン編集エリア
                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        OutlinedTextField(
                            value = phraseText,
                            onValueChange = { if (it.length <= 40) phraseText = it },
                            placeholder = { Text("短いフレーズを入力（40字以内）", fontSize = 12.sp) },
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                            modifier = Modifier.fillMaxWidth(),
                            supportingText = {
                                Text("${phraseText.length}/40", fontSize = 11.sp)
                            }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { showEditor = false }) {
                                Text("キャンセル", fontSize = 12.sp)
                            }
                            Button(
                                onClick = {
                                    if (phraseText.isNotBlank()) {
                                        onRegister(phraseText.trim())
                                        showEditor = false
                                    }
                                },
                                enabled = phraseText.isNotBlank()
                            ) {
                                Text("保存", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun suggestPhrase(category: ResponseCategory): String = when (category) {
    ResponseCategory.APOLOGY -> "不快な思いをさせてしまい申し訳ありません"
    ResponseCategory.ACCEPTANCE -> "そのようなお気持ちを受け止めます"
    ResponseCategory.CONFIRMATION -> "まず詳しく確認させてください"
    ResponseCategory.ORGANIZATION -> "事業所として対応いたします"
    ResponseCategory.BOUNDARY_SETTING -> "その要求にはお答えできかねます"
}

private fun buildAdvice(score: ScoreResult, missingRequired: List<ResponseCategory>): List<String> {
    val advice = mutableListOf<String>()
    missingRequired.forEach { cat ->
        when (cat) {
            ResponseCategory.APOLOGY ->
                advice.add("「不快な思いをされた点はお詫びします」など、謝罪を入れてみましょう")
            ResponseCategory.ACCEPTANCE ->
                advice.add("「受け止めます」「承りました」など、相手の気持ちを受け止める言葉を入れてみましょう")
            ResponseCategory.CONFIRMATION ->
                advice.add("「まず確認します」など、確認への移行を入れてみましょう")
            ResponseCategory.ORGANIZATION ->
                advice.add("「事業所として対応します」など、組織対応を示しましょう")
            ResponseCategory.BOUNDARY_SETTING ->
                advice.add("「その要求にはお答えできません」など、境界設定を入れてみましょう")
        }
    }
    if (score.tooLong) advice.add("返答が長めです。2〜3文を目安に短くしてみましょう")
    if (score.ngWordsFound.isNotEmpty()) advice.add("「でも」「そんなつもりじゃ」などのNGワードを避けましょう")
    return advice
}
