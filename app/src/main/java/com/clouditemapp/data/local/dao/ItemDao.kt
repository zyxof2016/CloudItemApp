package com.clouditemapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.clouditemapp.data.local.entity.ItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE category = :category ORDER BY id ASC")
    fun getItemsByCategory(category: String): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE difficulty = :difficulty ORDER BY id ASC")
    fun getItemsByDifficulty(difficulty: Int): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items ORDER BY RANDOM() LIMIT :limit")
    fun getRandomItems(limit: Int): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): ItemEntity?

    @Query("SELECT * FROM items")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT DISTINCT category FROM items ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Query("UPDATE items SET customImagePath = :path WHERE id = :itemId")
    suspend fun updateCustomImage(itemId: Long, path: String?)
}
