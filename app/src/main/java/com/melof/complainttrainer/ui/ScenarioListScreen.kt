package com.melof.complainttrainer.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.melof.complainttrainer.data.ComplaintDifficulty
import com.melof.complainttrainer.data.PracticeMode
import com.melof.complainttrainer.data.Scenario
import com.melof.complainttrainer.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioListScreen(
    vm: TrainerViewModel,
    onScenarioSelected: (Scenario, PracticeMode) -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val modes = PracticeMode.entries
    val currentMode = modes[selectedTab]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("苦情対応 反射訓練") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A6FA5),
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF3E5E8C),
                contentColor = Color.White,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color.White
                    )
                }
            ) {
                modes.forEachIndexed { index, mode ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = mode.label,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold
                                else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Text(
                text = currentMode.description,
                fontSize = 12.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    HelpCard(currentMode = currentMode)
                }

                item {
                    RandomStartCard(
                        vm = vm,
                        currentMode = currentMode,
                        onScenarioSelected = onScenarioSelected
                    )
                }

                ComplaintDifficulty.entries.forEach { difficulty ->
                    item {
                        Text(
                            text = "${difficulty.stars}　${difficulty.label}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    items(vm.scenarios.filter { it.difficulty == difficulty.level }) { scenario ->
                        ScenarioCard(
                            scenario = scenario,
                            mode = currentMode,
                            onClick = { onScenarioSelected(scenario, currentMode) }
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
private fun HelpCard(currentMode: PracticeMode) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F9FC)),
        border = BorderStroke(1.dp, Color(0xFF4A6FA5).copy(alpha = 0.18f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "使い方",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF35527C)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "シナリオをタップすると、その場面の練習を始められます。",
                fontSize = 12.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = when (currentMode) {
                    PracticeMode.CHOICE -> "選択式では4つの返しから選び、まず安全な返答の型を身につけます。"
                    PracticeMode.GUIDED -> "ガイド付きではヒントとねらいを見ながら、自分の言葉で返せます。"
                    PracticeMode.FREE -> "自由回答ではヒントを外して、本番に近い形で返答を練習します。"
                },
                fontSize = 12.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "「ランダムで始める」は全体、または選んだ難易度から1問を自動で出題します。",
                fontSize = 12.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun RandomStartCard(
    vm: TrainerViewModel,
    currentMode: PracticeMode,
    onScenarioSelected: (Scenario, PracticeMode) -> Unit,
) {
    var selectedDifficulty by remember { mutableStateOf<ComplaintDifficulty?>(null) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FC)),
        border = BorderStroke(1.dp, Color(0xFF4A6FA5).copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = "ランダムで始める",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF35527C)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = selectedDifficulty == null,
                    onClick = { selectedDifficulty = null },
                    label = { Text("全難易度", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF4A6FA5),
                        selectedLabelColor = Color.White
                    )
                )
                ComplaintDifficulty.entries.forEach { difficulty ->
                    FilterChip(
                        selected = selectedDifficulty == difficulty,
                        onClick = {
                            selectedDifficulty =
                                if (selectedDifficulty == difficulty) null else difficulty
                        },
                        label = { Text("${difficulty.stars} ${difficulty.label}", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF4A6FA5),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    vm.randomScenario(selectedDifficulty)?.let { onScenarioSelected(it, currentMode) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6FA5))
            ) {
                Text(
                    text = selectedDifficulty?.let { "「${it.label}」からランダム" } ?: "ランダムで始める",
                    fontSize = 15.sp
                )
            }
        }
    }
}

@Composable
fun ScenarioCard(scenario: Scenario, mode: PracticeMode, onClick: () -> Unit) {
    val difficulty = ComplaintDifficulty.fromLevel(scenario.difficulty)
    val bgColor = when (difficulty) {
        ComplaintDifficulty.BEGINNER -> Color(0xFFE8F5E9)
        ComplaintDifficulty.INTERMEDIATE -> Color(0xFFFFF8E1)
        ComplaintDifficulty.ADVANCED, null -> Color(0xFFFFEBEE)
    }
    val badgeBg = when (mode) {
        PracticeMode.CHOICE -> Color(0xFFE3F2FD)
        PracticeMode.GUIDED -> Color(0xFFF1F8E9)
        PracticeMode.FREE -> Color(0xFFF3E5F5)
    }
    val badgeFg = when (mode) {
        PracticeMode.CHOICE -> Color(0xFF1565C0)
        PracticeMode.GUIDED -> Color(0xFF2E7D32)
        PracticeMode.FREE -> Color(0xFF6A1B9A)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "「${scenario.complaint.take(28)}${if (scenario.complaint.length > 28) "…」" else "」"}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = scenario.situation.take(40) + if (scenario.situation.length > 40) "…" else "",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            SuggestionChip(
                onClick = {},
                enabled = false,
                label = { Text(mode.shortLabel, fontSize = 11.sp) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    disabledContainerColor = badgeBg,
                    disabledLabelColor = badgeFg
                )
            )
        }
    }
}
