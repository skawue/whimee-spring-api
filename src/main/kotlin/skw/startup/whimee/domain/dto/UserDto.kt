package skw.startup.whimee.domain.dto

import skw.startup.whimee.domain.entity.UserEntity

data class UserDto(
    val id: Long?,
    val name: String,
    val createdEvents: Set<Long>,
    val favouriteEventIds: Set<Long>,
    val selectedEventTypes: Set<EventType>,
    val selectedDistance: Int,
) {
    constructor(userEntity: UserEntity) : this(
        userEntity.id,
        userEntity.name,
        userEntity.createdEvents.map { it.id }.toSet(),
        userEntity.favouriteEventIds,
        userEntity.selectedEventTypes,
        userEntity.selectedDistance ?: 10
    )
}