package skw.startup.whimee.service.impl

import org.springframework.stereotype.Service
import skw.startup.whimee.domain.entity.UserEntity
import skw.startup.whimee.repositories.UserRepository
import skw.startup.whimee.service.UserService
import kotlin.jvm.optionals.getOrNull

@Service
class UserServiceImpl(val userRepository: UserRepository) : UserService {

    override fun createUser(userEntity: UserEntity): UserEntity {
        return userRepository.save(userEntity)
    }

    override fun getAllUsers(): List<UserEntity> {
        return userRepository.findAll().toList()
    }

    override fun getUser(userId: Long): UserEntity? {
        return userRepository.findById(userId).getOrNull()
    }

    override fun updateUser(userEntity: UserEntity): UserEntity {
        return userRepository.save(userEntity)
    }
}