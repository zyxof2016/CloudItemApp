package com.clouditemapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.clouditemapp.data.local.entity.GameRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {
    @Query("SELECT * FROM game_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<GameRecordEntity>>

    @Query("SELECT * FROM game_records ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentRecords(limit: Int): Flow<List<GameRecordEntity>>

    @Query("SELECT * FROM game_records WHERE gameType = :gameType ORDER BY timestamp DESC")
    fun getRecordsByGameType(gameType: String): Flow<List<GameRecordEntity>>

    @Query("SELECT AVG(score) FROM game_records WHERE gameType = :gameType")
    suspend fun getAverageScoreByGameType(gameType: String): Double?

    @Query("SELECT SUM(correctCount) as totalCorrect, SUM(totalCount) as totalQuestions FROM game_records WHERE gameType = :gameType")
    suspend fun getTotalStatsByGameType(gameType: String): TotalStats?

    @Insert
    suspend fun insertRecord(record: GameRecordEntity)

    @Query("DELETE FROM game_records WHERE timestamp < :timestamp")
    suspend fun deleteOldRecords(timestamp: Long)
}

data class TotalStats(
    val totalCorrect: Int,
    val totalQuestions: Int
)