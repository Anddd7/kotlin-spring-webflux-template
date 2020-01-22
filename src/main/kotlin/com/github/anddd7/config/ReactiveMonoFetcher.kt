package com.github.anddd7.config

import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors.toList

interface ReactiveFetcher<T> : DataFetcher<CompletableFuture<T>> {
  fun getType(): String
  fun getFieldName(): String
}

interface ReactiveMonoFetcher<T> : ReactiveFetcher<T> {
  fun fetch(environment: DataFetchingEnvironment): Mono<T>

  override fun get(environment: DataFetchingEnvironment) =
      fetch(environment).toFuture()
}

interface ReactiveFluxFetcher<T> : ReactiveFetcher<List<T>> {
  fun fetch(environment: DataFetchingEnvironment): Flux<T>

  override fun get(environment: DataFetchingEnvironment) =
      fetch(environment).collect(toList()).toFuture()
}
