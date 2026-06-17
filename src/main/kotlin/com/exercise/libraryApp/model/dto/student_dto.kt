package com.exercise.libraryApp.model.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.OffsetDateTime
import java.util.UUID

//------------------------REQUESTS

data class CreateStudentRequest(
    @field:NotBlank(message = "First name is required") val firstName: String,
    @field:NotBlank(message = "Last name is required") val lastName: String,
    @field:NotBlank(message = "Email is required") @field:Email val email: String
)

data class PatchStudentRequest(
    val firstName: String? = null,
    val lastName: String? = null,
    @field:Email val email: String? = null,
    val isActive: Boolean? = null
)

//------------------------RESPONSE

data class StudentResponse(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val isActive: Boolean,
    val createdAt: OffsetDateTime
)