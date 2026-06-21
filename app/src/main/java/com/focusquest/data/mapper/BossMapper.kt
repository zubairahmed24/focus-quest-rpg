package com.focusquest.data.mapper

import com.focusquest.data.local.entity.BossEntity
import com.focusquest.domain.model.Boss
import javax.inject.Inject

/**
 * Converts between BossEntity (Room) and Boss (domain).
 *
 * Simple 1:1 mapping since Boss has no date fields.
 */
class BossMapper @Inject constructor() {

    fun entityToDomain(entity: BossEntity): Boss {
        return Boss(
            id = entity.id,
            name = entity.name,
            maxHp = entity.maxHp,
            taunt = entity.taunt,
            isUnlocked = entity.isUnlocked,
            isDefeated = entity.isDefeated,
            order = entity.order
        )
    }

    fun domainToEntity(domain: Boss): BossEntity {
        return BossEntity(
            id = domain.id,
            name = domain.name,
            maxHp = domain.maxHp,
            taunt = domain.taunt,
            isUnlocked = domain.isUnlocked,
            isDefeated = domain.isDefeated,
            order = domain.order
        )
    }

    fun entityListToDomainList(entities: List<BossEntity>): List<Boss> {
        return entities.map { entityToDomain(it) }
    }
}
