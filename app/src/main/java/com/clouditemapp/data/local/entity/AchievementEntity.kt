package com.clouditemapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementEntity(
    @PrimaryKey val id: String,
    val name: String,            // 成就名称
    val description: String,     // 描述
    val iconRes: String,         // 图标
    val type: String,            // 类型
    val requirement: String,     // 要求（JSON）
    val reward: Int,             // 奖励积分
    val unlocked: Boolean = false,       // 是否解锁
    val unlockedTime: Long? = null       // 解锁时间
)