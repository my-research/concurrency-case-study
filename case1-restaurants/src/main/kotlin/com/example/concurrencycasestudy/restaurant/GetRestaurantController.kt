package com.example.concurrencycasestudy.restaurant

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.jvm.optionals.getOrNull

@RestController
class GetRestaurantController(
  private val repository: RestaurantRepository
) {
  @GetMapping("/restaurants/{id}")
  fun get(@PathVariable("id") id: Long): ResponseEntity<Restaurant?> {
    return ResponseEntity.ok(repository.findById(id).getOrNull())
  }
}
