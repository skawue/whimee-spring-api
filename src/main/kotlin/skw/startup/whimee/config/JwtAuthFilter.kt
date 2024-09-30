package skw.startup.whimee.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(val jwtService: JwtService, val userDetailsService: UserDetailsService) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        var jwtToken: String = ""
        var login: String = ""

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)

            return
        }

        jwtToken = authHeader.substring(7)
        login = jwtService.getLoginFromToken(jwtToken)

        if (login.isNotEmpty() && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(login)

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }
}