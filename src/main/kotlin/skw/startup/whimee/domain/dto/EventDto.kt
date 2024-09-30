package skw.startup.whimee.domain.dto

import skw.startup.whimee.domain.entity.EventEntity

enum class EventType {
    Sport, Culture, Party, Foods, Games, Meetings
}

data class EventDto(
    val id: Long?,
    val name: String,
    val description: String,
    val startDate: Long,
    val endDate: Long,
    val locationLat: Float,
    val locationLong: Float,
    val type: EventType,
    val user: UserDto,
) {
    constructor(eventEntity: EventEntity) : this(
        eventEntity.id,
        eventEntity.name,
        eventEntity.description,
        eventEntity.startDate,
        eventEntity.endDate,
        eventEntity.locationLat,
        eventEntity.locationLong,
        EventType.valueOf(eventEntity.type),
        UserDto(eventEntity.user)
    )
}