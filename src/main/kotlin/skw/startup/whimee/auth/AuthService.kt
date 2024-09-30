package skw.startup.whimee.auth

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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

    fun register(request: RegisterRequest): AuthResponse {
        val user = UserEntity(
            name = request.name,
            login = request.login,
            password = passwordEncoder.encode(request.password)
        )

        userRepository.save(user)

        val jwtToken = jwtService.generateToken(user)

        return AuthResponse(jwtToken)
    }

    fun login(request: AuthRequest): AuthResponse {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(request.login, request.password))
        } catch (ex: Exception) {
            throw RuntimeException("Authentication failed", ex)
        }

        val user = userRepository.findByLogin(request.login) ?: throw RuntimeException("User not found")
//        val encodedPassword = passwordEncoder.encode(request.password)

//        if (!passwordEncoder.matches(encodedPassword, user.password)) {
//            throw RuntimeException("Invalid password")
//        }

        val jwtToken = jwtService.generateToken(user)

        return AuthResponse(jwtToken)
    }
}