package com.exercise.libraryApp


import com.exercise.libraryApp.model.dto.CreateStudentRequest
import com.exercise.libraryApp.model.dto.PatchStudentRequest
import com.exercise.libraryApp.model.entity.Student
import com.exercise.libraryApp.repository.StudentRepository
import com.exercise.libraryApp.service.StudentService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class StudentServiceTest {

    private val studentRepository = mockk<StudentRepository>()
    private val studentService = StudentService(studentRepository)

    @Test
    fun `should create student successfully`() {
        val request = CreateStudentRequest(
            "John",
            "Doe",
            "john@example.com")
        val savedStudent = Student(
            UUID.randomUUID(),
            "John",
            "Doe",
            "john@example.com",
            true)

        every { studentRepository.save(any()) } returns savedStudent

        // Act
        val result = studentService.createStudent(request)

        // Assert
        assertEquals(
            "John",
            result.firstName)
        verify(exactly = 1) {
            studentRepository.save(any()) }
    }

    @Test
    fun `should get student by id successfully`() {
        // Arrange
        val id = UUID.randomUUID()
        val student = Student(
            id,
            "John",
            "Doe",
            "john@example.com",
            true)
        every { studentRepository.findById(id) } returns Optional.of(student)
        val result = studentService.getStudentById(id)


        assertEquals(id, result.id)
    }

    @Test
    fun `should throw exception when student not found`() {
        val id = UUID.randomUUID()
        every { studentRepository.findById(id) } returns Optional.empty()

        // Act & Assert
        assertThrows<Exception>("Student not found") {
            studentService.getStudentById(id)
        }
    }

    @Test
    fun `should update student fields correctly`() {
        // Arrange
        val id = UUID.randomUUID()
        val existingStudent = Student(id,
            "John",
            "Old",
            "john@example.com",
            true)
        val patchRequest = PatchStudentRequest(
            firstName = "Johnny",
            lastName = null,
            email = null,
            isActive = null)

        every { studentRepository.findById(id) } returns Optional.of(existingStudent)
        every { studentRepository.save(any()) } returns existingStudent // Return the modified entity

        // Act
        val result = studentService.updateStudent(id, patchRequest)

        // Assert
        assertEquals("Johnny", result.firstName)
        verify { studentRepository.save(match { it.firstName == "Johnny" }) }
    }

    @Test
    fun `should delete student when exists`() {
        // Arrange
        val id = UUID.randomUUID()
        every { studentRepository.existsById(id) } returns true
        every { studentRepository.deleteById(id) } returns Unit

        // Act
        studentService.deleteStudent(id)

        // Assert
        verify(exactly = 1) { studentRepository.deleteById(id) }
    }
}