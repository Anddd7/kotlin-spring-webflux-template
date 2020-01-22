package com.github.anddd7.config

import graphql.schema.DataFetcher

interface SpringDataFetcher<T> : DataFetcher<T> {
  fun getType():String
  fun getFieldName():String
}
