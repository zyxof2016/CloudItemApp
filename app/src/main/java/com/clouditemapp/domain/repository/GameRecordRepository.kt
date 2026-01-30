package com.clouditemapp.domain.repository

import com.clouditemapp.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    fun getAllRecords(): Flow<List<GameRecord>>
    fun getRecentRecords(limit: Int): Flow<List<GameRecord>>
    fun getRecordsByGameType(gameType: String): Flow<List<GameRecord>>
    suspend fun getAverageScoreByGameType(gameType: String): Double?
    suspend fun getTotalStatsByGameType(gameType: String): Pair<Int, Int>?
    suspend fun insertRecord(record: GameRecord)
    suspend fun deleteOldRecords(timestamp: Long)
}