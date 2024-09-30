package skw.startup.whimee.service.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import skw.startup.whimee.domain.entity.EventEntity
import skw.startup.whimee.repositories.EventRepository
import skw.startup.whimee.repositories.UserRepository
import skw.startup.whimee.service.EventService
import kotlin.jvm.optionals.getOrNull

@Service
class EventServiceImpl(
    val eventRepository: EventRepository,
    val userRepository: UserRepository
) : EventService {

    override fun createEvent(eventEntity: EventEntity): EventEntity {
        return eventRepository.save(eventEntity)
    }

    override fun getAllEvents(): List<EventEntity> {
        return eventRepository.findAll().toList()
    }

    override fun getAllEvents(pageable: Pageable): Page<EventEntity> {
        return eventRepository.findAll(pageable)
    }

    override fun getAllEvents(
        latitude: Float,
        longitude: Float,
        distanceLat: Float,
        distanceLong: Float,
        types: List<String>
    ): List<EventEntity> {
        return eventRepository.findAllFiltered(
            latitude - distanceLat,
            longitude - distanceLong,
            latitude + distanceLat,
            longitude + distanceLong,
            types.toTypedArray()
        )?.toList()?.take(100) ?: listOf()
    }

    override fun getAllUserFavsEvents(
        latitude: Float,
        longitude: Float,
        distanceLat: Float,
        distanceLong: Float,
        types: List<String>,
        userId: Long
    ): List<EventEntity> {
        val userEntity = userRepository.findById(userId).getOrNull() ?: return getAllEvents(
            latitude,
            longitude,
            distanceLat,
            distanceLong,
            types
        )

        return eventRepository.findAllFavsFiltered(
            latitude - distanceLat,
            longitude - distanceLong,
            latitude + distanceLat,
            longitude + distanceLong,
            types.toTypedArray(),
            userEntity.favouriteEventIds.toTypedArray()
        )?.toList()?.take(100) ?: listOf()
    }

    override fun getEvent(eventId: Long): EventEntity? {
        return eventRepository.findById(eventId).getOrNull()
    }
}