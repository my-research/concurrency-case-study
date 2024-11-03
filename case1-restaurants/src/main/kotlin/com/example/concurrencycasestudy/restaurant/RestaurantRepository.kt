package com.example.concurrencycasestudy.restaurant

import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantRepository: JpaRepository<Restaurant, Long> {
}
