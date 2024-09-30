package skw.startup.whimee.repositories

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import skw.startup.whimee.domain.entity.EventEntity

@Repository
interface EventRepository : CrudRepository<EventEntity, Long>, PagingAndSortingRepository<EventEntity, Long> {

    @Query("SELECT e from EventEntity e " +
            "where e.locationLat > ?1 and e.locationLat < ?3 and e.locationLong > ?2 and e.locationLong < ?4 " +
            "and e.type in ?5")
    fun findAllFiltered(
        startLocationLat: Float,
        startLocationLong: Float,
        endLocationLat: Float,
        endLocationLong: Float,
        types: Array<String>
    ): List<EventEntity>?

    @Query("SELECT e from EventEntity e " +
            "where e.locationLat > ?1 and e.locationLat < ?3 and e.locationLong > ?2 and e.locationLong < ?4 " +
            "and e.type in ?5 " +
            "and e.id in ?6")
    fun findAllFavsFiltered(
        startLocationLat: Float,
        startLocationLong: Float,
        endLocationLat: Float,
        endLocationLong: Float,
        types: Array<String>,
        favouriteEventIds: Array<Long>
    ): List<EventEntity>?
}