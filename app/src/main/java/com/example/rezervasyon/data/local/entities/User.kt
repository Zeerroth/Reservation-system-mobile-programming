package com.example.rezervasyon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User entity representing both regular users and admin users in the database
 * 
 * @property id Auto-generated unique identifier
 * @property email User's email address (used for login)
 * @property password User's password (in production, this should be hashed)
 * @property name User's full name
 * @property isAdmin Flag indicating if user has admin privileges
 * @property createdAt Timestamp when the user account was created
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String,
    val isAdmin: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
