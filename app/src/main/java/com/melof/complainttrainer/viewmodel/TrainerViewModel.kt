package com.melof.complainttrainer.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.melof.complainttrainer.data.ResponseCategory
import com.melof.complainttrainer.data.Scenario
import com.melof.complainttrainer.data.ScenarioRepository
import com.melof.complainttrainer.data.ScoreResult
import com.melof.complainttrainer.data.ScoringEngine
import com.melof.complainttrainer.data.UserDictionaryStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TrainerViewModel(app: Application) : AndroidViewModel(app) {

    private val userDict = UserDictionaryStore(app)

    private val _currentScenario = MutableStateFlow<Scenario?>(null)
    val currentScenario: StateFlow<Scenario?> = _currentScenario.asStateFlow()

    private val _scoreResult = MutableStateFlow<ScoreResult?>(null)
    val scoreResult: StateFlow<ScoreResult?> = _scoreResult.asStateFlow()

    // 登録済みカスタム表現（画面に表示するために公開）
    private val _userPhrases = MutableStateFlow(userDict.loadAll())
    val userPhrases: StateFlow<Map<ResponseCategory, List<String>>> = _userPhrases.asStateFlow()

    val scenarios = ScenarioRepository.scenarios

    fun selectScenario(scenario: Scenario) {
        _currentScenario.value = scenario
        _scoreResult.value = null
    }

    fun submitResponse(text: String) {
        _scoreResult.value = ScoringEngine.evaluate(text, userDict.loadAll())
    }

    fun retry() {
        _scoreResult.value = null
    }

    /** フィードバック画面から「この表現を登録」を呼ぶ */
    fun registerPhrase(category: ResponseCategory, phrase: String) {
        userDict.addPhrase(category, phrase)
        _userPhrases.value = userDict.loadAll()
    }

    fun removePhrase(category: ResponseCategory, phrase: String) {
        userDict.removePhrase(category, phrase)
        _userPhrases.value = userDict.loadAll()
    }
}
