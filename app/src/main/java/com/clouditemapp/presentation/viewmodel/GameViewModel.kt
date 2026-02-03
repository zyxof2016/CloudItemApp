package com.clouditemapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clouditemapp.domain.model.Item
import com.clouditemapp.domain.model.GameRecord
import com.clouditemapp.domain.usecase.GetRandomItemsUseCase
import com.clouditemapp.domain.usecase.GetItemsByCategoryUseCase
import com.clouditemapp.domain.usecase.GetTopScoresUseCase
import com.clouditemapp.domain.usecase.SaveGameRecordUseCase
import com.clouditemapp.presentation.ui.common.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val getRandomItemsUseCase: GetRandomItemsUseCase,
    private val getItemsByCategoryUseCase: GetItemsByCategoryUseCase,
    private val saveGameRecordUseCase: SaveGameRecordUseCase,
    private val getTopScoresUseCase: GetTopScoresUseCase,
    private val audioManager: AudioManager
) : ViewModel() {

    sealed class GameState {
        object Menu : GameState()
        object LevelSelection : GameState()
        object Playing : GameState()
        object Leaderboard : GameState()
        data class Result(val score: Int, val correctCount: Int, val totalCount: Int) : GameState()
    }

    private val _gameState = MutableStateFlow<GameState>(GameState.Menu)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _currentGameType = MutableStateFlow("guess")
    val currentGameType: StateFlow<String> = _currentGameType.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    fun selectGameMode(gameType: String) {
        _currentGameType.value = gameType
        _gameState.value = GameState.LevelSelection
    }

    private val _topScores = MutableStateFlow<List<GameRecord>>(emptyList())
    val topScores: StateFlow<List<GameRecord>> = _topScores.asStateFlow()

    fun showLeaderboard() {
        viewModelScope.launch {
            getTopScoresUseCase(_currentGameType.value).collect { scores ->
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

    private val _options = MutableStateFlow<List<Item>>(emptyList())
    val options: StateFlow<List<Item>> = _options.asStateFlow()

    fun startGame(category: String? = null, itemCount: Int = 10) {
        _selectedCategory.value = category
        viewModelScope.launch {
            val flow = if (category != null && category != "全部随机") {
                getItemsByCategoryUseCase(category)
            } else {
                getRandomItemsUseCase(itemCount)
            }

            // 使用 first() 确保只获取一次快照，避免数据库变化导致游戏重置
            val items = flow.first()
            if (items.isNotEmpty()) {
                val finalItems = if (category != null && category != "全部随机") {
                    items.shuffled().take(if (itemCount > 0) itemCount else items.size)
                } else {
                    items
                }

                _currentItems.value = finalItems
                _currentIndex.value = 0
                _score.value = 0
                _correctCount.value = 0
                _totalQuestions.value = finalItems.size
                gameStartTime = System.currentTimeMillis()
                
                // 先生成第一题的选项
                updateOptionsSync()
                
                _gameState.value = GameState.Playing
                
                if (_currentGameType.value == "listen") {
                    playCurrentItemAudio()
                } else if (_currentGameType.value == "guess") {
                    playCurrentItemDescriptionAudio()
                }
            }
        }
    }

    fun playCurrentItemAudio() {
        val item = getCurrentItem() ?: return
        audioManager.playSound(item.audioCN)
    }

    fun playCurrentItemDescriptionAudio() {
        val item = getCurrentItem() ?: return
        audioManager.playSound(item.audioDescCN)
    }

    private suspend fun updateOptionsSync() {
        val currentItem = getCurrentItem() ?: return
        // 获取干扰项，使用 first() 获取单次快照
        val allRandomItems = getRandomItemsUseCase(30).first()
        val distractors = allRandomItems
            .filter { it.id != currentItem.id }
            .distinctBy { it.nameCN }
            .take(3)
        
        _options.value = (distractors + currentItem).shuffled()
    }

    fun answer(isCorrect: Boolean) {
        if (isCorrect) {
            _score.value += 10
            _correctCount.value++
            audioManager.playSound("correct")
        } else {
            audioManager.playSound("wrong")
        }

        viewModelScope.launch {
            // 延迟一小会儿让用户看清结果
            kotlinx.coroutines.delay(1000)
                if (_currentIndex.value < _currentItems.value.size - 1) {
                    _currentIndex.value++
                    // 关键修复：必须等待选项生成完成后再允许用户操作或播放音频
                    updateOptionsSync()
                    if (_currentGameType.value == "listen") {
                        playCurrentItemAudio()
                    } else if (_currentGameType.value == "guess") {
                        playCurrentItemDescriptionAudio()
                    }
                } else {
                endGame()
            }
        }
    }

    private fun endGame() {
        val duration = (System.currentTimeMillis() - gameStartTime) / 1000
        viewModelScope.launch {
            saveGameRecordUseCase(
                gameType = _currentGameType.value,
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