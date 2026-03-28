package com.melof.complainttrainer.viewmodel

import androidx.lifecycle.ViewModel
import com.melof.complainttrainer.data.Scenario
import com.melof.complainttrainer.data.ScenarioRepository
import com.melof.complainttrainer.data.ScoreResult
import com.melof.complainttrainer.data.ScoringEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrainerViewModel : ViewModel() {

    private val _currentScenario = MutableStateFlow<Scenario?>(null)
    val currentScenario: StateFlow<Scenario?> = _currentScenario.asStateFlow()

    private val _scoreResult = MutableStateFlow<ScoreResult?>(null)
    val scoreResult: StateFlow<ScoreResult?> = _scoreResult.asStateFlow()

    val scenarios = ScenarioRepository.scenarios

    fun selectScenario(scenario: Scenario) {
        _currentScenario.value = scenario
        _scoreResult.value = null
    }

    fun submitResponse(text: String) {
        _scoreResult.value = ScoringEngine.evaluate(text)
    }

    fun retry() {
        _scoreResult.value = null
    }
}
