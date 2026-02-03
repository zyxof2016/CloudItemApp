package com.clouditemapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clouditemapp.domain.model.Achievement
import com.clouditemapp.domain.repository.AchievementRepository
import com.clouditemapp.domain.usecase.CheckAchievementsUseCase
import com.clouditemapp.domain.usecase.GetProfileStatsUseCase
import com.clouditemapp.domain.usecase.ProfileStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileStatsUseCase: GetProfileStatsUseCase,
    private val achievementRepository: AchievementRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase
) : ViewModel() {

    private val _stats = MutableStateFlow(ProfileStats(0, 0, 0, 0, 0))
    val stats: StateFlow<ProfileStats> = _stats.asStateFlow()

    private val _achievements = MutableStateFlow<List<Achievement>>(emptyList())
    val achievements: StateFlow<List<Achievement>> = _achievements.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // 先触发一次成就检查
            checkAchievementsUseCase()
            
            // 监听数据变化
            getProfileStatsUseCase().collect {
                _stats.value = it
            }
        }
        
        viewModelScope.launch {
            achievementRepository.getAllAchievements().collect {
                _achievements.value = it
            }
        }
    }

    fun refreshAchievements() {
        viewModelScope.launch {
            checkAchievementsUseCase()
        }
    }
}
