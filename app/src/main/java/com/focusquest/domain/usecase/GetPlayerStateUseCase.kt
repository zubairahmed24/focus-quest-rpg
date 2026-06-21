package com.focusquest.domain.usecase

import com.focusquest.domain.model.PlayerState
import com.focusquest.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns a reactive stream of the player's current game state.
 *
 * Used by ViewModels to observe and display player level, XP, streak,
 * current boss HP, and total focus time.
 */
class GetPlayerStateUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    operator fun invoke(): Flow<PlayerState?> {
        return playerRepository.getPlayerState()
    }
}
