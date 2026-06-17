package com.exercise.libraryApp.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {

    private val secret = "my-very-secret-key-change-this-in-production-123456"

    private fun key() = Keys.hmacShaKeyFor(secret.toByteArray())

    fun generateToken(userDetails: UserDetails): String {
        val roles = userDetails.authorities.map { it.authority }

        return Jwts.builder()
            .subject(userDetails.username)
            .claim("roles", roles)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(key())
            .compact()
    }

    fun extractEmail(token: String): String {
        return getClaims(token).subject
    }

    fun isValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractEmail(token)
        return (username == userDetails.username) && !isExpired(token)
    }

    private fun isExpired(token: String): Boolean {
        return getClaims(token).expiration.before(Date())
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(key())
            .build()
            .parseSignedClaims(token)
            .payload
    }
}