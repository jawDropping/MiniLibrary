package com.exercise.libraryApp.service

import com.exercise.libraryApp.mapper.toResponse
import com.exercise.libraryApp.model.dto.BorrowBookRequest
import com.exercise.libraryApp.model.dto.BorrowedBookResponse
import com.exercise.libraryApp.model.entity.BorrowedBooks
import com.exercise.libraryApp.repository.BookRepository
import com.exercise.libraryApp.repository.BorrowedBookRepository
import com.exercise.libraryApp.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime
import java.util.UUID

@Service
class BorrowedBookService(
    private val borrowedBookRepository: BorrowedBookRepository,
    private val bookRepository: BookRepository,
    private val studentRepository: StudentRepository
) {

    fun getAllBorrowedBooks(): List<BorrowedBookResponse> {
        return borrowedBookRepository.findAll().map { it.toResponse() }
    }

    @Transactional
    fun borrowBook(request: BorrowBookRequest): BorrowedBookResponse {
        val student = studentRepository.findById(request.studentId)
            .orElseThrow { Exception("Student not found") }

        val book = bookRepository.findById(request.bookId)
            .orElseThrow { Exception("Book not found") }

        // RULE 1: Cannot borrow archived books
        if (book.isArchived) {
            throw IllegalStateException("Cannot borrow: This book is archived.")
        }

        // RULE 2: Calculate available copies
        val activeLoans = borrowedBookRepository.countByBookIdAndReturnedAtIsNull(book.id!!)
        val availableCopies = book.totalCopies - activeLoans

        // Cannot borrow if there is only 1 copy remaining (must stay in library)
        if (availableCopies <= 1) {
            throw IllegalStateException("Cannot borrow: Only 1 reference copy remains in the library.")
        }

        // Save and return using your mapper (which dynamically calculates the BorrowStatus enum!)
        val borrowedBook = BorrowedBooks(student = student, book = book)
        return borrowedBookRepository.save(borrowedBook).toResponse()
    }

    @Transactional
    fun returnBook(borrowId: UUID): BorrowedBookResponse {
        val borrowedBook = borrowedBookRepository.findById(borrowId)
            .orElseThrow { Exception("Borrow record not found") }

        // Prevent double-returning
        if (borrowedBook.returnedAt != null) {
            throw IllegalStateException("This book has already been returned.")
        }

        // Mark as returned and save
        borrowedBook.returnedAt = OffsetDateTime.now()
        return borrowedBookRepository.save(borrowedBook).toResponse()
    }
}