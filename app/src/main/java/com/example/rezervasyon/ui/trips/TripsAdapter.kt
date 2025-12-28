package com.example.rezervasyon.ui.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.data.local.entities.ReservationStatus
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.data.local.entities.TripType
import com.example.rezervasyon.databinding.ItemTripCardBinding
import kotlinx.coroutines.launch

/**
 * RecyclerView adapter for displaying trip cards
 * Uses ListAdapter with DiffUtil for efficient list updates
 * Calculates available seats dynamically based on reservations
 */
class TripsAdapter(
    private val database: AppDatabase,
    private val lifecycleOwner: LifecycleOwner,
    private val onTripClick: (Trip) -> Unit
) : ListAdapter<Trip, TripsAdapter.TripViewHolder>(TripDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val binding = ItemTripCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TripViewHolder(binding, database, lifecycleOwner, onTripClick)
    }
    
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class TripViewHolder(
        private val binding: ItemTripCardBinding,
        private val database: AppDatabase,
        private val lifecycleOwner: LifecycleOwner,
        private val onTripClick: (Trip) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(trip: Trip) {
            binding.apply {
                // Set trip data
                tvType.text = if (trip.type == TripType.BUS) "OTOBÜS" else "UÇAK"
                tvCompany.text = trip.companyName
                tvDeparture.text = trip.departure
                tvDestination.text = trip.destination
                tvDate.text = trip.date
                tvTime.text = trip.time
                tvArrivalTime.text = "→ ${trip.arrivalTime}"
                
                // Calculate duration
                val duration = calculateDuration(trip.time, trip.arrivalTime)
                tvDuration.text = "($duration)"
                
                tvPrice.text = "${trip.price} TL"
                
                // Calculate available seats
                lifecycleOwner.lifecycleScope.launch {
                    val reservations = database.reservationDao().getReservationsByTrip(trip.id)
                    val activeReservations = reservations.filter { it.status == ReservationStatus.ACTIVE }
                    
                    // Count total reserved seats
                    val reservedSeatsCount = activeReservations.sumOf { reservation ->
                        reservation.seatNumbers.split(",").size
                    }
                    
                    val availableSeats = trip.totalSeats - reservedSeatsCount
                    
                    tvAvailableSeats.text = "$availableSeats koltuk müsait"
                }
                
                // Click listener
                root.setOnClickListener {
                    onTripClick(trip)
                }
            }
        }
        
        private fun calculateDuration(startTime: String, endTime: String): String {
            try {
                val (startHour, startMin) = startTime.split(":").map { it.toInt() }
                val (endHour, endMin) = endTime.split(":").map { it.toInt() }
                
                var totalMinutes = (endHour * 60 + endMin) - (startHour * 60 + startMin)
                
                // Handle overnight trips
                if (totalMinutes < 0) {
                    totalMinutes += 24 * 60
                }
                
                val hours = totalMinutes / 60
                val minutes = totalMinutes % 60
                
                return "${hours}s ${minutes}d"
            } catch (e: Exception) {
                return "-"
            }
        }
    }
    
    private class TripDiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem == newItem
        }
    }
}
