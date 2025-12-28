package com.example.rezervasyon.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.rezervasyon.R
import com.example.rezervasyon.ui.auth.login.LoginActivity
import com.example.rezervasyon.ui.main.MainActivity
import com.example.rezervasyon.utils.SampleDataGenerator
import com.example.rezervasyon.utils.SessionManager

/**
 * Splash screen displayed when the app launches
 * Checks user session and initializes sample data
 */
class SplashActivity : AppCompatActivity() {
    
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        sessionManager = SessionManager(this)
        
        // Populate database with sample data (only runs once)
        SampleDataGenerator.populateDatabase(this)
        
        // Navigate after delay
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 2000) // 2 seconds delay
    }
    
    /**
     * Navigate to Login or Main screen based on session
     */
    private fun navigateToNextScreen() {
        val intent = if (sessionManager.isLoggedIn()) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }
        
        startActivity(intent)
        finish()
    }
}
