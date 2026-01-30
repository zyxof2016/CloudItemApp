package com.clouditemapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_records")
data class GameRecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gameType: String,        // 游戏类型
    val score: Int,              // 得分
    val correctCount: Int,       // 正确数量
    val totalCount: Int,         // 总数量
    val duration: Long,          // 时长（秒）
    val timestamp: Long          // 时间戳
)