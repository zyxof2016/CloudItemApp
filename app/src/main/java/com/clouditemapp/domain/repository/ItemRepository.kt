package com.clouditemapp.domain.repository

import com.clouditemapp.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun getItemsByCategory(category: String): Flow<List<Item>>
    fun getItemsByDifficulty(difficulty: Int): Flow<List<Item>>
    fun getRandomItems(limit: Int): Flow<List<Item>>
    suspend fun getItemById(itemId: Long): Item?
    fun getAllItems(): Flow<List<Item>>
    fun getAllCategories(): Flow<List<String>>
    suspend fun updateItemCustomImage(itemId: Long, path: String?)
}
