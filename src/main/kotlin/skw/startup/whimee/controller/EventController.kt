package skw.startup.whimee.controller

import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import skw.startup.whimee.domain.dto.EventDto
import skw.startup.whimee.domain.entity.EventEntity
import skw.startup.whimee.service.EventService
import kotlin.math.cos

@RestController
class EventController(val eventService: EventService) {

    companion object {
        const val RANDOM_LOCATION_PARAM = 0.0001f
    }

    @PostMapping(path = ["/event"])
    fun createEvent(@RequestBody event: EventDto): ResponseEntity<EventDto> {
        try {
            val entityToSave = EventEntity(event).apply {
                this.locationLat += (Math.random() * RANDOM_LOCATION_PARAM).toFloat()
                this.locationLong += (Math.random() * RANDOM_LOCATION_PARAM).toFloat()
            }
            val entity = eventService.createEvent(entityToSave)

            return ResponseEntity(EventDto(entity), HttpStatus.CREATED)
        } catch (ex: Exception) {
            throw (ex)
        }
    }

//    @GetMapping(path = ["/events"])
//    fun getAllEvents(pageable: Pageable): ResponseEntity<Page<EventDto>> {
//        try {
//            val entities = eventService.getAllEvents(pageable)
//
//            return ResponseEntity(entities.map { EventDto(it) }, HttpStatus.OK)
//        } catch (ex: Exception) {
//            return ResponseEntity(null, HttpStatus.BAD_REQUEST)
//        }
//    }

    @GetMapping(path = ["/events"])
    fun getEvents(
        @RequestParam(name = "latitude") latitude: Float,
        @RequestParam(name = "longitude") longitude: Float,
        @RequestParam(name = "distance") distance: Float,
        @RequestParam(name = "types") types: List<String>?,
        @RequestParam(name = "onlyFavs") onlyFavs: Boolean? = false,
        @RequestParam(name = "userId") userId: Long? = null
    ): ResponseEntity<List<EventDto>> {
        try {
            if (types.isNullOrEmpty()) return ResponseEntity(listOf(), HttpStatus.OK)

            val distanceLat = distance / 111320.0f
            val distanceLong = distanceLat / cos(latitude * 0.01745f)

            val entities = if (onlyFavs == true && userId != null) {
                eventService.getAllUserFavsEvents(latitude, longitude, distanceLat, distanceLong, types, userId)
            } else {
                eventService.getAllEvents(latitude, longitude, distanceLat, distanceLong, types)
            }

            return ResponseEntity(entities.map { EventDto(it) }, HttpStatus.OK)
        } catch (ex: Exception) {
            throw (ex)
        }
    }

    @GetMapping(path = ["/event"])
    fun getEvent(@RequestParam(name = "eventId") eventId: Long): ResponseEntity<EventDto> {
        try {
            val entity = eventService.getEvent(eventId)

            entity?.let {
                return ResponseEntity(EventDto(it), HttpStatus.OK)
            } ?: throw ChangeSetPersister.NotFoundException()

        } catch (ex: Exception) {
            throw (ex)
        }
    }

    @ExceptionHandler
    fun handleHttpMessageNotReadableException(exp: Exception): ResponseEntity<Any> {
        val errorMessages = exp.message

        return ResponseEntity(errorMessages, HttpStatus.BAD_REQUEST)
    }
}