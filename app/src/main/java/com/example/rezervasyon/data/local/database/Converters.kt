package com.example.rezervasyon.data.local.database

import androidx.room.TypeConverter
import com.example.rezervasyon.data.local.entities.ReservationStatus
import com.example.rezervasyon.data.local.entities.TripType

/**
 * Type converters for Room database
 * Used to convert enum types to/from database storage
 */
class Converters {
    
    @TypeConverter
    fun fromTripType(value: TripType): String {
        return value.name
    }
    
    @TypeConverter
    fun toTripType(value: String): TripType {
        return TripType.valueOf(value)
    }
    
    @TypeConverter
    fun fromReservationStatus(value: ReservationStatus): String {
        return value.name
    }
    
    @TypeConverter
    fun toReservationStatus(value: String): ReservationStatus {
        return ReservationStatus.valueOf(value)
    }
}
