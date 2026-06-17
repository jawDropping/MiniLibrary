package com.exercise.libraryApp

import com.exercise.libraryApp.model.dto.BorrowBookRequest
import com.exercise.libraryApp.model.entity.Books
import com.exercise.libraryApp.model.entity.BorrowedBooks
import com.exercise.libraryApp.model.entity.Student
import com.exercise.libraryApp.repository.BookRepository
import com.exercise.libraryApp.repository.BorrowedBookRepository
import com.exercise.libraryApp.repository.StudentRepository
import com.exercise.libraryApp.service.BorrowedBookService
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime
import java.util.*

class BorrowedBookServiceTest {

    private lateinit var borrowedBookRepository: BorrowedBookRepository
    private lateinit var bookRepository: BookRepository
    private lateinit var studentRepository: StudentRepository
    private lateinit var service: BorrowedBookService

    @BeforeEach
    fun setup() {
        borrowedBookRepository = mockk()
        bookRepository = mockk()
        studentRepository = mockk()
        service = BorrowedBookService(borrowedBookRepository, bookRepository, studentRepository)
    }

    @Test
    fun `borrowBook should save successfully when rules are met`() {
        // Arrange
        val studentId = UUID.randomUUID()
        val bookId = UUID.randomUUID()
        val request = BorrowBookRequest(studentId, bookId)

        // Ensure you provide values for ALL fields in your Entity constructor
        val student = Student(id = studentId, firstName = "John", lastName = "Doe", email = "test@test.com", isActive = true)
        val book = Books(id = bookId, title = "Kotlin Basics", author = "Author", isbn = "123", genre = "Tech", totalCopies = 5, isArchived = false)

        every { studentRepository.findById(studentId) } returns Optional.of(student)
        every { bookRepository.findById(bookId) } returns Optional.of(book)
        every { borrowedBookRepository.countByBookIdAndReturnedAtIsNull(bookId) } returns 2 // 5 - 2 = 3 available (OK)
        every { borrowedBookRepository.save(any()) } answers { firstArg() }

        // Act
        val result = service.borrowBook(request)

        // Assert
        assertThat(result).isNotNull
        verify(exactly = 1) { borrowedBookRepository.save(any()) }
    }

    @Test
    fun `borrowBook should throw exception if book is archived`() {
        // Arrange
        val request = BorrowBookRequest(UUID.randomUUID(), UUID.randomUUID())
        // Set isArchived = true
        val book = Books(id = request.bookId, title = "Archived Book", author = "X", isbn = "Y", genre = "Z", totalCopies = 5, isArchived = true)

        every { studentRepository.findById(any()) } returns Optional.of(Student(id = UUID.randomUUID(), firstName = "A", lastName = "B", email = "c@d.com", isActive = true))
        every { bookRepository.findById(request.bookId) } returns Optional.of(book)

        // Act & Assert
        val ex = assertThrows<IllegalStateException> { service.borrowBook(request) }
        assertThat(ex.message).isEqualTo("Cannot borrow: This book is archived.")
    }

    @Test
    fun `borrowBook should throw exception if only 1 copy remains`() {
        // Arrange: 5 total - 4 active = 1 available. System must keep 1 in lib.
        val request = BorrowBookRequest(UUID.randomUUID(), UUID.randomUUID())
        val book = Books(id = request.bookId, title = "Book", author = "A", isbn = "B", genre = "C", totalCopies = 5, isArchived = false)

        every { studentRepository.findById(any()) } returns Optional.of(Student(id = UUID.randomUUID(), firstName = "A", lastName = "B", email = "c@d.com", isActive = true))
        every { bookRepository.findById(request.bookId) } returns Optional.of(book)
        every { borrowedBookRepository.countByBookIdAndReturnedAtIsNull(request.bookId) } returns 4

        // Act & Assert
        val ex = assertThrows<IllegalStateException> { service.borrowBook(request) }
        assertThat(ex.message).contains("Only 1 reference copy remains")
    }

    @Test
    fun `returnBook should update returnedAt date successfully`() {
        // Arrange
        val borrowId = UUID.randomUUID()
        val student = Student(id = UUID.randomUUID(), firstName = "A", lastName = "B", email = "c@d.com", isActive = true)
        val book = Books(id = UUID.randomUUID(), title = "Book", author = "A", isbn = "B", genre = "C", totalCopies = 5, isArchived = false)

        val borrowedBook = BorrowedBooks(id = borrowId, student = student, book = book, returnedAt = null)

        every { borrowedBookRepository.findById(borrowId) } returns Optional.of(borrowedBook)
        every { borrowedBookRepository.save(any()) } answers { firstArg() }

        // Act
        val result = service.returnBook(borrowId)

        // Assert
        assertThat(result.returnedAt).isNotNull
        verify(exactly = 1) { borrowedBookRepository.save(any()) }
    }
}