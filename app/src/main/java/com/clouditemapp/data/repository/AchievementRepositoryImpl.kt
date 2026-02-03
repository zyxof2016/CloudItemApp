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
    val map = mutableMapOf<String, Any>()
    try {
        if (json.startsWith("{") && json.endsWith("}")) {
            val content = json.substring(1, json.length - 1)
            val pairs = content.split(",")
            pairs.forEach { pair ->
                val parts = pair.split(":")
                if (parts.size == 2) {
                    val key = parts[0].trim().replace("\"", "")
                    val valueStr = parts[1].trim().replace("\"", "")
                    val value: Any = valueStr.toIntOrNull() ?: valueStr
                    map[key] = value
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return map
}

private fun mapToJson(map: Map<String, Any>): String {
    val entries = map.entries.joinToString(",") { (k, v) -> 
        val valueStr = if (v is String) "\"$v\"" else v.toString()
        "\"$k\":$valueStr" 
    }
    return "{$entries}"
}
