package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.model.Item
import com.clouditemapp.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRandomItemsUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(limit: Int = 10): Flow<List<Item>> {
        return itemRepository.getRandomItems(limit)
    }
}