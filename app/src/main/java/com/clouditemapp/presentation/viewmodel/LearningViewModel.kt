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

import com.clouditemapp.data.local.PreferencesManager
import com.clouditemapp.presentation.ui.common.AudioManager
import com.clouditemapp.presentation.ui.common.VoiceRecognitionManager

import com.clouditemapp.domain.usecase.UpdateItemCustomImageUseCase
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

@HiltViewModel
class LearningViewModel @Inject constructor(
    private val getItemsByCategoryUseCase: GetItemsByCategoryUseCase,
    private val getRandomItemsUseCase: GetRandomItemsUseCase,
    private val markAsViewedUseCase: MarkAsViewedUseCase,
    private val updateItemCustomImageUseCase: UpdateItemCustomImageUseCase,
    private val audioManager: AudioManager,
    private val voiceRecognitionManager: VoiceRecognitionManager,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items.asStateFlow()

    private val _selectedCategory = MutableStateFlow("动物世界")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isListening = voiceRecognitionManager.isListening
    val isListening: StateFlow<Boolean> = _isListening

    private val _speechResult = MutableStateFlow<Boolean?>(null)
    val speechResult: StateFlow<Boolean?> = _speechResult.asStateFlow()

    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()

    private val _showCategoryFinished = MutableStateFlow(false)
    val showCategoryFinished: StateFlow<Boolean> = _showCategoryFinished.asStateFlow()

    private var currentLanguage = "中文"

    init {
        viewModelScope.launch {
            preferencesManager.language.collect {
                currentLanguage = it
            }
        }
        loadItems("动物世界")
    }

    fun loadItems(category: String) {
        viewModelScope.launch {
            _selectedCategory.value = category
            _currentIndex.value = 0
            
            // 播放分类名称
            val categoryAudio = when(category) {
                "动物世界" -> "cat_animals"
                "美味水果" -> "cat_fruits"
                "新鲜蔬菜" -> "cat_vegetables"
                "交通工具" -> "cat_transport"
                "日常用品" -> "cat_daily"
                "自然现象" -> "cat_nature"
                "食物与饮料" -> "cat_food"
                "身体部位" -> "cat_body"
                else -> ""
            }
            if (categoryAudio.isNotEmpty()) {
                audioManager.playSound(categoryAudio)
                kotlinx.coroutines.delay(1000) // 等待分类名称播报完
            }

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
        } else if (_items.value.isNotEmpty()) {
            _showCategoryFinished.value = true
        }
    }

    fun dismissCategoryFinished() {
        _showCategoryFinished.value = false
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
            
            when(currentLanguage) {
                "中文" -> {
                    audioManager.playSound(item.audioCN) { _isPlaying.value = false }
                }
                "English" -> {
                    audioManager.playSound(item.audioEN) { _isPlaying.value = false }
                }
                "双语" -> {
                    audioManager.playSound(item.audioCN) {
                        viewModelScope.launch {
                            kotlinx.coroutines.delay(500)
                            audioManager.playSound(item.audioEN) { _isPlaying.value = false }
                        }
                    }
                }
                else -> {
                    audioManager.playSound(item.audioCN) { _isPlaying.value = false }
                }
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
        if (_isPlaying.value) {
            stopAudio()
        } else {
            playCurrentAudio()
        }
    }

    fun startVoiceRecognition() {
        val item = getCurrentItem() ?: return
        _speechResult.value = null
        _recognizedText.value = "正在听..."
        voiceRecognitionManager.startListening(
            onResult = { result ->
                _recognizedText.value = result
                val isCorrect = result.contains(item.nameCN) || result.contains(item.nameEN, ignoreCase = true)
                _speechResult.value = isCorrect
                if (isCorrect) {
                    audioManager.playSound("correct")
                } else {
                    audioManager.playSound("wrong")
                }
            },
            onError = { 
                _speechResult.value = false 
                _recognizedText.value = "没听清，再试一次吧"
            }
        )
    }

    fun stopVoiceRecognition() {
        voiceRecognitionManager.stopListening()
    }

    fun saveCustomImage(itemId: Long, uri: Uri, context: android.content.Context) {
        viewModelScope.launch {
            try {
                val directory = File(context.filesDir, "custom_images")
                if (!directory.exists()) directory.mkdirs()
                
                val file = File(directory, "item_$itemId.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    FileOutputStream(file).use { output ->
                        input.copyTo(output)
                    }
                }
                updateItemCustomImageUseCase(itemId, file.absolutePath)
                // 强制刷新当前分类
                loadItems(_selectedCategory.value)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun resetCustomImage(itemId: Long) {
        viewModelScope.launch {
            updateItemCustomImageUseCase(itemId, null)
            loadItems(_selectedCategory.value)
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.stopSound()
        voiceRecognitionManager.destroy()
    }

    fun getCurrentItem(): Item? {
        return _items.value.getOrNull(_currentIndex.value)
    }
}
