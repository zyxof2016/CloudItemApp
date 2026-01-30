package com.clouditemapp.data.repository

import com.clouditemapp.data.local.dao.AchievementDao
import com.clouditemapp.data.local.entity.AchievementEntity
import com.clouditemapp.domain.model.Achievement
import com.clouditemapp.domain.repository.AchievementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AchievementRepositoryImpl @Inject constructor(
    private val achievementDao: AchievementDao
) : AchievementRepository {

    override fun getAllAchievements(): Flow<List<Achievement>> {
        return achievementDao.getAllAchievements().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getAchievementById(id: String): Achievement? {
        return achievementDao.getAchievementById(id)?.toDomainModel()
    }

    override fun getUnlockedAchievements(): Flow<List<Achievement>> {
        return achievementDao.getUnlockedAchievements().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getLockedAchievements(): Flow<List<Achievement>> {
        return achievementDao.getLockedAchievements().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun unlockAchievement(id: String) {
        achievementDao.unlockAchievement(id, System.currentTimeMillis())
    }

    override suspend fun insertAchievement(achievement: Achievement) {
        achievementDao.insertAchievement(achievement.toEntity())
    }

    override suspend fun updateAchievement(achievement: Achievement) {
        achievementDao.updateAchievement(achievement.toEntity())
    }
}

private fun AchievementEntity.toDomainModel(): Achievement {
    return Achievement(
        id = id,
        name = name,
        description = description,
        iconRes = iconRes,
        type = type,
        requirement = parseJsonMap(requirement),
        reward = reward,
        unlocked = unlocked,
        unlockedTime = unlockedTime
    )
}

private fun Achievement.toEntity(): AchievementEntity {
    return AchievementEntity(
        id = id,
        name = name,
        description = description,
        iconRes = iconRes,
        type = type,
        requirement = mapToJson(requirement),
        reward = reward,
        unlocked = unlocked,
        unlockedTime = unlockedTime
    )
}

private fun parseJsonMap(json: String): Map<String, Any> {
    // 简单的JSON解析，实际项目中应该使用Gson或Moshi
    return try {
        emptyMap()
    } catch (e: Exception) {
        emptyMap()
    }
}

private fun mapToJson(map: Map<String, Any>): String {
    // 简单的JSON序列化，实际项目中应该使用Gson或Moshi
    return "{}"
}