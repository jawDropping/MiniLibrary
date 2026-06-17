package com.exercise.libraryApp

import com.exercise.libraryApp.model.entity.Books
import com.exercise.libraryApp.model.dto.CreateBookRequest
import com.exercise.libraryApp.repository.BookRepository
import com.exercise.libraryApp.repository.BorrowedBookRepository
import com.exercise.libraryApp.service.BookService
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.*

class BookServiceTest {

    private lateinit var bookRepository: BookRepository
    private lateinit var borrowedBookRepository: BorrowedBookRepository
    private lateinit var bookService: BookService

    private lateinit var bookId: UUID
    private lateinit var book: Books
    private lateinit var request: CreateBookRequest

    @BeforeEach
    fun setUp() {
        bookRepository = mockk()
        borrowedBookRepository = mockk()

        bookService = BookService(bookRepository, borrowedBookRepository)

        bookId = UUID.randomUUID()

        request = CreateBookRequest(
            title = "Clean Code",
            author = "Robert Martin",
            isbn = "123456789",
            genre = "Programming",
            totalCopies = 5
        )

        book = Books(
            id = bookId,
            title = request.title,
            author = request.author,
            isbn = request.isbn,
            genre = request.genre,
            totalCopies = request.totalCopies,
            isArchived = false
        )
    }

    // ---------------- CREATE BOOK ----------------
    @Test
    fun `should create book successfully`() {
        every { bookRepository.save(any()) } returns book

        val result = bookService.createBook(request)

        assertEquals("Clean Code", result.title)

        verify(exactly = 1) { bookRepository.save(any()) }
    }

    // ---------------- GET BY ID ----------------
    @Test
    fun `should return book by id`() {
        every { bookRepository.findById(bookId) } returns Optional.of(book)
        every { borrowedBookRepository.findByBookIdAndReturnedAtIsNull(bookId) } returns emptyList()

        val result = bookService.getBookById(bookId)

        assertEquals(bookId, result.id)
        assertEquals("Clean Code", result.title)
    }

    @Test
    fun `should throw exception when book not found`() {
        every { bookRepository.findById(bookId) } returns Optional.empty()

        val exception = assertThrows(Exception::class.java) {
            bookService.getBookById(bookId)
        }

        assertEquals("Book not found", exception.message)
    }

    // ---------------- DELETE BOOK ----------------
    @Test
    fun `should delete book successfully`() {
        every { bookRepository.existsById(bookId) } returns true
        every { bookRepository.deleteById(bookId) } just Runs

        bookService.deleteBook(bookId)

        verify { bookRepository.deleteById(bookId) }
    }

    @Test
    fun `should throw when deleting non-existing book`() {
        every { bookRepository.existsById(bookId) } returns false

        val exception = assertThrows(Exception::class.java) {
            bookService.deleteBook(bookId)
        }

        assertEquals("Book not found", exception.message)
    }
}
