package com.github.anddd7.repository

import com.github.anddd7.config.SpringDataFetcher
import com.github.anddd7.entity.Book
import graphql.schema.DataFetchingEnvironment
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.from
import org.springframework.data.r2dbc.query.Criteria
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture

@Component
class BookRepository(private val databaseClient: DatabaseClient) : SpringDataFetcher<CompletableFuture<Book>> {
  override fun getType() = "Query"
  override fun getFieldName() = "bookById"
  override fun get(environment: DataFetchingEnvironment) = databaseClient
      .select().from<Book>()
      .matching(
          Criteria.where("id").`is`(environment.getArgument<Int>("id"))
      )
      .fetch()
      .first()
      .toFuture()
}
