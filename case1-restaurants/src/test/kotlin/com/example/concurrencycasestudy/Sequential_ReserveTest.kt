package com.example.concurrencycasestudy

import com.example.concurrencycasestudy.reservation.MakeReservationRequest
import com.example.concurrencycasestudy.reservation.MakeReservationService
import com.example.concurrencycasestudy.restaurant.Restaurant
import com.example.concurrencycasestudy.restaurant.RestaurantRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.throwable.shouldHaveMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest
@Transactional
class Sequential_ReserveTest @Autowired constructor(
  private val sut: MakeReservationService,
  private val restaurantRepository: RestaurantRepository,
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
  fun `단일 요청에 대해서는 정상적으로 수행됨`() {
    (1..10).forEach {
      sut.reserve(MakeReservationRequest(it.toLong(), 7777))
    }

    val restaurant = restaurantRepository.findById(7777).orElseThrow()

    assertThat(restaurant.numberOfRemainingTable).isEqualTo(0)
    assertThat(restaurant.hasRemainingTable()).isFalse()

    shouldThrow<IllegalStateException> { sut.reserve(MakeReservationRequest(11, 7777)) }
      .shouldHaveMessage("restaurant(7777) is full booking")
  }
}
