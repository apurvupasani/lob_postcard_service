package com.lob.postcard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@Suppress("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}

@SpringBootApplication(scanBasePackages = ["com.lob.postcard"])
@Suppress("SpreadOperator")
open class Application
