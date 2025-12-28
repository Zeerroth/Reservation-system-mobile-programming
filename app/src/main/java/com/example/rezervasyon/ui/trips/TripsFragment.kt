package com.example.rezervasyon.ui.trips

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rezervasyon.R
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.data.local.entities.TripType
import com.example.rezervasyon.databinding.BottomSheetFiltersBinding
import com.example.rezervasyon.databinding.FragmentTripsBinding
import com.example.rezervasyon.ui.seat.SeatSelectionActivity
import com.example.rezervasyon.utils.FilterHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment for displaying and searching trips
 * Features RecyclerView, search and advanced filtering
 */
class TripsFragment : Fragment() {
    
    private var _binding: FragmentTripsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var tripsAdapter: TripsAdapter
    
    private var searchJob: Job? = null
    private var allTrips = listOf<Trip>()
    
    // Filter state
    private var selectedTripType: TripType? = null
    private var selectedCompany: String? = null
    private var selectedDeparture: String? = null
    private var selectedDestination: String? = null
    private var selectedDate: String? = null
    private var minPrice: Double = 0.0
    private var maxPrice: Double = 10000.0
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        
        setupRecyclerView()
        setupSearch()
        setupFilterButton()
        loadTrips()
    }
    
    private fun setupRecyclerView() {
        tripsAdapter = TripsAdapter(database, viewLifecycleOwner) { trip ->
            // Navigate to seat selection
            val intent = Intent(requireContext(), SeatSelectionActivity::class.java)
            intent.putExtra("TRIP_ID", trip.id)
            startActivity(intent)
        }
        
        binding.recyclerViewTrips.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = tripsAdapter
        }
    }
    
    private fun setupSearch() {
        binding.etSearch.addTextChangedListener { text ->
            val query = text.toString().trim()
            searchTrips(query)
        }
    }
    
    private fun setupFilterButton() {
        binding.btnFilters.setOnClickListener {
            showFilterBottomSheet()
        }
    }
    
    private fun loadTrips() {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            database.tripDao().getAllTrips().collectLatest { trips ->
                allTrips = trips
                tripsAdapter.submitList(trips)
                updateEmptyState(trips.isEmpty())
            }
        }
    }
    
    private fun searchTrips(query: String) {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            if (query.isEmpty()) {
                applyFilters()
            } else {
                database.tripDao().searchTrips(query, query).collectLatest { trips ->
                    tripsAdapter.submitList(trips)
                    updateEmptyState(trips.isEmpty())
                }
            }
        }
    }
    
    private fun showFilterBottomSheet() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomSheetFiltersBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(sheetBinding.root)
        
        // Populate spinners with unique values
        setupFilterSpinners(sheetBinding)
        
        // Setup price slider
        val (min, max) = FilterHelper.getPriceRange(allTrips)
        sheetBinding.sliderPrice.valueFrom = min.toFloat()
        sheetBinding.sliderPrice.valueTo = max.toFloat()
        sheetBinding.sliderPrice.values = listOf(minPrice.toFloat().coerceIn(min.toFloat(), max.toFloat()), 
                                                   maxPrice.toFloat().coerceIn(min.toFloat(), max.toFloat()))
        sheetBinding.tvPriceRange.text = "${sheetBinding.sliderPrice.values[0].toInt()} TL - ${sheetBinding.sliderPrice.values[1].toInt()} TL"
        
        sheetBinding.sliderPrice.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            sheetBinding.tvPriceRange.text = "${values[0].toInt()} TL - ${values[1].toInt()} TL"
        }
        
        // Apply button
        sheetBinding.btnApplyFilters.setOnClickListener {
            // Get trip type
            val tripTypeSelection = sheetBinding.spinnerTripType.selectedItem?.toString()
            selectedTripType = when (tripTypeSelection) {
                "Otobüs" -> TripType.BUS
                "Uçak" -> TripType.FLIGHT
                else -> null
            }
            
            selectedCompany = getSelectedSpinnerValue(sheetBinding.spinnerCompany)
            selectedDeparture = getSelectedSpinnerValue(sheetBinding.spinnerDeparture)
            selectedDestination = getSelectedSpinnerValue(sheetBinding.spinnerDestination)
            selectedDate = getSelectedSpinnerValue(sheetBinding.spinnerDate)
            
            val priceValues = sheetBinding.sliderPrice.values
            minPrice = priceValues[0].toDouble()
            maxPrice = priceValues[1].toDouble()
            
            applyFilters()
            bottomSheetDialog.dismiss()
        }
        
        // Clear button
        sheetBinding.btnClearFilters.setOnClickListener {
            clearFilters()
            bottomSheetDialog.dismiss()
            loadTrips()
        }
        
        bottomSheetDialog.show()
    }
    
    private fun setupFilterSpinners(binding: BottomSheetFiltersBinding) {
        // Trip Type
        val tripTypes = listOf("Tümü", "Otobüs", "Uçak")
        binding.spinnerTripType.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tripTypes).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        // Company
        val companies = listOf("Tümü") + FilterHelper.getUniqueCompanies(allTrips)
        binding.spinnerCompany.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, companies).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        // Departure
        val departures = listOf("Tümü") + FilterHelper.getUniqueDepartures(allTrips)
        binding.spinnerDeparture.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, departures).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        // Destination
        val destinations = listOf("Tümü") + FilterHelper.getUniqueDestinations(allTrips)
        binding.spinnerDestination.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, destinations).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        
        // Date
        val dates = listOf("Tümü") + FilterHelper.getUniqueDates(allTrips)
        binding.spinnerDate.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dates).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }
    
    private fun getSelectedSpinnerValue(spinner: android.widget.Spinner): String? {
        val value = spinner.selectedItem?.toString()
        return if (value == "Tümü") null else value
    }
    
    private fun applyFilters() {
        searchJob?.cancel()
        searchJob = viewLifecycleOwner.lifecycleScope.launch {
            database.tripDao().searchTripsWithFilters(
                type = selectedTripType,
                departure = selectedDeparture,
                destination = selectedDestination,
                date = selectedDate
            ).collectLatest { trips ->
                // Filter by company and price locally
                val filteredTrips = trips.filter { trip ->
                    (selectedCompany == null || trip.companyName == selectedCompany) &&
                    (trip.price >= minPrice && trip.price <= maxPrice)
                }
                
                tripsAdapter.submitList(filteredTrips)
                updateEmptyState(filteredTrips.isEmpty())
            }
        }
    }
    
    private fun clearFilters() {
        selectedTripType = null
        selectedCompany = null
        selectedDeparture = null
        selectedDestination = null
        selectedDate = null
        minPrice = 0.0
        maxPrice = 10000.0
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewTrips.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
        _binding = null
    }
}
