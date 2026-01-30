package com.clouditemapp.data.repository

import com.clouditemapp.data.local.dao.GameRecordDao
import com.clouditemapp.data.local.entity.GameRecordEntity
import com.clouditemapp.domain.model.GameRecord
import com.clouditemapp.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameRecordRepositoryImpl @Inject constructor(
    private val gameRecordDao: GameRecordDao
) : GameRecordRepository {

    override fun getAllRecords(): Flow<List<GameRecord>> {
        return gameRecordDao.getAllRecords().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getRecentRecords(limit: Int): Flow<List<GameRecord>> {
        return gameRecordDao.getRecentRecords(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getRecordsByGameType(gameType: String): Flow<List<GameRecord>> {
        return gameRecordDao.getRecordsByGameType(gameType).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getAverageScoreByGameType(gameType: String): Double? {
        return gameRecordDao.getAverageScoreByGameType(gameType)
    }

    override suspend fun getTotalStatsByGameType(gameType: String): Pair<Int, Int>? {
        val stats = gameRecordDao.getTotalStatsByGameType(gameType)
        return if (stats != null) {
            Pair(stats.totalCorrect, stats.totalQuestions)
        } else {
            null
        }
    }

    override suspend fun insertRecord(record: GameRecord) {
        gameRecordDao.insertRecord(record.toEntity())
    }

    override suspend fun deleteOldRecords(timestamp: Long) {
        gameRecordDao.deleteOldRecords(timestamp)
    }
}

private fun GameRecordEntity.toDomainModel(): GameRecord {
    return GameRecord(
        id = id,
        gameType = gameType,
        score = score,
        correctCount = correctCount,
        totalCount = totalCount,
        duration = duration,
        timestamp = timestamp
    )
}

private fun GameRecord.toEntity(): GameRecordEntity {
    return GameRecordEntity(
        id = id,
        gameType = gameType,
        score = score,
        correctCount = correctCount,
        totalCount = totalCount,
        duration = duration,
        timestamp = timestamp
    )
}