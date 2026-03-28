package com.melof.complainttrainer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melof.complainttrainer.data.ResponseCategory
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
            val requiredCategories = scenario?.targetCategories ?: emptyList()

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
                        requiredCategories.forEach { category ->
                            val achieved = score.categoryResults[category] == true
                            CategoryRow(label = category.label, achieved = achieved, required = true)
                            if (category != requiredCategories.last()) {
                                Divider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
                            }
                        }

                        // 必須でないが達成したカテゴリ
                        ResponseCategory.values()
                            .filter { it !in requiredCategories && score.categoryResults[it] == true }
                            .forEach { category ->
                                Divider(color = Color(0xFFF0F0F0), thickness = 0.5.dp)
                                CategoryRow(label = "${category.label}（ボーナス）", achieved = true, required = false)
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

                // スコアバッジ
                val scoreColor = when {
                    score.totalScore >= 3 -> Color(0xFF2E7D32)
                    score.totalScore >= 2 -> Color(0xFFF57F17)
                    else -> Color(0xFFB00020)
                }
                val scoreLabel = when {
                    score.totalScore >= 3 -> "よくできました"
                    score.totalScore >= 2 -> "もう少し"
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
                            text = "スコア ${score.totalScore}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = scoreColor
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = scoreLabel, fontSize = 16.sp, color = scoreColor)
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
private fun CategoryRow(label: String, achieved: Boolean, required: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = if (required) FontWeight.Medium else FontWeight.Normal,
            color = if (required) Color(0xFF212121) else Color(0xFF757575)
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
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
}

private fun buildAdvice(score: ScoreResult, missingRequired: List<ResponseCategory>): List<String> {
    val advice = mutableListOf<String>()
    missingRequired.forEach { cat ->
        when (cat) {
            ResponseCategory.APOLOGY ->
                advice.add("「不快な思いをされた点はお詫びします」など、謝罪を入れてみましょう")
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
