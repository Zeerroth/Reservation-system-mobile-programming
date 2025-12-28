package com.example.rezervasyon.utils

import android.content.Context
import android.content.Intent
import com.example.rezervasyon.data.local.entities.Reservation
import com.example.rezervasyon.data.local.entities.Trip
import java.text.SimpleDateFormat
import java.util.*

/**
 * Utility class for sharing reservations via implicit intents
 * Demonstrates the use of intent to share data with external apps
 */
object ShareHelper {
    
    /**
     * Share reservation details using an implicit intent
     * This allows users to share via WhatsApp, Email, SMS, etc.
     * 
     * @param context Context for starting the intent
     * @param reservation The reservation to share
     * @param trip The trip details associated with the reservation
     */
    fun shareReservation(context: Context, reservation: Reservation, trip: Trip) {
        val message = buildReservationMessage(reservation, trip)
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, message)
            type = "text/plain"
        }
        
        val chooser = Intent.createChooser(shareIntent, "Rezervasyonu Paylaş")
        context.startActivity(chooser)
    }
    
    /**
     * Build a formatted message for the reservation
     */
    private fun buildReservationMessage(reservation: Reservation, trip: Trip): String {
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale("tr", "TR"))
        val reservationDate = dateFormat.format(Date(reservation.reservationDate))
        
        val tripTypeText = if (trip.type.name == "BUS") "Otobüs" else "Uçak"
        
        return """
            🎫 Rezervasyon Detayları 🎫
            
            Tip: $tripTypeText
            Firma: ${trip.companyName}
            Kalkış: ${trip.departure}
            Varış: ${trip.destination}
            Tarih: ${trip.date}
            Saat: ${trip.time}
            
            Koltuk Numaraları: ${reservation.seatNumbers}
            Toplam Ücret: ${reservation.totalPrice} TL
            
            Rezervasyon Tarihi: $reservationDate
            Durum: ${if (reservation.status.name == "ACTIVE") "Aktif" else "İptal Edildi"}
            
            ✈️ İyi yolculuklar! 🚌
        """.trimIndent()
    }
    
    /**
     * Share a simple text message
     */
    fun shareText(context: Context, text: String, title: String = "Paylaş") {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
        
        val chooser = Intent.createChooser(shareIntent, title)
        context.startActivity(chooser)
    }
}
