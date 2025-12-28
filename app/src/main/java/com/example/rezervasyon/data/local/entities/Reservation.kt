package com.example.rezervasyon.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Enum representing reservation status
 */
enum class ReservationStatus {
    ACTIVE,    // Active reservation
    CANCELLED  // Cancelled reservation
}

/**
 * Reservation entity representing a user's trip reservation
 * 
 * @property id Auto-generated unique identifier
 * @property userId Foreign key to User
 * @property tripId Foreign key to Trip
 * @property seatNumbers Comma-separated list of seat numbers (e.g., "12,13,14")
 * @property totalPrice Total price for all seats
 * @property status Reservation status (ACTIVE or CANCELLED)
 * @property reservationDate Timestamp when the reservation was created
 */
@Entity(
    tableName = "reservations",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Reservation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: Long,
    val tripId: Long,
    val seatNumbers: String,  // Format: "1,2,3" - comma separated
    val totalPrice: Double,
    val status: ReservationStatus = ReservationStatus.ACTIVE,
    val reservationDate: Long = System.currentTimeMillis()
)
