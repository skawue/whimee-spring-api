package skw.startup.whimee.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(val envConfig: EnvConfig) {
    fun getLoginFromToken(jwtToken: String): String {
        return extractClaim(jwtToken, Claims::getSubject)
    }

    fun generateToken(userDetails: UserDetails): String {
        return generateToken(emptyMap(), userDetails)
    }

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + envConfig.jwtExpiration))
            .signWith(getSignInKey())
            .compact()
    }

    fun isTokenValid(jwtToken: String, userDetails: UserDetails): Boolean {
        val login = getLoginFromToken(jwtToken)

        return login == userDetails.username && !isTokenExpired(jwtToken)
    }

    private fun isTokenExpired(jwtToken: String): Boolean {
        val expirationDate = extractClaim(jwtToken, Claims::getExpiration)

        return expirationDate.before(Date(System.currentTimeMillis()))
    }

    private fun <T> extractClaim(jwtToken: String, claimResolver: (Claims) -> T): T {
        val claims = Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(jwtToken)
            .payload

        return claimResolver(claims)
    }

    private fun getSignInKey(): SecretKey {
        val keyBytes = envConfig.jwtSecret.toByteArray()

        return Keys.hmacShaKeyFor(keyBytes)
    }
}