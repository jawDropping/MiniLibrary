package com.exercise.libraryApp.mapper

import com.exercise.libraryApp.model.dto.BorrowedBookResponse
import com.exercise.libraryApp.model.entity.BorrowedBooks
import com.exercise.libraryApp.model.entity.extensions.status
import java.util.UUID

// Entity -> Response
fun BorrowedBooks.toResponse() = BorrowedBookResponse(
    id = this.id ?: UUID.randomUUID(),
    studentName = "${this.student.firstName} ${this.student.lastName}",
    bookTitle = this.book.title,
    borrowedAt = this.borrowedAt,
    returnedAt = this.returnedAt,
    status = this.status() // Dynamically calculates the enum status
)