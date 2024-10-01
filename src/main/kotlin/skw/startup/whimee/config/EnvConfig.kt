package skw.startup.whimee.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "whimee")
data class EnvConfig(
    var jwtSecret: String = "",
    var jwtExpiration: Long = 0
)