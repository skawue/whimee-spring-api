package skw.startup.whimee.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<AuthResponse> {
        try {
            val response = authService.register(request)

            return ResponseEntity.ok(response)
        } catch (ex: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: AuthRequest): ResponseEntity<AuthResponse> {
        try {
            val response = authService.login(request)

            return ResponseEntity.ok(response)
        } catch (ex: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }
}