package com.exercise.libraryApp.mapper

import com.exercise.libraryApp.model.dto.BookResponse
import com.exercise.libraryApp.model.dto.CreateBookRequest
import com.exercise.libraryApp.model.entity.Books
import com.exercise.libraryApp.model.entity.BorrowedBooks
import com.exercise.libraryApp.model.entity.enums.BorrowStatus
import com.exercise.libraryApp.model.entity.extensions.status

// Request -> Entity
fun CreateBookRequest.toEntity() = Books(
    title = this.title,
    author = this.author,
    isbn = this.isbn,
    genre = this.genre,
    totalCopies = this.totalCopies,
    isArchived = false // Explicitly default to false on creation
)

// Entity -> Response (Requires context of active loans)
fun Books.toResponse(activeLoans: List<BorrowedBooks>): BookResponse {

    // Calculate the derived fields
    val borrowedCount = activeLoans.size
    val overdueCount = activeLoans.count { it.status() == BorrowStatus.OVERDUE }
    val availableCount = this.totalCopies - borrowedCount

    return BookResponse(
        id = this.id!!,
        title = this.title,
        author = this.author,
        isbn = this.isbn,
        genre = this.genre,
        totalCopies = this.totalCopies,
        availableCopies = availableCount,
        borrowedCopies = borrowedCount,
        overdueCopies = overdueCount,
        isArchived = this.isArchived
    )
}