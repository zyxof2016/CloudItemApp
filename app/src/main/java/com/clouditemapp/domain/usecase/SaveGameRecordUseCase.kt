package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.model.GameRecord
import com.clouditemapp.domain.repository.GameRecordRepository
import javax.inject.Inject

class SaveGameRecordUseCase @Inject constructor(
    private val gameRecordRepository: GameRecordRepository,
    private val checkAchievementsUseCase: CheckAchievementsUseCase
) {
    suspend operator fun invoke(
        gameType: String,
        score: Int,
        correctCount: Int,
        totalCount: Int,
        duration: Long
    ) {
        val record = GameRecord(
            gameType = gameType,
            score = score,
            correctCount = correctCount,
            totalCount = totalCount,
            duration = duration,
            timestamp = System.currentTimeMillis()
        )
        gameRecordRepository.insertRecord(record)
        // 游戏结束后检查成就
        checkAchievementsUseCase()
    }
}
