package com.clouditemapp.di

import com.clouditemapp.data.repository.AchievementRepositoryImpl
import com.clouditemapp.data.repository.GameRecordRepositoryImpl
import com.clouditemapp.data.repository.ItemRepositoryImpl
import com.clouditemapp.data.repository.UserProgressRepositoryImpl
import com.clouditemapp.domain.repository.AchievementRepository
import com.clouditemapp.domain.repository.GameRecordRepository
import com.clouditemapp.domain.repository.ItemRepository
import com.clouditemapp.domain.repository.UserProgressRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindItemRepository(
        itemRepositoryImpl: ItemRepositoryImpl
    ): ItemRepository

    @Binds
    @Singleton
    abstract fun bindUserProgressRepository(
        userProgressRepositoryImpl: UserProgressRepositoryImpl
    ): UserProgressRepository

    @Binds
    @Singleton
    abstract fun bindGameRecordRepository(
        gameRecordRepositoryImpl: GameRecordRepositoryImpl
    ): GameRecordRepository

    @Binds
    @Singleton
    abstract fun bindAchievementRepository(
        achievementRepositoryImpl: AchievementRepositoryImpl
    ): AchievementRepository
}