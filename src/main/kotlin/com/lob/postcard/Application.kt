package com.lob.postcard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication(scanBasePackages = ["com.lob.postcard"])
class Application
