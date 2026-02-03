package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.repository.AchievementRepository
import com.clouditemapp.domain.repository.GameRecordRepository
import com.clouditemapp.domain.repository.UserProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class ProfileStats(
    val learnedCount: Int,
    val gameCount: Int,
    val totalStars: Int,
    val unlockedAchievementsCount: Int,
    val totalAchievementsCount: Int
)

class GetProfileStatsUseCase @Inject constructor(
    private val userProgressRepository: UserProgressRepository,
    private val gameRecordRepository: GameRecordRepository,
    private val achievementRepository: AchievementRepository
) {
    operator fun invoke(): Flow<ProfileStats> {
        return combine(
            userProgressRepository.getAllProgress(),
            gameRecordRepository.getAllRecords(),
            achievementRepository.getAllAchievements()
        ) { progress, records, achievements ->
            val learnedCount = progress.size
            val gameCount = records.size
            // 简单逻辑：每学习一个物品得1颗星，游戏每得10分得1颗星
            val starsFromLearning = learnedCount
            val starsFromGames = records.sumOf { it.score } / 10
            
            ProfileStats(
                learnedCount = learnedCount,
                gameCount = gameCount,
                totalStars = starsFromLearning + starsFromGames,
                unlockedAchievementsCount = achievements.count { it.unlocked },
                totalAchievementsCount = achievements.size
            )
        }
    }
}
