package com.example.rezervasyon.ui.reservations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rezervasyon.R
import com.example.rezervasyon.data.local.entities.Reservation
import com.example.rezervasyon.data.local.entities.ReservationStatus
import com.example.rezervasyon.data.local.entities.Trip
import com.example.rezervasyon.databinding.ItemReservationCardBinding

/**
 * RecyclerView adapter for displaying reservation cards
 */
class ReservationsAdapter(
    private val tripMap: Map<Long, Trip>,
    private val onShareClick: (Reservation, Trip) -> Unit,
    private val onCancelClick: (Reservation) -> Unit
) : ListAdapter<Reservation, ReservationsAdapter.ReservationViewHolder>(ReservationDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = ItemReservationCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReservationViewHolder(binding, tripMap, onShareClick, onCancelClick)
    }
    
    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ReservationViewHolder(
        private val binding: ItemReservationCardBinding,
        private val tripMap: Map<Long, Trip>,
        private val onShareClick: (Reservation, Trip) -> Unit,
        private val onCancelClick: (Reservation) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(reservation: Reservation) {
            val trip = tripMap[reservation.tripId]
            
            binding.apply {
                // Status
                tvStatus.text = if (reservation.status == ReservationStatus.ACTIVE) "AKTİF" else "İPTAL EDİLDİ"
                tvStatus.setBackgroundResource(
                    if (reservation.status == ReservationStatus.ACTIVE) 
                        R.color.success 
                    else 
                        R.color.error
                )
                
                // Trip info
                trip?.let {
                    tvTripRoute.text = "${it.departure} → ${it.destination}"
                    tvCompany.text = it.companyName
                    tvDateTime.text = "${it.date} ${it.time}"
                }
                
                // Reservation details
                tvSeats.text = "Koltuklar: ${reservation.seatNumbers}"
                tvPrice.text = "${reservation.totalPrice} TL"
                
                // Buttons
                if (reservation.status == ReservationStatus.ACTIVE) {
                    btnShare.visibility = View.VISIBLE
                    btnCancel.visibility = View.VISIBLE
                    
                    btnShare.setOnClickListener {
                        trip?.let { onShareClick(reservation, it) }
                    }
                    
                    btnCancel.setOnClickListener {
                        onCancelClick(reservation)
                    }
                } else {
                    btnShare.visibility = View.GONE
                    btnCancel.visibility = View.GONE
                }
            }
        }
    }
    
    private class ReservationDiffCallback : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem == newItem
        }
    }
}
