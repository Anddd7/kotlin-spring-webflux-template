package com.github.anddd7.repository

import com.github.anddd7.entity.Product
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * can replaced by ReactiveCrudRepository, like jpa repository
 *
 * @see org.springframework.data.repository.reactive.ReactiveCrudRepository
 */
@Repository
class ProductRepository(private val databaseClient: DatabaseClient) {
  fun findAll(): Flux<Product> = databaseClient.select().from<Product>().fetch().all()
  fun getOne(id: Int): Mono<Product> = databaseClient.select().from<Product>()
      .matching(
          where("id").`is`(id)
      )
      .fetch().first()
}
