package com.example.rezervasyon.utils

import android.content.Context
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.data.local.entities.TripType
import com.example.rezervasyon.data.local.entities.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Utility class to populate the database with sample data for testing
 * This ensures the app has data readily available for demonstration
 */
object SampleDataGenerator {
    
    /**
     * Populate database with sample users and trips
     * This is called on first app launch
     */
    fun populateDatabase(context: Context) {
        val database = AppDatabase.getDatabase(context)
        
        CoroutineScope(Dispatchers.IO).launch {
            // Check if data already exists
            val userDao = database.userDao()
            val tripDao = database.tripDao()
            
            // Insert sample users if database is empty
            if (userDao.emailExists("admin@test.com") == 0) {
                insertSampleUsers(database)
            }
            
            // Insert sample trips
            insertSampleTrips(database)
        }
    }
    
    private suspend fun insertSampleUsers(database: AppDatabase) {
        val userDao = database.userDao()
        
        // Admin users
        userDao.insert(
            User(
                email = "admin@test.com",
                password = "admin123",
                name = "Admin Kullanıcı",
                isAdmin = true
            )
        )
        
        userDao.insert(
            User(
                email = "admin2@test.com",
                password = "admin123",
                name = "Admin 2",
                isAdmin = true
            )
        )
        
        // Regular users
        userDao.insert(
            User(
                email = "user1@test.com",
                password = "user123",
                name = "Ahmet Yılmaz",
                isAdmin = false
            )
        )
        
        userDao.insert(
            User(
                email = "user2@test.com",
                password = "user123",
                name = "Ayşe Demir",
                isAdmin = false
            )
        )
        
        userDao.insert(
            User(
                email = "user3@test.com",
                password = "user123",
                name = "Mehmet Kaya",
                isAdmin = false
            )
        )
    }
    
    private suspend fun insertSampleTrips(database: AppDatabase) {
        val tripDao = database.tripDao()
        
        // Bus trips
        val busTrips = listOf(
            Trip(
                type = TripType.BUS,
                companyName = "Metro Turizm",
                departure = "İstanbul",
                destination = "Ankara",
                date = "2025-01-05",
                time = "09:00",
                arrivalTime = "14:30",
                price = 350.0,
                totalSeats = 45
            ),
            Trip(
                type = TripType.BUS,
                companyName = "Pamukkale",
                departure = "İstanbul",
                destination = "Ankara",
                date = "2025-01-05",
                time = "14:30",
                arrivalTime = "20:00",
                price = 325.0,
                totalSeats = 45
            ),
            Trip(
                type = TripType.BUS,
                companyName = "Kamil Koç",
                departure = "İzmir",
                destination = "Ankara",
                date = "2025-01-06",
                time = "10:00",
                arrivalTime = "17:30",
                price = 400.0,
                totalSeats = 40
            ),
            Trip(
                type = TripType.BUS,
                companyName = "Metro Turizm",
                departure = "Ankara",
                destination = "İzmir",
                date = "2025-01-07",
                time = "08:30",
                arrivalTime = "16:00",
                price = 380.0,
                totalSeats = 45
            ),
            Trip(
                type = TripType.BUS,
                companyName = "Ulusoy",
                departure = "İstanbul",
                destination = "İzmir",
                date = "2025-01-08",
                time = "20:00",
                arrivalTime = "08:30",
                price = 420.0,
                totalSeats = 50
            ),
            Trip(
                type = TripType.BUS,
                companyName = "Pamukkale",
                departure = "Ankara",
                destination = "Antalya",
                date = "2025-01-10",
                time = "19:00",
                arrivalTime = "06:30",
                price = 450.0,
                totalSeats = 45
            ),
            Trip(
                type = TripType.BUS,
                companyName = "Metro Turizm",
                departure = "İzmir",
                destination = "Bodrum",
                date = "2025-01-12",
                time = "11:00",
                arrivalTime = "14:30",
                price = 280.0,
                totalSeats = 40
            )
        )
        
        // Flight trips
        val flightTrips = listOf(
            Trip(
                type = TripType.FLIGHT,
                companyName = "Turkish Airlines",
                departure = "İstanbul",
                destination = "Ankara",
                date = "2025-01-05",
                time = "07:30",
                arrivalTime = "08:45",
                price = 850.0,
                totalSeats = 180
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "Pegasus",
                departure = "İstanbul",
                destination = "Ankara",
                date = "2025-01-05",
                time = "12:00",
                arrivalTime = "13:15",
                price = 650.0,
                totalSeats = 180
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "AnadoluJet",
                departure = "İstanbul",
                destination = "İzmir",
                date = "2025-01-06",
                time = "06:45",
                arrivalTime = "07:50",
                price = 750.0,
                totalSeats = 189
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "Turkish Airlines",
                departure = "İzmir",
                destination = "Ankara",
                date = "2025-01-06",
                time = "15:30",
                arrivalTime = "16:40",
                price = 900.0,
                totalSeats = 180
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "Pegasus",
                departure = "Ankara",
                destination = "Antalya",
                date = "2025-01-07",
                time = "09:15",
                arrivalTime = "10:30",
                price = 700.0,
                totalSeats = 180
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "SunExpress",
                departure = "İstanbul",
                destination = "Antalya",
                date = "2025-01-08",
                time = "10:45",
                arrivalTime = "12:05",
                price = 800.0,
                totalSeats = 189
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "Turkish Airlines",
                departure = "İstanbul",
                destination = "Bodrum",
                date = "2025-01-10",
                time = "08:00",
                arrivalTime = "09:10",
                price = 950.0,
                totalSeats = 180
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "Pegasus",
                departure = "Ankara",
                destination = "İstanbul",
                date = "2025-01-12",
                time = "18:30",
                arrivalTime = "19:45",
                price = 680.0,
                totalSeats = 180
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "AnadoluJet",
                departure = "İzmir",
                destination = "İstanbul",
                date = "2025-01-12",
                time = "16:00",
                arrivalTime = "17:10",
                price = 720.0,
                totalSeats = 189
            ),
            Trip(
                type = TripType.FLIGHT,
                companyName = "Turkish Airlines",
                departure = "Antalya",
                destination = "Ankara",
                date = "2025-01-15",
                time = "11:30",
                arrivalTime = "12:45",
                price = 880.0,
                totalSeats = 180
            )
        )
        
        // Insert all trips
        (busTrips + flightTrips).forEach { trip ->
            tripDao.insert(trip)
        }
    }
}
