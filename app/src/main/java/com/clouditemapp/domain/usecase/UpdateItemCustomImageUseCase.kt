package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.repository.ItemRepository
import javax.inject.Inject

class UpdateItemCustomImageUseCase @Inject constructor(
    private val itemRepository: ItemRepository
) {
    suspend operator fun invoke(itemId: Long, path: String?) {
        itemRepository.updateItemCustomImage(itemId, path)
    }
}
