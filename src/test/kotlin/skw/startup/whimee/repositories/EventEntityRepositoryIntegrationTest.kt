package skw.startup.whimee.repositories

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import skw.startup.whimee.domain.entity.EventEntity
import skw.startup.whimee.domain.entity.UserEntity
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

@DataJpaTest
@ExtendWith(SpringExtension::class)
class EventEntityRepositoryIntegrationTest {

    @Autowired
    lateinit var eventRepository: EventRepository

    private val userEntity = UserEntity(1, "name")
    private val eventEntity1 = EventEntity(1, "Event 1", "Description 1", 1, 2, 10f, 10f, "Sport", userEntity)
    private val eventEntity2 = EventEntity(2, "Event 2", "Description 2", 1, 2, 12f, 10f, "Culture", userEntity)
    private val eventEntity3 = EventEntity(3, "Event 3", "Description 3", 1, 2, 14f, 10f, "Sport", userEntity)
    private val eventEntity4 = EventEntity(4, "Event 4", "Description 4", 1, 2, 10f, 8f, "Foods", userEntity)

    @BeforeTest
    fun setUp() {
        eventRepository.saveAll(listOf(eventEntity1, eventEntity2, eventEntity3, eventEntity4))
    }

    @Test
    fun `find all events in given range`() {
        val result = eventRepository.findAllFiltered(9f, 9f, 13f, 11f, arrayOf("Sport", "Foods"))

        assertEquals(listOf(eventEntity1), result)
    }

    @Test
    fun `find all favs events in given range`() {
        val favs = listOf(eventEntity2.id).toTypedArray()
        val result = eventRepository.findAllFavsFiltered(9f, 9f, 13f, 11f, arrayOf("Sport", "Culture"), favs)

        assertEquals(listOf(eventEntity2), result)
    }
}