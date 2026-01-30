package com.clouditemapp.domain.repository

import com.clouditemapp.domain.model.UserProgress
import kotlinx.coroutines.flow.Flow

interface UserProgressRepository {
    fun getAllProgress(): Flow<List<UserProgress>>
    suspend fun getProgressByItemId(itemId: Long): UserProgress?
    fun getFavoriteItems(): Flow<List<UserProgress>>
    fun getRecentViewed(limit: Int): Flow<List<UserProgress>>
    suspend fun markAsViewed(itemId: Long)
    suspend fun incrementReviewCount(itemId: Long)
    suspend fun toggleFavorite(itemId: Long, favorite: Boolean)
    suspend fun updateProgress(progress: UserProgress)
}