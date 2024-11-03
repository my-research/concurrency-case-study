package com.example.concurrencycasestudy.reservation

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant

@Entity(name = "reservations")
data class Reservation(
  @Id @GeneratedValue(strategy = GenerationType.AUTO)
  val id: Long? = null,

  val customerId: Long,
  val restaurantId: Long,
  val reservedAt: Instant
) {
}
