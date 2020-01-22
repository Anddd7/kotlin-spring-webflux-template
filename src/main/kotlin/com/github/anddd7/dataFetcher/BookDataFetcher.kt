package com.github.anddd7.dataFetcher

import com.github.anddd7.config.ReactiveMonoFetcher
import com.github.anddd7.entity.Author
import com.github.anddd7.entity.Book
import com.github.anddd7.repository.AuthorRepository
import com.github.anddd7.repository.BookRepository
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component

@Component
class BookDataFetcher(private val bookRepository: BookRepository) : ReactiveMonoFetcher<Book> {
  override fun getType() = "Query"
  override fun getFieldName() = "bookById"
  override fun fetch(environment: DataFetchingEnvironment) =
      bookRepository.findById(environment.getArgument<String>("id").toInt())
}

@Component
class AuthorDataFetcher(private val authorRepository: AuthorRepository) : ReactiveMonoFetcher<Author> {
  override fun getType() = "Book"
  override fun getFieldName() = "author"
  override fun fetch(environment: DataFetchingEnvironment) =
      authorRepository.findById(environment.getSource<Book>().authorId)
}
