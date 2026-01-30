package com.clouditemapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clouditemapp.domain.model.Item
import com.clouditemapp.domain.model.GameRecord
import com.clouditemapp.domain.usecase.GetRandomItemsUseCase
import com.clouditemapp.domain.usecase.GetTopScoresUseCase
import com.clouditemapp.domain.usecase.SaveGameRecordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getRandomItemsUseCase: GetRandomItemsUseCase,
    private val saveGameRecordUseCase: SaveGameRecordUseCase,
    private val getTopScoresUseCase: GetTopScoresUseCase
) : ViewModel() {

    sealed class GameState {
        object Menu : GameState()
        object Playing : GameState()
        object Leaderboard : GameState()
        data class Result(val score: Int, val correctCount: Int, val totalCount: Int) : GameState()
    }

    private val _gameState = MutableStateFlow<GameState>(GameState.Menu)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _topScores = MutableStateFlow<List<GameRecord>>(emptyList())
    val topScores: StateFlow<List<GameRecord>> = _topScores.asStateFlow()

    fun showLeaderboard() {
        viewModelScope.launch {
            getTopScoresUseCase(currentGameType).collect { scores ->
                _topScores.value = scores
                _gameState.value = GameState.Leaderboard
            }
        }
    }

    private val _currentItems = MutableStateFlow<List<Item>>(emptyList())
    val currentItems: StateFlow<List<Item>> = _currentItems.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _correctCount = MutableStateFlow(0)
    val correctCount: StateFlow<Int> = _correctCount.asStateFlow()

    private val _totalQuestions = MutableStateFlow(0)
    val totalQuestions: StateFlow<Int> = _totalQuestions.asStateFlow()

    private var gameStartTime: Long = 0
    private var currentGameType: String = "guess"

    private val _options = MutableStateFlow<List<String>>(emptyList())
    val options: StateFlow<List<String>> = _options.asStateFlow()

    fun startGame(gameType: String = "guess", itemCount: Int = 10) {
        currentGameType = gameType
        viewModelScope.launch {
            getRandomItemsUseCase(itemCount).collect { items ->
                if (items.isNotEmpty()) {
                    _currentItems.value = items
                    _currentIndex.value = 0
                    _score.value = 0
                    _correctCount.value = 0
                    _totalQuestions.value = items.size
                    gameStartTime = System.currentTimeMillis()
                    generateOptions()
                    _gameState.value = GameState.Playing
                }
            }
        }
    }

    private fun generateOptions() {
        val currentItem = getCurrentItem() ?: return
        viewModelScope.launch {
            // 获取一些随机物品作为干扰项
            getRandomItemsUseCase(20).collect { allRandomItems ->
                val distractors = allRandomItems
                    .filter { it.id != currentItem.id }
                    .map { it.nameCN }
                    .distinct()
                    .take(3)
                
                _options.value = (distractors + currentItem.nameCN).shuffled()
            }
        }
    }

    fun answer(isCorrect: Boolean) {
        if (isCorrect) {
            _score.value += 10
            _correctCount.value++
        }

        viewModelScope.launch {
            // 延迟一小会儿让用户看清结果
            kotlinx.coroutines.delay(500)
            if (_currentIndex.value < _currentItems.value.size - 1) {
                _currentIndex.value++
                generateOptions() // 为下一题生成新选项
            } else {
                endGame()
            }
        }
    }

    private fun endGame() {
        val duration = (System.currentTimeMillis() - gameStartTime) / 1000
        viewModelScope.launch {
            saveGameRecordUseCase(
                gameType = currentGameType,
                score = _score.value,
                correctCount = _correctCount.value,
                totalCount = _totalQuestions.value,
                duration = duration
            )
            _gameState.value = GameState.Result(
                score = _score.value,
                correctCount = _correctCount.value,
                totalCount = _totalQuestions.value
            )
        }
    }

    fun goToMenu() {
        _gameState.value = GameState.Menu
    }

    fun getCurrentItem(): Item? {
        return _currentItems.value.getOrNull(_currentIndex.value)
    }
}