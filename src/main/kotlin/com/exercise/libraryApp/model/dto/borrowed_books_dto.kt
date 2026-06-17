package com.exercise.libraryApp.model.dto

import com.exercise.libraryApp.model.entity.enums.BorrowStatus
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.UUID

//------------------------REQUESTS

data class BorrowBookRequest(
    @field:NotNull(message = "Student ID is required") val studentId: UUID,
    @field:NotNull(message = "Book ID is required") val bookId: UUID
)

//------------------------RESPONSE

data class BorrowedBookResponse(
    val id: UUID,
    val studentName: String,
    val bookTitle: String,
    val borrowedAt: OffsetDateTime,
    val returnedAt: OffsetDateTime?,
    val status: BorrowStatus
)