package com.exercise.libraryApp.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import jakarta.servlet.http.HttpServletRequest
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<Map<String, Any>> {
        // Extract every field that failed validation along with its custom message
        val fieldErrors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }

        val errorBody = mapOf(
            "timestamp" to OffsetDateTime.now().toString(),
            "status" to HttpStatus.BAD_REQUEST.value(),
            "error" to "Validation Failed",
            "errors" to fieldErrors, // This replaces the vague single message
            "path" to request.requestURI
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBody)
    }
}