package com.clouditemapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_progress")
data class UserProgressEntity(
    @PrimaryKey val itemId: Long,
    val viewed: Boolean = false,         // 是否已学习
    val reviewCount: Int = 0,            // 复习次数
    val masteryLevel: Int = 0,           // 掌握程度（0-100）
    val lastViewTime: Long = 0L,         // 最后学习时间
    val favorite: Boolean = false        // 是否收藏
)