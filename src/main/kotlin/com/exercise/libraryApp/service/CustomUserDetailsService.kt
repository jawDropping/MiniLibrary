package com.exercise.libraryApp.security

import com.exercise.libraryApp.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found with email: $email")

        // Map your entity to Spring Security's User object
        return User.builder()
            .username(user.email)
            .password(user.passwordHash)
            .roles("STUDENT") // Or map roles from your entity
            .build()
    }
}