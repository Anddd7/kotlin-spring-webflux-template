package com.github.anddd7.repository

import com.github.anddd7.entity.Book
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : ReactiveCrudRepository<Book, Int>
