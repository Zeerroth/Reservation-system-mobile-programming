package com.example.rezervasyon.ui.reservations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rezervasyon.R
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.data.local.entities.Reservation
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.databinding.FragmentReservationsBinding
import com.example.rezervasyon.utils.SessionManager
import com.example.rezervasyon.utils.ShareHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Fragment for displaying user reservations
 * Features RecyclerView with share and cancel functionality
 */
class ReservationsFragment : Fragment() {
    
    private var _binding: FragmentReservationsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var database: AppDatabase
    private lateinit var sessionManager: SessionManager
    private lateinit var reservationsAdapter: ReservationsAdapter
    
    private val tripMap = mutableMapOf<Long, Trip>()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReservationsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        database = AppDatabase.getDatabase(requireContext())
        sessionManager = SessionManager(requireContext())
        
        setupRecyclerView()
        loadReservations()
    }
    
    private fun setupRecyclerView() {
        reservationsAdapter = ReservationsAdapter(
            tripMap = tripMap,
            onShareClick = { reservation, trip ->
                ShareHelper.shareReservation(requireContext(), reservation, trip)
            },
            onCancelClick = { reservation ->
                showCancelConfirmation(reservation)
            }
        )
        
        binding.recyclerViewReservations.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reservationsAdapter
        }
    }
    
    private fun loadReservations() {
        val userId = sessionManager.getUserId()
        
        viewLifecycleOwner.lifecycleScope.launch {
            // Load trips first
            database.tripDao().getAllTrips().collectLatest { trips ->
                tripMap.clear()
                trips.forEach { trip ->
                    tripMap[trip.id] = trip
                }
                
                // Then load reservations
                database.reservationDao().getReservationsByUser(userId).collectLatest { reservations ->
                    reservationsAdapter.submitList(reservations)
                    updateEmptyState(reservations.isEmpty())
                }
            }
        }
    }
    
    private fun showCancelConfirmation(reservation: Reservation) {
        AlertDialog.Builder(requireContext())
            .setTitle("Rezervasyonu İptal Et")
            .setMessage("Bu rezervasyonu iptal etmek istediğinizden emin misiniz?")
            .setPositiveButton("İptal Et") { _, _ ->
                cancelReservation(reservation)
            }
            .setNegativeButton("Vazgeç", null)
            .show()
    }
    
    private fun cancelReservation(reservation: Reservation) {
        viewLifecycleOwner.lifecycleScope.launch {
            database.reservationDao().cancelReservation(reservation.id)
            Toast.makeText(
                requireContext(),
                getString(R.string.reservation_cancelled),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    
    private fun updateEmptyState(isEmpty: Boolean) {
        binding.tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.recyclerViewReservations.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
