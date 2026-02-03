package com.clouditemapp.domain.model

data class Item(
    val id: Long,
    val nameCN: String,
    val nameEN: String,
    val category: String,
    val difficulty: Int,
    val descriptionCN: String,
    val descriptionEN: String,
    val imageRes: String,
    val audioCN: String,
    val audioEN: String,
    val audioDescCN: String,
    val features: List<String>,
    val scenarios: List<String>
)