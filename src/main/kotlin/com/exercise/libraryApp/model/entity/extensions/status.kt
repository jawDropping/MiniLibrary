package com.exercise.libraryApp.model.entity.extensions

import com.exercise.libraryApp.model.entity.BorrowedBooks
import com.exercise.libraryApp.model.entity.enums.BorrowStatus
import java.time.OffsetDateTime

fun BorrowedBooks.status() : BorrowStatus {
    val overdueDate = borrowedAt.plusHours(48)
    if (returnedAt != null){
        return BorrowStatus.RETURNED
    }


    return if (OffsetDateTime.now().isAfter(overdueDate)){
        BorrowStatus.OVERDUE
    }else{
        BorrowStatus.BORROWED
    }
}