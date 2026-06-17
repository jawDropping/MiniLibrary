package com.exercise.libraryApp.mapper


import com.exercise.libraryApp.model.dto.CreateStudentRequest
import com.exercise.libraryApp.model.dto.StudentResponse
import com.exercise.libraryApp.model.entity.Student

// Request -> Entity
fun CreateStudentRequest.toEntity() = Student(
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email
    // id, isActive, and createdAt use their default/generated values
)

// Entity -> Response
fun Student.toResponse() = StudentResponse(
    id = this.id!!, // Safe to assert non-null because it comes from the DB
    firstName = this.firstName,
    lastName = this.lastName,
    email = this.email,
    isActive = this.isActive,
    createdAt = this.createdAt
)