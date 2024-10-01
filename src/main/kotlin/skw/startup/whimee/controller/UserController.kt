package skw.startup.whimee.controller

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import skw.startup.whimee.domain.dto.UserDto
import skw.startup.whimee.domain.entity.UserEntity
import skw.startup.whimee.service.UserService

@RestController
class UserController(val userService: UserService) {

    @PostMapping(path = ["/user"])
    fun createUser(@RequestBody user: UserDto): ResponseEntity<UserDto> {
        try {
            val entity = userService.createUser(UserEntity(user))

            return ResponseEntity(UserDto(entity), HttpStatus.CREATED)
        } catch (ex: Exception) {
            throw (ex)
        }
    }

    @GetMapping(path = ["/userExists"])
    fun checkIfUserExists(@RequestParam(name = "login") login: String): ResponseEntity<Boolean> {
        try {
            val entity = userService.getUserByLogin(login)

            return ResponseEntity(entity != null, HttpStatus.OK)
        } catch (ex: Exception) {
            throw (ex)
        }
    }

    @GetMapping(path = ["/user"])
    fun getUser(@RequestParam(name = "userId") userId: Long): ResponseEntity<UserDto> {
        try {
            val entity = userService.getUser(userId)

            entity?.let {
                return ResponseEntity(UserDto(it), HttpStatus.OK)
            } ?: throw NotFoundException()

        } catch (ex: Exception) {
            throw (ex)
        }
    }

    @PutMapping(path = ["/user/{userId}"])
    fun updateUser(
        @PathVariable(name = "userId") userId: Long,
        @RequestBody newUser: UserDto
    ): ResponseEntity<UserDto> {
        try {
            val user = userService.getUser(userId) ?: throw NotFoundException()
            user.name = newUser.name
            user.favouriteEventIds = newUser.favouriteEventIds
            user.selectedEventTypes = newUser.selectedEventTypes
            user.selectedDistance = newUser.selectedDistance
            val entity = userService.updateUser(user)

            entity.let {
                return ResponseEntity(UserDto(it), HttpStatus.OK)
            }

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