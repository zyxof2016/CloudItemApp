package com.clouditemapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.clouditemapp.data.local.entity.UserProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress")
    fun getAllProgress(): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress WHERE itemId = :itemId")
    suspend fun getProgressByItemId(itemId: Long): UserProgressEntity?

    @Query("SELECT * FROM user_progress WHERE favorite = 1")
    fun getFavoriteItems(): Flow<List<UserProgressEntity>>

    @Query("SELECT * FROM user_progress ORDER BY lastViewTime DESC LIMIT :limit")
    fun getRecentViewed(limit: Int): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: UserProgressEntity)

    @Update
    suspend fun updateProgress(progress: UserProgressEntity)

    @Query("UPDATE user_progress SET viewed = 1, lastViewTime = :timestamp WHERE itemId = :itemId")
    suspend fun markAsViewed(itemId: Long, timestamp: Long)

    @Query("UPDATE user_progress SET reviewCount = reviewCount + 1, masteryLevel = MIN(100, masteryLevel + 10), lastViewTime = :timestamp WHERE itemId = :itemId")
    suspend fun incrementReviewCount(itemId: Long, timestamp: Long)

    @Query("UPDATE user_progress SET favorite = :favorite WHERE itemId = :itemId")
    suspend fun toggleFavorite(itemId: Long, favorite: Boolean)
}