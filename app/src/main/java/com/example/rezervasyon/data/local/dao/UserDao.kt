package com.example.rezervasyon.data.local.dao

import androidx.room.*
import com.example.rezervasyon.data.local.entities.User
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for User operations
 */
@Dao
interface UserDao {
    
    /**
     * Insert a new user
     * @return the ID of the inserted user
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User): Long
    
    /**
     * Get user by email and password (for login)
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): User?
    
    /**
     * Get user by ID
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?
    
    /**
     * Get user by ID as Flow (for reactive updates)
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserByIdFlow(userId: Long): Flow<User?>
    
    /**
     * Check if email already exists
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun emailExists(email: String): Int
    
    /**
     * Get all users
     */
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
    
    /**
     * Update user
     */
    @Update
    suspend fun update(user: User)
    
    /**
     * Delete user
     */
    @Delete
    suspend fun delete(user: User)
}
