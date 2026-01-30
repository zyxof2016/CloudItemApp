package com.clouditemapp.di

import android.content.Context
import androidx.room.Room
import com.clouditemapp.data.local.dao.AchievementDao
import com.clouditemapp.data.local.dao.GameRecordDao
import com.clouditemapp.data.local.dao.ItemDao
import com.clouditemapp.data.local.dao.UserProgressDao
import com.clouditemapp.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "cloud_item_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideItemDao(database: AppDatabase): ItemDao {
        return database.itemDao()
    }

    @Provides
    fun provideUserProgressDao(database: AppDatabase): UserProgressDao {
        return database.userProgressDao()
    }

    @Provides
    fun provideGameRecordDao(database: AppDatabase): GameRecordDao {
        return database.gameRecordDao()
    }

    @Provides
    fun provideAchievementDao(database: AppDatabase): AchievementDao {
        return database.achievementDao()
    }
}