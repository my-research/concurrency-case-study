package com.example.concurrencycasestudy.reservation

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
class ReserveRestaurantController(
  private val service: MakeReservationService
) {

  @PostMapping("/reservations")
  fun reserve(@RequestBody request: MakeReservationRequest): ResponseEntity<MakeReservationResponse> {
    val response = service.reserve(request)
    return ResponseEntity.ok(response)
  }

  @ExceptionHandler(value = [IllegalStateException::class])
  fun handle(e: IllegalStateException): ResponseEntity<MakeReservationFailedResponse> {
    return ResponseEntity.badRequest().body(MakeReservationFailedResponse(e.message.orEmpty()))
  }

  @ExceptionHandler(value = [IllegalArgumentException::class])
  fun handle(e: IllegalArgumentException): ResponseEntity<MakeReservationFailedResponse> {
    return ResponseEntity.badRequest().body(MakeReservationFailedResponse(e.message.orEmpty()))
  }
}

data class MakeReservationRequest(
  val customerId: Long,
  val restaurantId: Long
)

data class MakeReservationResponse(
  val reservedAt: Instant
)

data class MakeReservationFailedResponse(
  val message: String
)
