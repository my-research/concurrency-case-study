package com.example.concurrencycasestudy

import com.example.concurrencycasestudy.reservation.MakeReservationRequest
import com.example.concurrencycasestudy.reservation.MakeReservationService
import com.example.concurrencycasestudy.reservation.ReservationRepository
import com.example.concurrencycasestudy.restaurant.Restaurant
import com.example.concurrencycasestudy.restaurant.RestaurantRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql

@SpringBootTest
class ConcurrencyTest @Autowired constructor(
  private val sut: MakeReservationService,
  private val restaurantRepository: RestaurantRepository,
  private val reservationRepository: ReservationRepository
) {

  @BeforeEach
  fun setUp() {
    restaurantRepository.save(Restaurant(7777, "TGI Friday", 10))
  }

  @Test
  fun name() {
//    sut.reserve(MakeReservationRequest(1, 7777))
    val findById = restaurantRepository.findById(1)
  }
}
