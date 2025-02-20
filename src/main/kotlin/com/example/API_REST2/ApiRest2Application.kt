package com.example.API_REST2

import com.example.API_REST2.security.RSAKeysProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RSAKeysProperties::class)
class ApiRest2Application

fun main(args: Array<String>) {
	runApplication<ApiRest2Application>(*args)
}
