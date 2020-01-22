package com.github.anddd7.config

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
  fun getBookByIdDataFetcher(): SpringDataFetcher<Map<String, String>> {
    return object : SpringDataFetcher<Map<String, String>> {
      override fun getType() = "Book"
      override fun getFieldName() = "bookById"
      override fun get(environment: DataFetchingEnvironment): Map<String, String>? =
          books.firstOrNull { it["id"] == environment.getArgument<String>("id") }
    }
  }

  @Bean("authorDataFetcher")
  fun getAuthorDataFetcher(): SpringDataFetcher<Map<String, String>> {
    return object : SpringDataFetcher<Map<String, String>> {
      override fun getType() = "Author"
      override fun getFieldName() = "authorId"
      override fun get(environment: DataFetchingEnvironment): Map<String, String>? {
        return authors.firstOrNull { it["id"] == environment.getSource<Map<String, String>>()["authorId"] }
      }
    }
  }

  val books: List<Map<String, String>> = listOf(
      mapOf("id" to "book-1",
          "name" to "Harry Potter and the Philosopher's Stone",
          "pageCount" to "223",
          "authorId" to "author-1"),
      mapOf("id" to "book-2",
          "name" to "Moby Dick",
          "pageCount" to "635",
          "authorId" to "author-2"),
      mapOf("id" to "book-3",
          "name" to "Interview with the vampire",
          "pageCount" to "371",
          "authorId" to "author-3")
  )

  private val authors: List<Map<String, String>> = listOf(
      mapOf("id" to "author-1",
          "firstName" to "Joanne",
          "lastName" to "Rowling"),
      mapOf("id" to "author-2",
          "firstName" to "Herman",
          "lastName" to "Melville"),
      mapOf("id" to "author-3",
          "firstName" to "Anne",
          "lastName" to "Rice")
  )
}

