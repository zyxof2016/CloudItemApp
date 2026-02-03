package com.clouditemapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clouditemapp.domain.model.Item
import com.clouditemapp.domain.usecase.GetItemsByCategoryUseCase
import com.clouditemapp.domain.usecase.GetRandomItemsUseCase
import com.clouditemapp.domain.usecase.MarkAsViewedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.clouditemapp.presentation.ui.common.AudioManager

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val getItemsByCategoryUseCase: GetItemsByCategoryUseCase,
    private val getRandomItemsUseCase: GetRandomItemsUseCase,
    private val markAsViewedUseCase: MarkAsViewedUseCase,
    private val audioManager: AudioManager
) : ViewModel() {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _selectedCategory = MutableStateFlow("动物世界")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    init {
        loadItems("动物世界")
    }

    fun loadItems(category: String) {
        viewModelScope.launch {
            _selectedCategory.value = category
            _currentIndex.value = 0
            getItemsByCategoryUseCase(category).collect { itemList ->
                _items.value = itemList
                if (itemList.isNotEmpty()) {
                    playCurrentAudio()
                }
            }
        }
    }

    fun loadRandomItems(limit: Int = 10) {
        viewModelScope.launch {
            _selectedCategory.value = "随机"
            _currentIndex.value = 0
            getRandomItemsUseCase(limit).collect { itemList ->
                _items.value = itemList
                if (itemList.isNotEmpty()) {
                    playCurrentAudio()
                }
            }
        }
    }

    fun nextItem() {
        if (_currentIndex.value < _items.value.size - 1) {
            _currentIndex.value++
            playCurrentAudio()
        }
    }

    fun previousItem() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
            playCurrentAudio()
        }
    }

    fun goToItem(index: Int) {
        if (index in _items.value.indices) {
            _currentIndex.value = index
            playCurrentAudio()
        }
    }

    private fun playCurrentAudio() {
        stopAudio()
        val item = getCurrentItem() ?: return
        viewModelScope.launch {
            // 稍微延迟一下，确保界面已经切换
            kotlinx.coroutines.delay(300)
            _isPlaying.value = true
            audioManager.playSound(item.audioCN) {
                _isPlaying.value = false
            }
        }
    }

    private fun stopAudio() {
        audioManager.stopSound()
        _isPlaying.value = false
    }

    fun markCurrentAsViewed() {
        val currentItem = _items.value.getOrNull(_currentIndex.value) ?: return
        viewModelScope.launch {
            markAsViewedUseCase(currentItem.id)
        }
    }

    fun togglePlaying() {
        val item = getCurrentItem() ?: return
        if (_isPlaying.value) {
            stopAudio()
        } else {
            _isPlaying.value = true
            audioManager.playSound(item.audioCN) {
                _isPlaying.value = false
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.stopSound()
    }

    fun getCurrentItem(): Item? {
        return _items.value.getOrNull(_currentIndex.value)
    }
}
