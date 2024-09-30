package skw.startup.whimee.domain.entity

import jakarta.persistence.*
import org.hibernate.Hibernate
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import skw.startup.whimee.domain.dto.EventType
import skw.startup.whimee.domain.dto.UserDto

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    var id: Long = 0,
    var name: String = "",
    private var login: String = "",
    private var password: String = "",
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val createdEvents: Set<EventEntity> = setOf(),
    var favouriteEventIds: Set<Long> = setOf(),
    var selectedEventTypes: Set<EventType> = setOf(
        EventType.Sport,
        EventType.Culture,
        EventType.Party,
        EventType.Foods,
        EventType.Games,
        EventType.Meetings
    ),
    var selectedDistance: Int = 10,
) : UserDetails {
    constructor(userDto: UserDto) : this(
        userDto.id ?: 0,
        userDto.name,
        "",
        "",
        userDto.createdEvents.map { EventEntity(it) }.toSet(),
        userDto.favouriteEventIds,
        userDto.selectedEventTypes,
        userDto.selectedDistance
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , favouriteEventIds = $favouriteEventIds ," +
                "selectedEventTypes = $selectedEventTypes , selectedDistance = $selectedDistance )"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority("USER"))
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return login
    }
}