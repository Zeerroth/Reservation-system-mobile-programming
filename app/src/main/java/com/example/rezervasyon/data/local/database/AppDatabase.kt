package com.example.rezervasyon.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.rezervasyon.data.local.dao.ReservationDao
import com.example.rezervasyon.data.local.dao.TripDao
import com.example.rezervasyon.data.local.dao.UserDao
import com.example.rezervasyon.data.local.entities.Reservation
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.data.local.entities.User

/**
 * Main Room database for the Rezervasyon Sistemi application
 * 
 * This database manages three main entities:
 * - User: User accounts (regular and admin)
 * - Trip: Bus and flight trips
 * - Reservation: User reservations for trips
 */
@Database(
    entities = [User::class, Trip::class, Reservation::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun tripDao(): TripDao
    abstract fun reservationDao(): ReservationDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        /**
         * Get database instance (Singleton pattern)
         * 
         * @param context Application context
         * @return AppDatabase instance
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rezervasyon_database"
                )
                    .fallbackToDestructiveMigration() // Recreate DB on version change
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
