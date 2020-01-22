package com.github.anddd7.repository

import com.github.anddd7.entity.Author
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Repository

@Repository
class AuthorRepository(private val databaseClient: DatabaseClient) {
  fun findById(id: Int) = databaseClient
      .select().from<Author>()
      .matching(
          where("id").`is`(id)
      )
      .fetch()
      .first()
}
