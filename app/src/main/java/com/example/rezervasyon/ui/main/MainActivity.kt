package com.example.rezervasyon.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rezervasyon.R
import com.example.rezervasyon.databinding.ActivityMainBinding
import com.example.rezervasyon.ui.profile.ProfileFragment
import com.example.rezervasyon.ui.reservations.ReservationsFragment
import com.example.rezervasyon.ui.trips.TripsFragment

/**
 * Main activity with bottom navigation
 * Hosts three main fragments: Trips, Reservations, and Profile
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set initial fragment
        if (savedInstanceState == null) {
            loadFragment(TripsFragment())
        }
        
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_trips -> {
                    loadFragment(TripsFragment())
                    true
                }
                R.id.navigation_reservations -> {
                    loadFragment(ReservationsFragment())
                    true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
