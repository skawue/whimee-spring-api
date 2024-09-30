package skw.startup.whimee

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WhimeeApplication

fun main(args: Array<String>) {
	runApplication<WhimeeApplication>(*args)
}
