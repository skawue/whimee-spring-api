package skw.startup.whimee.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import skw.startup.whimee.config.JwtService
import skw.startup.whimee.domain.entity.UserEntity
import skw.startup.whimee.repositories.UserRepository

@Service
class AuthService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtService: JwtService,
    val authenticationManager: AuthenticationManager
) {
    fun join(request: JoinRequest): AuthResponse {
        return try {
            tryLogin(request)
        } catch (ex: AuthenticationException) {
            tryRegister(request)
        }
    }

    private fun tryRegister(request: JoinRequest): AuthResponse {
        val user = userRepository.findByLogin(request.login)

        if (user != null) {
            throw RuntimeException("User already exists")
        }

        val newUser = UserEntity(
            name = request.login,
            login = request.login,
            password = passwordEncoder.encode(request.password)
        )

        userRepository.save(newUser)

        val jwtToken = jwtService.generateToken(newUser)

        return AuthResponse(jwtToken)
    }

    private fun tryLogin(request: JoinRequest): AuthResponse {
        authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.login, request.password))

        val user = userRepository.findByLogin(request.login) ?: throw RuntimeException("User not found")
        val jwtToken = jwtService.generateToken(user)

        return AuthResponse(jwtToken)
    }
}