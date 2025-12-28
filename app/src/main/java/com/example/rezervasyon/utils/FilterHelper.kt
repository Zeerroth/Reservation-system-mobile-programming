package com.example.rezervasyon.utils

import com.example.rezervasyon.data.local.entities.Trip

/**
 * Helper class to extract unique filter values from trip list
 */
object FilterHelper {
    
    /**
     * Get unique company names from trips
     */
    fun getUniqueCompanies(trips: List<Trip>): List<String> {
        return trips.map { it.companyName }.distinct().sorted()
    }
    
    /**
     * Get unique departure cities from trips
     */
    fun getUniqueDepartures(trips: List<Trip>): List<String> {
        return trips.map { it.departure }.distinct().sorted()
    }
    
    /**
     * Get unique destination cities from trips
     */
    fun getUniqueDestinations(trips: List<Trip>): List<String> {
        return trips.map { it.destination }.distinct().sorted()
    }
    
    /**
     * Get unique dates from trips
     */
    fun getUniqueDates(trips: List<Trip>): List<String> {
        return trips.map { it.date }.distinct().sorted()
    }
    
    /**
     * Get min and max price from trips
     */
    fun getPriceRange(trips: List<Trip>): Pair<Double, Double> {
        if (trips.isEmpty()) return Pair(0.0, 1000.0)
        val prices = trips.map { it.price }
        return Pair(prices.minOrNull() ?: 0.0, prices.maxOrNull() ?: 1000.0)
    }
}
