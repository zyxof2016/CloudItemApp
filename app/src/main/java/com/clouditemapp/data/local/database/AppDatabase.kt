package com.clouditemapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.clouditemapp.data.local.dao.AchievementDao
import com.clouditemapp.data.local.dao.GameRecordDao
import com.clouditemapp.data.local.dao.ItemDao
import com.clouditemapp.data.local.dao.UserProgressDao
import com.clouditemapp.data.local.entity.AchievementEntity
import com.clouditemapp.data.local.entity.GameRecordEntity
import com.clouditemapp.data.local.entity.ItemEntity
import com.clouditemapp.data.local.entity.UserProgressEntity

@Database(
    entities = [
        ItemEntity::class,
        UserProgressEntity::class,
        GameRecordEntity::class,
        AchievementEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun gameRecordDao(): GameRecordDao
    abstract fun achievementDao(): AchievementDao
}