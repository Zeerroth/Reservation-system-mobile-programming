package com.example.rezervasyon.ui.admin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rezervasyon.R
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.data.local.entities.TripType
import com.example.rezervasyon.databinding.ActivityAdminBinding
import com.example.rezervasyon.ui.trips.TripsAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Admin panel for managing trips
 * Allows adding and deleting trips
 */
class AdminActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAdminBinding
    private lateinit var database: AppDatabase
    private lateinit var tripsAdapter: TripsAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getDatabase(this)
        
        setupTripTypeSpinner()
        setupRecyclerView()
        setupListeners()
        loadTrips()
    }
    
    private fun setupTripTypeSpinner() {
        val tripTypes = arrayOf("OTOBÜS", "UÇAK")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tripTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerTripType.adapter = adapter
    }
    
    private fun setupRecyclerView() {
        tripsAdapter = TripsAdapter(database, this) { trip ->
            showDeleteConfirmation(trip)
        }
        
        binding.recyclerViewTrips.apply {
            layoutManager = LinearLayoutManager(this@AdminActivity)
            adapter = tripsAdapter
        }
    }
    
    private fun setupListeners() {
        binding.btnAddTrip.setOnClickListener {
            addTrip()
        }
        
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
    
    private fun loadTrips() {
        lifecycleScope.launch {
            database.tripDao().getAllTrips().collectLatest { trips ->
                tripsAdapter.submitList(trips)
            }
        }
    }
    
    private fun addTrip() {
        binding.apply {
            val company = etCompanyName.text.toString().trim()
            val departure = etDeparture.text.toString().trim()
            val destination = binding.etDestination.text.toString()
            val date = binding.etDate.text.toString()
            val time = binding.etTime.text.toString()
            val arrivalTime = binding.etArrivalTime.text.toString()
            val priceText = binding.etPrice.text.toString()
            val seatsText = binding.etTotalSeats.text.toString()
            
            // Validate inputs
            if (company.isEmpty() || departure.isEmpty() || destination.isEmpty() ||
                date.isEmpty() || time.isEmpty() || arrivalTime.isEmpty() || priceText.isEmpty() || seatsText.isEmpty()) {
                Toast.makeText(this@AdminActivity, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
                return
            }
            
            val tripType = if (spinnerTripType.selectedItemPosition == 0) TripType.BUS else TripType.FLIGHT
            
            val trip = Trip(
                type = tripType,
                companyName = company,
                departure = departure,
                destination = destination,
                date = date,
                time = time,
                arrivalTime = arrivalTime,
                price = priceText.toDoubleOrNull() ?: 0.0,
                totalSeats = seatsText.toIntOrNull() ?: 0
            )
            
            lifecycleScope.launch {
                database.tripDao().insert(trip)
                Toast.makeText(this@AdminActivity, R.string.trip_added, Toast.LENGTH_SHORT).show()
                clearForm()
            }
        }
    }
    
    private fun clearForm() {
        binding.apply {
            etCompanyName.text?.clear()
            etDeparture.text?.clear()
            etDestination.text?.clear()
            etDate.text?.clear()
            etTime.text?.clear()
            etArrivalTime.text?.clear()
            etPrice.text?.clear()
            etTotalSeats.text?.clear()
        }
    }
    
    private fun showDeleteConfirmation(trip: Trip) {
        AlertDialog.Builder(this)
            .setTitle("Sefer Sil")
            .setMessage("${trip.departure} → ${trip.destination} seferini silmek istediğinizden emin misiniz?")
            .setPositiveButton("Sil") { _, _ ->
                deleteTrip(trip)
            }
            .setNegativeButton("İptal", null)
            .show()
    }
    
    private fun deleteTrip(trip: Trip) {
        lifecycleScope.launch {
            database.tripDao().delete(trip)
            Toast.makeText(this@AdminActivity, R.string.trip_deleted, Toast.LENGTH_SHORT).show()
        }
    }
}
