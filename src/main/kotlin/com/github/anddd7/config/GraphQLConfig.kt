package com.github.anddd7.config

import com.github.anddd7.entity.Author
import com.github.anddd7.entity.Book
import graphql.GraphQL
import graphql.schema.DataFetchingEnvironment
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.RuntimeWiring.newRuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.nio.file.Files


@Configuration
class GraphQLConfig {

  @Value("classpath:schema.graphqls")
  lateinit var graphQLDefinition: Resource

  @Bean
  fun graphQL(springDataFetchers: List<SpringDataFetcher<*>>): GraphQL {
    val typeRegistry = getTypeDefinitionRegistry()
    val runtimeWiring = getRuntimeWiring(springDataFetchers)
    val graphQLSchema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)

    return GraphQL.newGraphQL(graphQLSchema).build()
  }

  private fun getTypeDefinitionRegistry() =
      SchemaParser().parse(Files.readString(graphQLDefinition.file.toPath()))

  private fun getRuntimeWiring(springDataFetchers: List<SpringDataFetcher<*>>): RuntimeWiring =
      newRuntimeWiring().apply {
        springDataFetchers.forEach { fetcher ->
          type(fetcher.getType()) { it.dataFetcher(fetcher.getFieldName(), fetcher) }
        }
      }.build()

  /**
   * data fetcher demos
   */
  @Bean("bookByIdDataFetcher")
  fun getBookByIdDataFetcher(): SpringDataFetcher<Book> {
    return object : SpringDataFetcher<Book> {
      override fun getType() = "Query"
      override fun getFieldName() = "bookById"
      override fun get(environment: DataFetchingEnvironment): Book? =
      books.firstOrNull { it.id == environment.getArgument<String>("id").toInt() }
    }
  }

  @Bean("authorDataFetcher")
  fun getAuthorDataFetcher(): SpringDataFetcher<Author> {
    return object : SpringDataFetcher<Author> {
      override fun getType() = "Book"
      override fun getFieldName() = "author"
      override fun get(environment: DataFetchingEnvironment): Author? {
        return authors.firstOrNull { it.id == environment.getSource<Book>().authorId }
      }
    }
  }

  val books: List<Book> = listOf(
      Book(1, "Harry Potter and the Philosopher's Stone", 223, 1),
      Book(1, "Moby Dick", 635, 2),
      Book(1, "Interview with the vampire", 371, 3)
  )

  private val authors: List<Author> = listOf(
      Author(1, "Joanne", "Rowling"),
      Author(2, "Herman", "Melville"),
      Author(3, "Anne", "Rice")
  )
}

