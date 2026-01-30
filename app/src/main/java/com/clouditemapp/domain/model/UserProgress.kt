package com.clouditemapp.domain.model

data class UserProgress(
    val itemId: Long,
    val viewed: Boolean = false,
    val reviewCount: Int = 0,
    val masteryLevel: Int = 0,
    val lastViewTime: Long = 0L,
    val favorite: Boolean = false
)