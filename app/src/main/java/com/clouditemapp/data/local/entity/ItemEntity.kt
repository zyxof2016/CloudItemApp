package com.clouditemapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Long,
    val nameCN: String,          // 中文名称
    val nameEN: String,          // 英文名称
    val category: String,        // 分类
    val difficulty: Int,         // 难度等级 1-3
    val descriptionCN: String,   // 中文描述
    val descriptionEN: String,   // 英文描述
    val imageRes: String,        // 图片资源路径
    val audioCN: String,         // 中文音频路径
    val audioEN: String,         // 英文音频路径
    val features: String,        // 特征（JSON）
    val scenarios: String        // 场景（JSON）
)