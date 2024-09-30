package skw.startup.whimee.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        jwtAuthFilter: JwtAuthFilter,
        authProvider: AuthenticationProvider
    ): SecurityFilterChain {
        httpSecurity
            .csrf {
                it.disable()
            }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/**").permitAll()
                it.requestMatchers("/events").permitAll()
                it.requestMatchers(HttpMethod.GET, "/event").permitAll()
                it.requestMatchers(HttpMethod.GET, "/user").permitAll()
                it.anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

        return httpSecurity.build()
    }
}