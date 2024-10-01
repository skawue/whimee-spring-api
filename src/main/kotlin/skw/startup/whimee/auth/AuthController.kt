package skw.startup.whimee.auth

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(val authService: AuthService) {

    @PostMapping("/join")
    fun join(@RequestBody request: JoinRequest): ResponseEntity<AuthResponse> {
        try {
            val response = authService.join(request)

            return ResponseEntity.ok(response)
        } catch (ex: Exception) {
            println("POST /join -> " + ex.message)

            return ResponseEntity.badRequest().build()
        }
    }
}