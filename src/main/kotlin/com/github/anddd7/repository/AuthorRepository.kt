package com.github.anddd7.repository

import com.github.anddd7.config.SpringDataFetcher
import com.github.anddd7.entity.Author
import com.github.anddd7.entity.Book
import graphql.schema.DataFetchingEnvironment
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria.where
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

@Component
class AuthorRepository(private val databaseClient: DatabaseClient) : SpringDataFetcher<CompletableFuture<Author>> {
  override fun getType() = "Book"
  override fun getFieldName() = "author"
  override fun get(environment: DataFetchingEnvironment) = databaseClient
      .select().from<Author>()
      .matching(
          where("id").`is`(environment.getSource<Book>().authorId)
      )
      .fetch()
      .first()
      .toFuture()
}
