package com.example.concurrencycasestudy.reservation

import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository: JpaRepository<Reservation, Long> {
  fun existsByCustomerIdAndRestaurantId(customerId: Long, restaurantId: Long): Boolean
}
