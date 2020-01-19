package com.github.anddd7.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/hello")
class HelloController {
  @GetMapping
  fun hello() = "Hello, this is kotlin-spring-webflux !"
}
