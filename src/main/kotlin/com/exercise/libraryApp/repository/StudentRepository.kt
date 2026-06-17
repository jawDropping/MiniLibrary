package com.exercise.libraryApp.repository

import com.exercise.libraryApp.model.entity.Student
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface StudentRepository: JpaRepository<Student, UUID>