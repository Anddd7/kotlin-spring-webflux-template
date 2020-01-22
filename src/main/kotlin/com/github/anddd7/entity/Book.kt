package com.github.anddd7.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("books")
data class Book(
    @Id
    val id: Int = 0,
    val name: String,
    val pageCount: Int,
    val authorId: Int
)
