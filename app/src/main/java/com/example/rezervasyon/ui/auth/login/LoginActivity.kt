package com.example.rezervasyon.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rezervasyon.R
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.databinding.ActivityLoginBinding
import com.example.rezervasyon.ui.auth.register.RegisterActivity
import com.example.rezervasyon.ui.main.MainActivity
import com.example.rezervasyon.utils.SessionManager
import kotlinx.coroutines.launch

/**
 * Login screen for user authentication
 * Supports both regular users and admin users
 */
class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: AppDatabase
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getDatabase(this)
        sessionManager = SessionManager(this)
        
        setupListeners()
    }
    
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(email, password)) {
                performLogin(email, password)
            }
        }
        
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    
    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
    
    private fun performLogin(email: String, password: String) {
        lifecycleScope.launch {
            val user = database.userDao().login(email, password)
            
            if (user != null) {
                // Save session
                sessionManager.saveUserSession(
                    userId = user.id,
                    email = user.email,
                    name = user.name,
                    isAdmin = user.isAdmin
                )
                
                // Show success message
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.login_success),
                    Toast.LENGTH_SHORT
                ).show()
                
                // Navigate to main screen
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    getString(R.string.login_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
