import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class UserRegistrationRequest(
    @field:Email(message = "Invalid email format") val email: String,
    @field:Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
        message = "Password must be at least 8 characters, include uppercase, lowercase, and a number") val password: String,

    @field:NotBlank val firstName: String,
    @field:NotBlank val lastName: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UpdateAccountRequest(
    val email: String? = null,
    val password: String? = null
)