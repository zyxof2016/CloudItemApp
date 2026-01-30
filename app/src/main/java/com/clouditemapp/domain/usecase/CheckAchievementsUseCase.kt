package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.repository.AchievementRepository
import com.clouditemapp.domain.repository.GameRecordRepository
import com.clouditemapp.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.first
import org.json.JSONObject
import javax.inject.Inject

class CheckAchievementsUseCase @Inject constructor(
    private val achievementRepository: AchievementRepository,
    private val gameRecordRepository: GameRecordRepository,
    private val userProgressRepository: UserProgressRepository
) {
    suspend operator fun invoke() {
        val lockedAchievements = achievementRepository.getLockedAchievements().first()
        val allProgress = userProgressRepository.getAllProgress().first()
        val allRecords = gameRecordRepository.getAllRecords().first()
        
        lockedAchievements.forEach { achievement ->
            val requirement = JSONObject(achievement.requirement)
            var shouldUnlock = false
            
            when (achievement.id) {
                "first_explore" -> {
                    if (allProgress.isNotEmpty()) shouldUnlock = true
                }
                "learning_master" -> {
                    val learnedCount = allProgress.size
                    if (learnedCount >= requirement.optInt("learned_count", 10)) shouldUnlock = true
                }
                "game_master" -> {
                    val gameCount = allRecords.size
                    if (gameCount >= requirement.optInt("game_count", 5)) shouldUnlock = true
                }
                "perfect_answer" -> {
                    val hasPerfect = allRecords.any { 
                        it.correctCount == it.totalCount && it.totalCount >= 10 
                    }
                    if (hasPerfect) shouldUnlock = true
                }
                // 可以根据需要增加更多逻辑
            }
            
            if (shouldUnlock) {
                achievementRepository.unlockAchievement(achievement.id)
            }
        }
    }
}
