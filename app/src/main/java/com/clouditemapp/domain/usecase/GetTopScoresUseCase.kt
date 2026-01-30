package com.clouditemapp.domain.usecase

import com.clouditemapp.domain.model.GameRecord
import com.clouditemapp.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTopScoresUseCase @Inject constructor(
    private val repository: GameRecordRepository
) {
    operator fun invoke(gameType: String, limit: Int = 10): Flow<List<GameRecord>> {
        return repository.getRecordsByGameType(gameType).map { records ->
            records.sortedByDescending { it.score }.take(limit)
        }
    }
}
