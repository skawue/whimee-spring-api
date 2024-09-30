package skw.startup.whimee.service

import skw.startup.whimee.domain.entity.UserEntity

interface UserService {

    fun createUser(userEntity: UserEntity): UserEntity
    fun getAllUsers(): List<UserEntity>
    fun getUser(userId: Long): UserEntity?
    fun updateUser(userEntity: UserEntity): UserEntity
}