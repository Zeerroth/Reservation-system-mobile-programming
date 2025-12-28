package com.example.rezervasyon.data.local.dao

import androidx.room.*
import com.example.rezervasyon.data.local.entities.Reservation
import com.example.rezervasyon.data.local.entities.ReservationStatus
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Reservation operations
 */
@Dao
interface ReservationDao {
    
    /**
     * Insert a new reservation
     * @return the ID of the inserted reservation
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reservation: Reservation): Long
    
    /**
     * Get all reservations for a specific user
     */
    @Query("SELECT * FROM reservations WHERE userId = :userId ORDER BY reservationDate DESC")
    fun getReservationsByUser(userId: Long): Flow<List<Reservation>>
    
    /**
     * Get active reservations for a specific user
     */
    @Query("SELECT * FROM reservations WHERE userId = :userId AND status = 'ACTIVE' ORDER BY reservationDate DESC")
    fun getActiveReservationsByUser(userId: Long): Flow<List<Reservation>>
    
    /**
     * Get reservations for a specific trip
     */
    @Query("SELECT * FROM reservations WHERE tripId = :tripId AND status = 'ACTIVE'")
    suspend fun getReservationsByTrip(tripId: Long): List<Reservation>
    
    /**
     * Get reservations for a specific trip as Flow
     */
    @Query("SELECT * FROM reservations WHERE tripId = :tripId AND status = 'ACTIVE'")
    fun getReservationsByTripFlow(tripId: Long): Flow<List<Reservation>>
    
    /**
     * Get reservation by ID
     */
    @Query("SELECT * FROM reservations WHERE id = :reservationId")
    suspend fun getReservationById(reservationId: Long): Reservation?
    
    /**
     * Update reservation (mainly for cancellation)
     */
    @Update
    suspend fun update(reservation: Reservation)
    
    /**
     * Cancel reservation by ID
     */
    @Query("UPDATE reservations SET status = 'CANCELLED' WHERE id = :reservationId")
    suspend fun cancelReservation(reservationId: Long)
    
    /**
     * Delete reservation
     */
    @Delete
    suspend fun delete(reservation: Reservation)
    
    /**
     * Get all reservations (for admin)
     */
    @Query("SELECT * FROM reservations ORDER BY reservationDate DESC")
    fun getAllReservations(): Flow<List<Reservation>>
}
