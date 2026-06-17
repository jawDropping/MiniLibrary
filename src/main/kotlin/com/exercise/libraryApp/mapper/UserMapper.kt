package com.exercise.libraryApp.mapper

import com.exercise.libraryApp.model.dto.UserResponse
import com.exercise.libraryApp.model.entity.User

fun User.toResponse() = UserResponse(
    id = this.id!!,
    email = this.email,
    // We reuse your existing Student.toResponse() here!
    student = this.student.toResponse()
)