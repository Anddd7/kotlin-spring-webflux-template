package com.github.anddd7.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class HelloController {
  @Value("\${image.version:development}")
  private lateinit var imageVersion: String

  @Value("\${APP_ENV:local}")
  private lateinit var appEnv: String

  @GetMapping("/profile")
  fun profile() = mapOf(
      "build.env" to appEnv,
      "image.version" to imageVersion
  )

  @GetMapping("/hello")
  fun hello() = "Hello, this is kotlin-spring-webflux !"

  @GetMapping("/ping")
  fun ping() = PONG

  companion object {
    private const val PONG = "pong"
  }
}
