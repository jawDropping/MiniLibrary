package com.exercise.libraryApp.service

import com.exercise.libraryApp.mapper.toEntity
import com.exercise.libraryApp.mapper.toResponse
import com.exercise.libraryApp.model.dto.BookResponse
import com.exercise.libraryApp.model.dto.CreateBookRequest
import com.exercise.libraryApp.model.dto.PatchBookRequest
import com.exercise.libraryApp.repository.BookRepository
import com.exercise.libraryApp.repository.BorrowedBookRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val borrowedBookRepository: BorrowedBookRepository
) {

    @Transactional
    fun createBook(request: CreateBookRequest): BookResponse {
        val book = request.toEntity()
        val savedBook = bookRepository.save(book)

        return savedBook.toResponse(activeLoans = emptyList())
    }

    fun getBookById(id: UUID): BookResponse {
        val book = bookRepository.findById(id)
            .orElseThrow { Exception("Book not found") }

        val activeLoans = borrowedBookRepository.findByBookIdAndReturnedAtIsNull(id)
        return book.toResponse(activeLoans)
    }

    fun getAllBooks(pageable: Pageable): Page<BookResponse> {
        val books = bookRepository.findAll(pageable)
        val bookIds = books.content.map { it.id!! }
        val allActiveLoans = borrowedBookRepository.findByBookIdInAndReturnedAtIsNull(bookIds)
        val loansByBookId = allActiveLoans.groupBy { it.book.id }

        return books.map { book ->
            val bookLoans = loansByBookId[book.id] ?: emptyList()
            book.toResponse(bookLoans)
        }
    }

    @Transactional
    fun updateBook(id: UUID, request: PatchBookRequest): BookResponse {
        val book = bookRepository.findById(id)
            .orElseThrow { Exception("Book not found") }

        request.title?.let { book.title = it }
        request.author?.let { book.author = it }
        request.isbn?.let { book.isbn = it }
        request.genre?.let { book.genre = it }
        request.totalCopies?.let { book.totalCopies = it }
        request.isArchived?.let { book.isArchived = it }

        val updatedBook = bookRepository.save(book)
        val activeLoans = borrowedBookRepository.findByBookIdAndReturnedAtIsNull(id)

        return updatedBook.toResponse(activeLoans)
    }

    @Transactional
    fun deleteBook(id: UUID) {
        if (!bookRepository.existsById(id)) {
            throw Exception("Book not found")
        }
        bookRepository.deleteById(id)
    }
}