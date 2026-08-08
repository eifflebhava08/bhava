package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MakeupCatalog
import com.example.model.BeautyChallenge
import com.example.model.CharacterModel
import com.example.model.MakeupCategory
import com.example.model.MakeupOption
import com.example.model.SavedLook
import com.example.model.ScoreResult
import com.example.sound.SoundManager
import com.example.util.ScoringEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class GameScreen {
    START_SCREEN,
    STUDIO_SCREEN,
    SCORE_SCREEN,
    GALLERY_SCREEN
}

data class MakeupGameState(
    val currentScreen: GameScreen = GameScreen.START_SCREEN,
    val activeCharacterIndex: Int = 0,
    val activeChallengeIndex: Int = 0,
    val activeCategory: MakeupCategory = MakeupCategory.FOUNDATION,
    val currentLook: Map<MakeupCategory, MakeupOption> = emptyMap(),
    val historyStack: List<Map<MakeupCategory, MakeupOption>> = emptyList(),
    val scoreResult: ScoreResult? = null,
    val savedLooks: List<SavedLook> = emptyList(),
    val isSparkling: Boolean = false,
    val showInstructionDialog: Boolean = false
) {
    val activeCharacter: CharacterModel
        get() = MakeupCatalog.characters[activeCharacterIndex % MakeupCatalog.characters.size]

    val activeChallenge: BeautyChallenge
        get() = MakeupCatalog.challenges[activeChallengeIndex % MakeupCatalog.challenges.size]
}

class MakeupGameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MakeupGameState())
    val uiState: StateFlow<MakeupGameState> = _uiState.asStateFlow()

    init {
        resetLookToDefaults()
    }

    fun startGame() {
        resetLookToDefaults()
        _uiState.update { it.copy(currentScreen = GameScreen.STUDIO_SCREEN) }
    }

    fun navigateTo(screen: GameScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun selectCategory(category: MakeupCategory) {
        _uiState.update { it.copy(activeCategory = category) }
    }

    fun selectOption(category: MakeupCategory, option: MakeupOption) {
        val currentLook = _uiState.value.currentLook
        val newHistory = _uiState.value.historyStack + listOf(currentLook)
        val updatedLook = currentLook.toMutableMap().apply {
            put(category, option)
        }

        _uiState.update {
            it.copy(
                currentLook = updatedLook,
                historyStack = newHistory
            )
        }

        triggerSparkleEffect()
        SoundManager.playClickSound()
    }

    fun undo() {
        val history = _uiState.value.historyStack
        if (history.isNotEmpty()) {
            val previousLook = history.last()
            val newHistory = history.dropLast(1)
            _uiState.update {
                it.copy(
                    currentLook = previousLook,
                    historyStack = newHistory
                )
            }
            SoundManager.playUndoSound()
            triggerSparkleEffect()
        }
    }

    fun reset() {
        resetLookToDefaults()
        SoundManager.playUndoSound()
        triggerSparkleEffect()
    }

    fun randomLook() {
        val currentLook = _uiState.value.currentLook
        val newHistory = _uiState.value.historyStack + listOf(currentLook)

        val randomLookMap = mutableMapOf<MakeupCategory, MakeupOption>()
        MakeupCategory.values().forEach { category ->
            val options = MakeupCatalog.optionsByCategory[category] ?: emptyList()
            if (options.isNotEmpty()) {
                val randomOption = options.random()
                randomLookMap[category] = randomOption
            }
        }

        _uiState.update {
            it.copy(
                currentLook = randomLookMap,
                historyStack = newHistory
            )
        }

        SoundManager.playSparkleChime()
        triggerSparkleEffect()
    }

    fun evaluateAndShowScore() {
        val state = _uiState.value
        val score = ScoringEngine.evaluateLook(state.currentLook, state.activeChallenge)

        val summaryMap = state.currentLook.map { (cat, opt) ->
            cat.displayName to opt.name
        }.toMap()

        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val newSavedLook = SavedLook(
            id = System.currentTimeMillis().toString(),
            characterName = state.activeCharacter.name,
            challengeTitle = state.activeChallenge.title,
            score = score.score,
            grade = score.grade,
            dateString = dateFormat.format(Date()),
            activeOptionsSummary = summaryMap
        )

        _uiState.update {
            it.copy(
                scoreResult = score,
                savedLooks = listOf(newSavedLook) + it.savedLooks,
                currentScreen = GameScreen.SCORE_SCREEN
            )
        }

        SoundManager.playFanfareSound()
    }

    fun nextCharacter() {
        val nextCharIndex = (_uiState.value.activeCharacterIndex + 1) % MakeupCatalog.characters.size
        val nextChallIndex = (_uiState.value.activeChallengeIndex + 1) % MakeupCatalog.challenges.size

        _uiState.update {
            it.copy(
                activeCharacterIndex = nextCharIndex,
                activeChallengeIndex = nextChallIndex,
                currentScreen = GameScreen.STUDIO_SCREEN
            )
        }
        resetLookToDefaults()
        SoundManager.playSparkleChime()
        triggerSparkleEffect()
    }

    fun selectChallenge(index: Int) {
        _uiState.update { it.copy(activeChallengeIndex = index) }
    }

    fun selectCharacter(index: Int) {
        _uiState.update { it.copy(activeCharacterIndex = index) }
        resetLookToDefaults()
    }

    fun toggleInstructionDialog(show: Boolean) {
        _uiState.update { it.copy(showInstructionDialog = show) }
    }

    private fun resetLookToDefaults() {
        val character = MakeupCatalog.characters[_uiState.value.activeCharacterIndex % MakeupCatalog.characters.size]
        val defaultLook = mutableMapOf<MakeupCategory, MakeupOption>()

        // Default Foundation
        MakeupCatalog.optionsByCategory[MakeupCategory.FOUNDATION]?.firstOrNull()?.let {
            defaultLook[MakeupCategory.FOUNDATION] = it
        }

        _uiState.update {
            it.copy(
                currentLook = defaultLook,
                historyStack = emptyList()
            )
        }
    }

    private fun triggerSparkleEffect() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSparkling = true) }
            delay(600)
            _uiState.update { it.copy(isSparkling = false) }
        }
    }
}
