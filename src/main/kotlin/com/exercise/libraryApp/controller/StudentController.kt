package com.exercise.libraryApp.controller

import com.exercise.libraryApp.model.dto.CreateStudentRequest
import com.exercise.libraryApp.model.dto.PatchStudentRequest
import com.exercise.libraryApp.model.dto.StudentResponse
import com.exercise.libraryApp.service.StudentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/v1/students")
class StudentController(
    private val studentService: StudentService
) {

    @GetMapping
    fun getAllStudents(): List<StudentResponse> {
        return studentService.getAllStudents()
    }

    @GetMapping("/{id}")
    fun getStudent(@PathVariable id: UUID): StudentResponse {
        return studentService.getStudentById(id)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(@Valid @RequestBody request: CreateStudentRequest): StudentResponse {
        return studentService.createStudent(request)
    }

    @PatchMapping("/{id}")
    fun updateStudent(
        @PathVariable id: UUID,
        @Valid @RequestBody request: PatchStudentRequest
    ): StudentResponse {
        return studentService.updateStudent(id, request)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(@PathVariable id: UUID) {
        studentService.deleteStudent(id)
    }
}