package com.example.rezervasyon.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rezervasyon.databinding.FragmentProfileBinding
import com.example.rezervasyon.ui.admin.AdminActivity
import com.example.rezervasyon.ui.auth.login.LoginActivity
import com.example.rezervasyon.utils.SessionManager

/**
 * Profile fragment displaying user information
 * Shows admin panel button for admin users
 */
class ProfileFragment : Fragment() {
    
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var sessionManager: SessionManager
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        sessionManager = SessionManager(requireContext())
        
        displayUserInfo()
        setupListeners()
    }
    
    private fun displayUserInfo() {
        binding.apply {
            tvUserName.text = sessionManager.getUserName()
            tvUserEmail.text = sessionManager.getUserEmail()
            
            // Show admin panel button only for admin users
            if (sessionManager.isAdmin()) {
                btnAdminPanel.visibility = View.VISIBLE
            } else {
                btnAdminPanel.visibility = View.GONE
            }
        }
    }
    
    private fun setupListeners() {
        binding.btnAdminPanel.setOnClickListener {
            val intent = Intent(requireContext(), AdminActivity::class.java)
            startActivity(intent)
        }
        
        binding.btnLogout.setOnClickListener {
            logout()
        }
    }
    
    private fun logout() {
        sessionManager.clearSession()
        
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
