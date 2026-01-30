package com.clouditemapp.data.repository

import com.clouditemapp.data.local.dao.ItemDao
import com.clouditemapp.data.local.entity.ItemEntity
import com.clouditemapp.domain.model.Item
import com.clouditemapp.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ItemRepositoryImpl @Inject constructor(
    private val itemDao: ItemDao
) : ItemRepository {

    override fun getItemsByCategory(category: String): Flow<List<Item>> {
        return itemDao.getItemsByCategory(category).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getItemsByDifficulty(difficulty: Int): Flow<List<Item>> {
        return itemDao.getItemsByDifficulty(difficulty).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getRandomItems(limit: Int): Flow<List<Item>> {
        return itemDao.getRandomItems(limit).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override suspend fun getItemById(itemId: Long): Item? {
        return itemDao.getItemById(itemId)?.toDomainModel()
    }

    override fun getAllItems(): Flow<List<Item>> {
        return itemDao.getAllItems().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }

    override fun getAllCategories(): Flow<List<String>> {
        return itemDao.getAllCategories()
    }
}

private fun ItemEntity.toDomainModel(): Item {
    return Item(
        id = id,
        nameCN = nameCN,
        nameEN = nameEN,
        category = category,
        difficulty = difficulty,
        descriptionCN = descriptionCN,
        descriptionEN = descriptionEN,
        imageRes = imageRes,
        audioCN = audioCN,
        audioEN = audioEN,
        features = parseJsonList(features),
        scenarios = parseJsonList(scenarios)
    )
}

private fun parseJsonList(json: String): List<String> {
    // 简单的JSON解析，实际项目中应该使用Gson或Moshi
    return try {
        if (json.startsWith("[") && json.endsWith("]")) {
            json.substring(1, json.length - 1)
                .split(",")
                .map { it.trim().replace("\"", "") }
        } else {
            emptyList()
        }
    } catch (e: Exception) {
        emptyList()
    }
}