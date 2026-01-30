package com.clouditemapp.domain.repository

import com.clouditemapp.domain.model.Achievement
import kotlinx.coroutines.flow.Flow

interface AchievementRepository {
    fun getAllAchievements(): Flow<List<Achievement>>
    suspend fun getAchievementById(id: String): Achievement?
    fun getUnlockedAchievements(): Flow<List<Achievement>>
    fun getLockedAchievements(): Flow<List<Achievement>>
    suspend fun unlockAchievement(id: String)
    suspend fun insertAchievement(achievement: Achievement)
    suspend fun updateAchievement(achievement: Achievement)
}