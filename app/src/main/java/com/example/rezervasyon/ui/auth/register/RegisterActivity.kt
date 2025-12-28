package com.example.rezervasyon.ui.auth.register

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.rezervasyon.R
import com.example.rezervasyon.data.local.database.AppDatabase
import com.example.rezervasyon.data.local.entities.User
import com.example.rezervasyon.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

/**
 * Registration screen for new users
 */
class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var database: AppDatabase
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        database = AppDatabase.getDatabase(this)
        
        setupListeners()
    }
    
    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (validateInput(name, email, password)) {
                performRegistration(name, email, password)
            }
        }
        
        binding.tvLogin.setOnClickListener {
            finish()
        }
    }
    
    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return false
        }
        
        if (password.length < 6) {
            Toast.makeText(this, "Şifre en az 6 karakter olmalıdır", Toast.LENGTH_SHORT).show()
            return false
        }
        
        return true
    }
    
    private fun performRegistration(name: String, email: String, password: String) {
        lifecycleScope.launch {
            // Check if email already exists
            val emailExists = database.userDao().emailExists(email) > 0
            
            if (emailExists) {
                Toast.makeText(
                    this@RegisterActivity,
                    "Bu e-posta adresi zaten kullanılıyor",
                    Toast.LENGTH_SHORT
                ).show()
                return@launch
            }
            
            // Create new user
            val user = User(
                name = name,
                email = email,
                password = password,
                isAdmin = false
            )
            
            database.userDao().insert(user)
            
            Toast.makeText(
                this@RegisterActivity,
                getString(R.string.register_success),
                Toast.LENGTH_SHORT
            ).show()
            
            finish()
        }
    }
}
