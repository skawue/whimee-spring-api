package skw.startup.whimee.domain.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import skw.startup.whimee.domain.dto.EventDto

@Entity
@Table(name = "events")
class EventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_seq")
    var id: Long = 0,
    var name: String = "",
    var description: String = "",
    var startDate: Long = 0,
    var endDate: Long = 0,
    var locationLat: Float = 0f,
    var locationLong: Float = 0f,
    var type: String = "",
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity = UserEntity(),
) {
    constructor(eventDto: EventDto): this(
        eventDto.id ?: 0,
        eventDto.name,
        eventDto.description,
        eventDto.startDate,
        eventDto.endDate,
        eventDto.locationLat,
        eventDto.locationLong,
        eventDto.type.name,
        UserEntity(eventDto.user)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as EventEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , description = $description ," +
                "startDate = $startDate , endDate = $endDate , locationLat = $locationLat )" +
                "locationLong = $locationLong , type = $type )"
    }
}