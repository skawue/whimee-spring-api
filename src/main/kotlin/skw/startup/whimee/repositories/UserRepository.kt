package skw.startup.whimee.repositories

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import skw.startup.whimee.domain.entity.UserEntity

@Repository
interface UserRepository : CrudRepository<UserEntity, Long> {
    fun findByLogin(login: String): UserEntity?
}