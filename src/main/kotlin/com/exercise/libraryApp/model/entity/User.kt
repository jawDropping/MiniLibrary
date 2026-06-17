package com.exercise.libraryApp.model.entity

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "users")
class User(
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(nullable = false)
    var passwordHash: String?,

    @OneToOne(cascade = [CascadeType.ALL], optional = false)
    @JoinColumn(name = "student_id", unique = true)
    var student: Student
)