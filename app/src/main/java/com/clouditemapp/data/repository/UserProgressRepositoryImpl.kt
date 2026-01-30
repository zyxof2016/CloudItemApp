package com.clouditemapp.data.repository

import com.clouditemapp.data.local.dao.UserProgressDao
import com.clouditemapp.data.local.entity.UserProgressEntity
import com.clouditemapp.domain.model.UserProgress
import com.clouditemapp.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserProgressRepositoryImpl @Inject constructor(
    private val userProgressDao: UserProgressDao
) : UserProgressRepository {

    override fun getAllProgress(): Flow<List<UserProgress>> {
        return userProgressDao.getAllProgress().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getProgressByItemId(itemId: Long): UserProgress? {
        return userProgressDao.getProgressByItemId(itemId)?.toDomainModel()
    }

    override fun getFavoriteItems(): Flow<List<UserProgress>> {
        return userProgressDao.getFavoriteItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getRecentViewed(limit: Int): Flow<List<UserProgress>> {
        return userProgressDao.getRecentViewed(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun markAsViewed(itemId: Long) {
        userProgressDao.markAsViewed(itemId, System.currentTimeMillis())
    }

    override suspend fun incrementReviewCount(itemId: Long) {
        userProgressDao.incrementReviewCount(itemId, System.currentTimeMillis())
    }

    override suspend fun toggleFavorite(itemId: Long, favorite: Boolean) {
        userProgressDao.toggleFavorite(itemId, favorite)
    }

    override suspend fun updateProgress(progress: UserProgress) {
        userProgressDao.updateProgress(progress.toEntity())
    }
}

private fun UserProgressEntity.toDomainModel(): UserProgress {
    return UserProgress(
        itemId = itemId,
        viewed = viewed,
        reviewCount = reviewCount,
        masteryLevel = masteryLevel,
        lastViewTime = lastViewTime,
        favorite = favorite
    )
}

private fun UserProgress.toEntity(): UserProgressEntity {
    return UserProgressEntity(
        itemId = itemId,
        viewed = viewed,
        reviewCount = reviewCount,
        masteryLevel = masteryLevel,
        lastViewTime = lastViewTime,
        favorite = favorite
    )
}