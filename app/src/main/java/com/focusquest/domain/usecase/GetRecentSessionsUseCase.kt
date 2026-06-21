package com.focusquest.domain.usecase

import com.focusquest.domain.model.FocusSession
import com.focusquest.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns a reactive stream of the 50 most recent focus sessions.
 *
 * Used by the Stats screen to display session history.
 */
class GetRecentSessionsUseCase @Inject constructor(
    private val focusSessionRepository: FocusSessionRepository
) {
    operator fun invoke(): Flow<List<FocusSession>> {
        return focusSessionRepository.getRecentSessions()
    }
}
