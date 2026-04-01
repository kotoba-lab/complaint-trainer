package com.melof.complainttrainer.ui

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.melof.complainttrainer.data.ChoiceOptionFactory
import com.melof.complainttrainer.data.PracticeMode
import com.melof.complainttrainer.data.ResponseCategory
import com.melof.complainttrainer.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeakScreen(
    vm: TrainerViewModel,
    onSubmit: (String) -> Unit,
    onBack: () -> Unit
) {
    val scenario by vm.currentScenario.collectAsStateWithLifecycle()
    val playMode by vm.currentPlayMode.collectAsStateWithLifecycle()
    val scenarioId = scenario?.id
    var localText by remember(scenarioId, playMode) { mutableStateOf("") }
    var selectedChoice by remember(scenarioId, playMode) { mutableStateOf<String?>(null) }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val results = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val recognized = results?.firstOrNull() ?: ""
            if (recognized.isNotEmpty()) localText = recognized
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("返答練習 ${playMode.shortLabel}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "戻る")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4A6FA5),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        scenario?.let { sc ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 状況テキスト
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "状況",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF1565C0)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = sc.situation, fontSize = 13.sp, lineHeight = 20.sp)
                    }
                }

                // 相手のセリフ（これが核心）
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "相手のセリフ",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFB00020)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = sc.complaint,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 26.sp,
                            color = Color(0xFF212121)
                        )
                    }
                }

                if (playMode == PracticeMode.GUIDED) {
                    GuideCard(sc.hint, sc.targetCategories)
                }

                Divider(color = Color(0xFFE0E0E0))

                if (playMode == PracticeMode.CHOICE) {
                    ChoiceModeSection(
                        options = remember(sc.id) { ChoiceOptionFactory.buildOptions(sc) },
                        selectedChoice = selectedChoice,
                        onSelect = { selectedChoice = it }
                    )
                } else {
                    OutlinedTextField(
                        value = localText,
                        onValueChange = { localText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 96.dp),
                        placeholder = {
                            Text(
                                text = "マイクボタンを押して話す\nまたはここに直接入力",
                                color = Color(0xFFAAAAAA),
                                fontSize = 14.sp,
                                lineHeight = 22.sp
                            )
                        },
                        label = { Text("返答") },
                        supportingText = {
                            Text(
                                text = "${localText.length}文字${if (localText.length > 80) "　⚠ 長め" else ""}",
                                color = if (localText.length > 80) Color(0xFFE65100) else Color(0xFF9E9E9E)
                            )
                        },
                        isError = localText.length > 80,
                        minLines = 3,
                        textStyle = TextStyle(fontSize = 15.sp)
                    )

                    Button(
                        onClick = {
                            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                putExtra(
                                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                                )
                                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja-JP")
                                putExtra(RecognizerIntent.EXTRA_PROMPT, "返答を話してください")
                            }
                            speechLauncher.launch(intent)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6FA5))
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = "音声入力")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (localText.isEmpty()) "話す" else "言い直す",
                            fontSize = 17.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = {
                        val answer = if (playMode == PracticeMode.CHOICE) selectedChoice.orEmpty() else localText
                        onSubmit(answer)
                    },
                    enabled = if (playMode == PracticeMode.CHOICE) selectedChoice != null else localText.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text(
                        if (playMode == PracticeMode.CHOICE) "この選択で進む" else "採点する",
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GuideCard(
    hint: String,
    targetCategories: List<ResponseCategory>,
) {
    Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F8FC))) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (hint.isNotBlank()) {
                Text(
                    text = "ヒント：$hint",
                    fontSize = 12.sp,
                    color = Color(0xFF35527C),
                    lineHeight = 18.sp
                )
            }
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                targetCategories.forEach { category ->
                    SuggestionChip(
                        onClick = {},
                        enabled = false,
                        label = { Text(category.label, fontSize = 11.sp) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChoiceModeSection(
    options: List<com.melof.complainttrainer.data.ChoiceOption>,
    selectedChoice: String?,
    onSelect: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "最も安全な返しを1つ選んでください",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = Color(0xFF35527C)
        )
        options.forEachIndexed { index, option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(option.text) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedChoice == option.text) Color(0xFFE3F2FD) else Color.White
                ),
                border = BorderStroke(
                    1.dp,
                    if (selectedChoice == option.text) Color(0xFF4A6FA5) else Color(0xFFE0E0E0)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedChoice == option.text,
                        onClick = { onSelect(option.text) }
                    )
                    Text(
                        text = "${'A' + index}. ${option.text}",
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
