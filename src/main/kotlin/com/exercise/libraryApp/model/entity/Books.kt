package com.exercise.libraryApp.model.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "books")
class Books (
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var title: String,
    var author: String,
    var isbn: String,
    var genre: String,
    var totalCopies: Int,
    var isArchived: Boolean
)