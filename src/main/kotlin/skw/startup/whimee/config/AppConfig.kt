package skw.startup.whimee.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import skw.startup.whimee.repositories.UserRepository

@Configuration
class AppConfig {

    @Bean
    fun userDetailsService(userRepository: UserRepository): UserDetailsService {
        return UserDetailsService { login ->
            userRepository.findByLogin(login) ?: throw UsernameNotFoundException("User not found")
        }
    }

    @Bean
    fun authProvider(userDetailsService: UserDetailsService, passwordEncoder: PasswordEncoder): AuthenticationProvider {
        return DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder)
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun authManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }
}