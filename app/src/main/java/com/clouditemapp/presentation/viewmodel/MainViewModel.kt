package com.clouditemapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clouditemapp.domain.model.Achievement
import com.clouditemapp.domain.repository.AchievementRepository
import com.clouditemapp.presentation.ui.common.AudioManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val audioManager: AudioManager
) : ViewModel() {
    
    private val _unlockedAchievement = MutableStateFlow<Achievement?>(null)
    val unlockedAchievement = _unlockedAchievement.asStateFlow()

    init {
        viewModelScope.launch {
            achievementRepository.newUnlockedAchievements.collect { achievement ->
                _unlockedAchievement.value = achievement
                audioManager.playSound("achievement")
            }
        }
    }

    fun dismissAchievement() {
        _unlockedAchievement.value = null
    }
}
