package com.github.anddd7.repository

import com.github.anddd7.entity.Book
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class BookRepository(private val databaseClient: DatabaseClient) {
  fun findById(id: Int) = databaseClient
      .select().from<Book>()
      .matching(
          where("id").`is`(id)
      )
      .fetch()
      .first()
}
