package com.melof.complainttrainer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.melof.complainttrainer.ui.FeedbackScreen
import com.melof.complainttrainer.ui.ScenarioListScreen
import com.melof.complainttrainer.ui.SpeakScreen
import com.melof.complainttrainer.ui.theme.ComplaintTrainerTheme
import com.melof.complainttrainer.viewmodel.TrainerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComplaintTrainerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    val vm: TrainerViewModel = viewModel()

                    NavHost(navController = navController, startDestination = "scenarios") {
                        composable("scenarios") {
                            ScenarioListScreen(
                                vm = vm,
                                onScenarioSelected = { scenario, playMode ->
                                    vm.selectScenario(scenario, playMode)
                                    navController.navigate("speak")
                                }
                            )
                        }
                        composable("speak") {
                            SpeakScreen(
                                vm = vm,
                                onSubmit = { text ->
                                    vm.submitResponse(text)
                                    navController.navigate("feedback")
                                },
                                onBack = { navController.popBackStack() }
                            )
                        }
                        composable("feedback") {
                            FeedbackScreen(
                                vm = vm,
                                onRetry = {
                                    vm.retry()
                                    navController.popBackStack()
                                },
                                onNext = {
                                    navController.popBackStack("scenarios", inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
