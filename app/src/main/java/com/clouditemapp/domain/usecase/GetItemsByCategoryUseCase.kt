package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.model.Item
import com.clouditemapp.domain.repository.ItemRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemsByCategoryUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    operator fun invoke(category: String): Flow<List<Item>> {
        return itemRepository.getItemsByCategory(category)
    }
}