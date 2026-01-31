package com.clouditemapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clouditemapp.domain.model.Achievement
import com.clouditemapp.domain.repository.AchievementRepository
import com.clouditemapp.domain.repository.GameRecordRepository
import com.clouditemapp.domain.repository.UserProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class ProfileUiState(
    val learnedCount: Int = 0,
    val gameCount: Int = 0,
    val stars: Int = 0,
    val level: Int = 1,
    val achievements: List<Achievement> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userProgressRepository: UserProgressRepository,
    private val gameRecordRepository: GameRecordRepository,
    private val achievementRepository: AchievementRepository
) : ViewModel() {

    val profileState: StateFlow<ProfileUiState> = combine(
        userProgressRepository.getAllProgress(),
        gameRecordRepository.getAllRecords(),
        achievementRepository.getAllAchievements()
    ) { progressList, recordList, achievementList ->
        val learnedCount = progressList.size
        val gameCount = recordList.size
        val unlocked = achievementList.filter { it.unlocked }
        val stars = unlocked.sumOf { it.reward }
        val level = (learnedCount / 10).coerceAtLeast(0) + 1
        ProfileUiState(
            learnedCount = learnedCount,
            gameCount = gameCount,
            stars = stars,
            level = level,
            achievements = achievementList,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProfileUiState(isLoading = true)
    )
}
