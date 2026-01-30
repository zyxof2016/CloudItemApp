package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.repository.UserProgressRepository
import javax.inject.Inject

class MarkAsViewedUseCase @Inject constructor(
    private val userProgressRepository: UserProgressRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase
) {
    suspend operator fun invoke(itemId: Long) {
        userProgressRepository.markAsViewed(itemId)
        // 学习新物品后检查成就
        checkAchievementsUseCase()
    }
}
