package com.exercise.libraryApp.repository

import com.exercise.libraryApp.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID>{
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
}