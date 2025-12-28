package com.example.rezervasyon.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Enum representing the type of transportation
 */
enum class TripType {
    BUS,    // Otobüs
    FLIGHT  // Uçak
}

/**
 * Trip entity representing bus or flight trips
 * 
 * @property id Auto-generated unique identifier
 * @property type Type of trip (BUS or FLIGHT)
 * @property companyName Name of the bus/airline company
 * @property departure Departure city
 * @property destination Destination city
 * @property date Trip date in format "2025-01-15"
 * @property time Trip time in format "14:30"
 * @property price Price per seat
 * @property totalSeats Total number of seats (e.g., 40 for bus, 180 for flight)
 * @property createdAt Timestamp when the trip was created
 */
@Entity(tableName = "trips")
data class Trip(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TripType,
    val companyName: String,
    val departure: String,
    val destination: String,
    val date: String,  // Format: "YYYY-MM-DD"
    val time: String,  // Departure time - Format: "HH:mm"
    val arrivalTime: String, // Arrival time - Format: "HH:mm"
    val price: Double,
    val totalSeats: Int,
    val createdAt: Long = System.currentTimeMillis()
)
