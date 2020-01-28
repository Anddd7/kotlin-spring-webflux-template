package com.github.anddd7.config

import graphql.GraphQL
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
  fun graphQL(reactiveMonoFetchers: List<ReactiveFetcher<*>>): GraphQL {
    val typeRegistry = getTypeDefinitionRegistry()
    val runtimeWiring = getRuntimeWiring(reactiveMonoFetchers)
    val graphQLSchema = SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring)

    return GraphQL.newGraphQL(graphQLSchema).build()
  }

  private fun getTypeDefinitionRegistry() =
      SchemaParser().parse(String(Files.readAllBytes(graphQLDefinition.file.toPath())))

  private fun getRuntimeWiring(reactiveMonoFetchers: List<ReactiveFetcher<*>>): RuntimeWiring =
      newRuntimeWiring().apply {
        reactiveMonoFetchers.forEach { fetcher ->
          type(fetcher.getType()) { it.dataFetcher(fetcher.getFieldName(), fetcher) }
        }
      }.build()
}

