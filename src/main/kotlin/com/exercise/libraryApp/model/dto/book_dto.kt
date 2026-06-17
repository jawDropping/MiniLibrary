package com.exercise.libraryApp.model.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import java.util.UUID

//------------------------REQUESTS

data class CreateBookRequest(
    @field:NotBlank(message = "Title is required") val title: String,
    @field:NotBlank(message = "Author is required") val author: String,
    @field:NotBlank(message = "ISBN is required") val isbn: String,
    @field:NotBlank(message = "Genre is required") val genre: String,
    @field:Min(0, message = "Total copies cannot be negative") val totalCopies: Int
)

data class PatchBookRequest(
    val title: String? = null,
    val author: String? = null,
    val isbn: String? = null,
    val genre: String? = null,
    @field:Min(0) val totalCopies: Int? = null,
    val isArchived: Boolean? = null
)

//------------------------RESPONSE

data class BookResponse(
    val id: UUID,
    val title: String,
    val author: String,
    val isbn: String,
    val genre: String,
    val totalCopies: Int,
    val availableCopies: Int,
    val borrowedCopies: Int,
    val overdueCopies: Int,
    val isArchived: Boolean
)