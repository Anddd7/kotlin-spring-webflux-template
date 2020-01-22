package com.github.anddd7.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("authors")
data class Author(
    @Id
    val id: Int = 0,
    val firstName: String,
    val lastName: String
)
