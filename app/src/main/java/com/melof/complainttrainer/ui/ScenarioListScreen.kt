package com.melof.complainttrainer.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.melof.complainttrainer.data.Scenario
import com.melof.complainttrainer.viewmodel.TrainerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioListScreen(
    vm: TrainerViewModel,
    onScenarioSelected: (Scenario) -> Unit
) {
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
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            listOf(
                1 to "★　軽い苦情",
                2 to "★★　こじれ",
                3 to "★★★　過大要求"
            ).forEach { (level, label) ->
                item {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )
                }
                items(vm.scenarios.filter { it.difficulty == level }) { scenario ->
                    ScenarioCard(scenario = scenario, onClick = { onScenarioSelected(scenario) })
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun ScenarioCard(scenario: Scenario, onClick: () -> Unit) {
    val bgColor = when (scenario.difficulty) {
        1 -> Color(0xFFE8F5E9)
        2 -> Color(0xFFFFF8E1)
        else -> Color(0xFFFFEBEE)
    }
    val borderColor = when (scenario.difficulty) {
        1 -> Color(0xFF81C784)
        2 -> Color(0xFFFFCA28)
        else -> Color(0xFFEF9A9A)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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
    }
}
