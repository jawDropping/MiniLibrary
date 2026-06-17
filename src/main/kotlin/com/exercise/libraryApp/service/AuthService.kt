package com.exercise.libraryApp.service

import LoginRequest
import UserRegistrationRequest
import com.exercise.libraryApp.mapper.toResponse
import com.exercise.libraryApp.model.dto.AuthResponse
import com.exercise.libraryApp.model.dto.UserResponse
import com.exercise.libraryApp.model.entity.Student
import com.exercise.libraryApp.model.entity.User
import com.exercise.libraryApp.repository.StudentRepository
import com.exercise.libraryApp.repository.UserRepository
import com.exercise.libraryApp.security.CustomUserDetailsService
import com.exercise.libraryApp.security.JwtService
import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val studentRepository: StudentRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val customUserDetailsService: CustomUserDetailsService
) {

    @Transactional
    fun register(request: UserRegistrationRequest): AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalStateException("Email already registered")
        }

        // 1. Create the Student profile
        val student = Student(
            firstName = request.firstName,
            lastName = request.lastName,
            email = request.email,
            isActive = true
        )

        // 2. Create the User (Auth record)
        val user = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password),
            student = student
        )
        val savedUser = userRepository.save(user)
        val userDetails = customUserDetailsService.loadUserByUsername(user.email)
        val token = jwtService.generateToken(userDetails)
        return AuthResponse(
            token = token,
            user = savedUser.toResponse(),
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw Exception("Invalid email or password")

        if (!passwordEncoder.matches(request.password, user.passwordHash)) {
            throw Exception("Invalid email or password")
        }
        val userDetails = customUserDetailsService.loadUserByUsername(request.email)
        val token = jwtService.generateToken(userDetails)

        return AuthResponse(
            token = token,
            user = user.toResponse(),
        )
    }
}