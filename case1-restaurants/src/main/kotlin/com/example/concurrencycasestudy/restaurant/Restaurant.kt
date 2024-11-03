package com.example.concurrencycasestudy.restaurant

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "restaurants")
data class Restaurant(
  @Id
  val id: Long? = null,
  val name: String,
  var numberOfRemainingTable: Int,
) {

  fun decreaseRemainingTable() {
    numberOfRemainingTable--
  }

  fun hasRemainingTable(): Boolean {
    return numberOfRemainingTable > 0
  }
}
