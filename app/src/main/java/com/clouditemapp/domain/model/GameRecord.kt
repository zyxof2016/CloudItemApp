package com.clouditemapp.domain.model

data class GameRecord(
    val id: Long = 0,
    val gameType: String,
    val score: Int,
    val correctCount: Int,
    val totalCount: Int,
    val duration: Long,
    val timestamp: Long
)