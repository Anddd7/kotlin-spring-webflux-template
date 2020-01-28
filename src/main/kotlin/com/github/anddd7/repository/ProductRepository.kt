package com.github.anddd7.repository

import com.github.anddd7.entity.Product
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : ReactiveCrudRepository<Product, Int>
