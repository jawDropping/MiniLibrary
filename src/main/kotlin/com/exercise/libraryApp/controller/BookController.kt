package com.exercise.libraryApp.controller

import com.exercise.libraryApp.model.dto.*
import com.exercise.libraryApp.service.BookService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.UUID


@RestController
@RequestMapping("/api/v1/books")
class BookController(
    private val bookService: BookService
) {

    // Example call: GET /api/books?page=0&size=20&sort=title,asc
    @GetMapping
    fun getAllBooks(pageable: Pageable): ResponseEntity<Page<BookResponse>> {
        val books = bookService.getAllBooks(pageable)
        return ResponseEntity.ok(books)
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: UUID): ResponseEntity<BookResponse> {
        return ResponseEntity.ok(bookService.getBookById(id))
    }

    // 2. LOCATION HEADER APPLIED
    @PostMapping
    fun createBook(
        @Valid @RequestBody request: CreateBookRequest,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<BookResponse> {
        val response = bookService.createBook(request)
        val location = uriBuilder.path("/api/books/{id}").buildAndExpand(response.id).toUri()

        return ResponseEntity.created(location).body(response)
    }

    @PatchMapping("/{id}")
    fun updateBook(
        @PathVariable id: UUID,
        @Valid @RequestBody request: PatchBookRequest
    ): ResponseEntity<BookResponse> {
        return ResponseEntity.ok(bookService.updateBook(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteBook(@PathVariable id: UUID): ResponseEntity<Void> {
        bookService.deleteBook(id)
        return ResponseEntity.noContent().build()
    }
}