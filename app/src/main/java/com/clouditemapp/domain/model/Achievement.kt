package com.clouditemapp.domain.model

data class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: String,
    val type: String,
    val requirement: Map<String, Any>,
    val reward: Int,
    val unlocked: Boolean = false,
    val unlockedTime: Long? = null
)