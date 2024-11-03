package com.example.concurrencycasestudy

import com.example.concurrencycasestudy.reservation.MakeReservationRequest
import com.example.concurrencycasestudy.reservation.MakeReservationService
import com.example.concurrencycasestudy.reservation.ReservationRepository
import com.example.concurrencycasestudy.restaurant.Restaurant
import com.example.concurrencycasestudy.restaurant.RestaurantRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.Duration

@Testcontainers
@SpringBootTest
@Transactional
class BasicFunctionalityTest @Autowired constructor(
  private val sut: MakeReservationService,
  private val restaurantRepository: RestaurantRepository,
  private val reservationRepository: ReservationRepository
) {

  companion object {
    @JvmStatic
    @Container
    @ServiceConnection
    val psql = PostgreSQLContainer("postgres")
      // .withMinimumRunningDuration(Duration.ofSeconds(10)) // see https://github.com/testcontainers/testcontainers-java/discussions/7299
  }

  @BeforeEach
  fun setUp() {
    restaurantRepository.save(Restaurant(7777, "TGI", 10))
  }
  @Test
  fun `예약가능`() {
    sut.reserve(MakeReservationRequest(123, 7777))
  }

  @Test
  fun `중복 예약은 불가하다`() {
    sut.reserve(MakeReservationRequest(123, 7777))
    sut.reserve(MakeReservationRequest(123, 7777))
  }
}
