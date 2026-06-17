package com.exercise.libraryApp.service

import com.exercise.libraryApp.mapper.toEntity
import com.exercise.libraryApp.mapper.toResponse
import com.exercise.libraryApp.model.dto.CreateStudentRequest
import com.exercise.libraryApp.model.dto.PatchStudentRequest
import com.exercise.libraryApp.model.dto.StudentResponse

import com.exercise.libraryApp.repository.StudentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class StudentService(
    private val studentRepository: StudentRepository
) {

    @Transactional
    fun createStudent(request: CreateStudentRequest): StudentResponse {
        val student = request.toEntity()
        return studentRepository.save(student).toResponse()
    }

    fun getStudentById(id: UUID): StudentResponse {
        val student = studentRepository.findById(id)
            .orElseThrow { Exception("Student not found") }
        return student.toResponse()
    }

    fun getAllStudents(): List<StudentResponse> {
        return studentRepository.findAll().map { it.toResponse() }
    }

    @Transactional
    fun updateStudent(id: UUID, request: PatchStudentRequest): StudentResponse {
        val student = studentRepository.findById(id)
            .orElseThrow { Exception("Student not found") }

        // Update fields only if they are provided in the PATCH request
        request.firstName?.let { student.firstName = it }
        request.lastName?.let { student.lastName = it }
        request.email?.let { student.email = it }
        request.isActive?.let { student.isActive = it }

        return studentRepository.save(student).toResponse()
    }

    @Transactional
    fun deleteStudent(id: UUID) {
        if (!studentRepository.existsById(id)) {
            throw Exception("Student not found")
        }
        studentRepository.deleteById(id)
    }
}