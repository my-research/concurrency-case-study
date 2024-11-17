package com.example.concurrencycasestudy

import com.example.concurrencycasestudy.reservation.MakeReservationRequest
import com.example.concurrencycasestudy.reservation.MakeReservationService
import com.example.concurrencycasestudy.restaurant.Restaurant
import com.example.concurrencycasestudy.restaurant.RestaurantRepository
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.lang.ref.WeakReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Testcontainers
@SpringBootTest
class MultiThreaded_ReserveTest @Autowired constructor(
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

        val a: String? by Weak("a")
    }

    @Test
    fun `동시에 10개의 스레드가 요청을 수행`() {

        val numberOfThreads = 10
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(1)
        val tasks = (1..numberOfThreads).map { id ->
            Runnable {
                latch.await() // 모든 스레드가 이 지점에서 대기
                try {
                    sut.reserve(MakeReservationRequest(id.toLong(), 7777))
                    println("thread $id 의 유저($id) 예약 완료")
                } catch (e: Exception) {
                    println("Error in thread $id: ${e.message}")
                }
            }
        }

        tasks.forEach { executorService.submit(it) }

        latch.countDown()

        executorService.shutdown()
        executorService.awaitTermination(1, TimeUnit.MINUTES)

        val restaurant = restaurantRepository.findById(7777).orElseThrow()

        restaurant.numberOfRemainingTable shouldBe 0 // 실제로는 9. 동시에 10개의 요청을 실행하므로 count 는 하나만 차감함
    }
}