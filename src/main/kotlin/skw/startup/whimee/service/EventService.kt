package skw.startup.whimee.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import skw.startup.whimee.domain.entity.EventEntity

interface EventService {

    fun createEvent(eventEntity: EventEntity): EventEntity
    fun getAllEvents(): List<EventEntity>
    fun getAllEvents(pageable: Pageable): Page<EventEntity>
    fun getAllEvents(
        latitude: Float,
        longitude: Float,
        distanceLat: Float,
        distanceLong: Float,
        types: List<String>
    ): List<EventEntity>
    fun getAllUserFavsEvents(
        latitude: Float,
        longitude: Float,
        distanceLat: Float,
        distanceLong: Float,
        types: List<String>,
        userId: Long
    ): List<EventEntity>

    fun getEvent(eventId: Long): EventEntity?
}