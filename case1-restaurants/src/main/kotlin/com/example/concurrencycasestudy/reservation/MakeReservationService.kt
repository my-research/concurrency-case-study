package com.example.concurrencycasestudy.reservation

import com.example.concurrencycasestudy.restaurant.RestaurantRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class MakeReservationService(
  private val restaurantRepository: RestaurantRepository,
  private val reservationRepository: ReservationRepository,
) {

  @Transactional
  fun reserve(request: MakeReservationRequest): MakeReservationResponse {

    val restaurant = restaurantRepository.findById(request.restaurantId)
      .orElseThrow { IllegalArgumentException("restaurant(${request.restaurantId}) not exists") }

    if (restaurant.hasRemainingTable().not()) {
      throw IllegalStateException("restaurant(${request.restaurantId}) is full booking")
    }

    if (reservationRepository.existsByCustomerIdAndRestaurantId(request.customerId, request.restaurantId)) {
      throw IllegalStateException("user(${request.customerId}) already reserved restaurant(${request.restaurantId})")
    }

    val newReservation = Reservation(
      customerId = request.customerId,
      restaurantId = request.restaurantId,
      reservedAt = Instant.now()
    )

    val reserved = reservationRepository.save(newReservation)

    restaurant.decreaseRemainingTable()
    restaurantRepository.save(restaurant)

    return MakeReservationResponse(reserved.reservedAt)
  }
}
