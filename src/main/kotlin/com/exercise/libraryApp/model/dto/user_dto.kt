package com.exercise.libraryApp.model.dto

import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val student: StudentResponse
)

data class AuthResponse(
    val token: String,
    val user: UserResponse
)