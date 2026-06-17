package com.exercise.libraryApp.repository

import com.exercise.libraryApp.model.entity.BorrowedBooks
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BorrowedBookRepository : JpaRepository<BorrowedBooks, UUID> {

    fun countByBookIdAndReturnedAtIsNull(bookId: UUID): Int
    fun findByBookIdAndReturnedAtIsNull(bookId: UUID): List<BorrowedBooks>
    fun findByReturnedAtIsNull(): List<BorrowedBooks>
    fun findByBookIdInAndReturnedAtIsNull(bookIds: Collection<UUID>): List<BorrowedBooks>
}