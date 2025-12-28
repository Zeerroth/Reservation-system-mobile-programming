package com.example.rezervasyon.data.local.dao

import androidx.room.*
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.data.local.entities.TripType
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for Trip operations
 */
@Dao
interface TripDao {
    
    /**
     * Insert a new trip
     * @return the ID of the inserted trip
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: Trip): Long
    
    /**
     * Get all trips as Flow
     */
    @Query("SELECT * FROM trips ORDER BY date ASC, time ASC")
    fun getAllTrips(): Flow<List<Trip>>
    
    /**
     * Get trips by type
     */
    @Query("SELECT * FROM trips WHERE type = :type ORDER BY date ASC, time ASC")
    fun getTripsByType(type: TripType): Flow<List<Trip>>
    
    @Query("""
        SELECT * FROM trips 
        WHERE LOWER(departure) LIKE '%' || LOWER(:departure) || '%' 
        OR LOWER(destination) LIKE '%' || LOWER(:destination) || '%'
        OR LOWER(companyName) LIKE '%' || LOWER(:departure) || '%'
        ORDER BY date ASC, time ASC
    """)
    fun searchTrips(departure: String, destination: String): Flow<List<Trip>>
    
    /**
     * Search trips with all filters
     */
    @Query("""
        SELECT * FROM trips 
        WHERE (:type IS NULL OR type = :type)
        AND (:departure IS NULL OR departure LIKE '%' || :departure || '%')
        AND (:destination IS NULL OR destination LIKE '%' || :destination || '%')
        AND (:date IS NULL OR date = :date)
        ORDER BY date ASC, time ASC
    """)
    fun searchTripsWithFilters(
        type: TripType?,
        departure: String?,
        destination: String?,
        date: String?
    ): Flow<List<Trip>>
    
    /**
     * Get trip by ID
     */
    @Query("SELECT * FROM trips WHERE id = :tripId")
    suspend fun getTripById(tripId: Long): Trip?
    
    /**
     * Get trip by ID as Flow
     */
    @Query("SELECT * FROM trips WHERE id = :tripId")
    fun getTripByIdFlow(tripId: Long): Flow<Trip?>
    
    /**
     * Update trip
     */
    @Update
    suspend fun update(trip: Trip)
    
    /**
     * Delete trip
     */
    @Delete
    suspend fun delete(trip: Trip)
    
    /**
     * Delete trip by ID
     */
    @Query("DELETE FROM trips WHERE id = :tripId")
    suspend fun deleteById(tripId: Long)
}
