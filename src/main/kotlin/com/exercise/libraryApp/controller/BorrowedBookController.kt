package com.exercise.libraryApp.controller

import com.exercise.libraryApp.model.dto.BorrowBookRequest
import com.exercise.libraryApp.model.dto.BorrowedBookResponse
import com.exercise.libraryApp.service.BorrowedBookService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.util.UUID

@RestController
@RequestMapping("/api/v1/borrowed-books")
class BorrowedBookController(
    private val borrowedBookService: BorrowedBookService
) {


    @GetMapping
    fun getAllBorrowedBooks(): List<BorrowedBookResponse> {
        return borrowedBookService.getAllBorrowedBooks()
    }

    @PostMapping
    fun borrowBook(
        @Valid @RequestBody request: BorrowBookRequest,
        uriBuilder: UriComponentsBuilder
    ): ResponseEntity<BorrowedBookResponse> {
        val response = borrowedBookService.borrowBook(request)
        val location = uriBuilder.path("/api/borrowed-books/{id}")
            .buildAndExpand(response.id).toUri()

        return ResponseEntity.created(location).body(response)
    }

    @PatchMapping("/{id}/return")
    fun returnBook(@PathVariable id: UUID): ResponseEntity<BorrowedBookResponse> {
        return ResponseEntity.ok(borrowedBookService.returnBook(id))
    }
}