package com.example.loginprofileapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Find views
        val rootLayout = findViewById<View>(R.id.main_root)
        val loginForm = findViewById<RelativeLayout>(R.id.rl_login_form)
        val profileCard = findViewById<RelativeLayout>(R.id.rl_profile_card)
        val progressBar = findViewById<ProgressBar>(R.id.pb_loading)

        val etUsername = findViewById<EditText>(R.id.et_username)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val btnLogout = findViewById<Button>(R.id.btn_logout)
        val tvForgotPassword = findViewById<TextView>(R.id.tv_forgot_password)

        // Set edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Login Logic
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username == "admin" && password == "1234") {
                // Show ProgressBar and hide login form
                loginForm.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                // Simulate loading
                Handler(Looper.getMainLooper()).postDelayed({
                    progressBar.visibility = View.GONE
                    profileCard.visibility = View.VISIBLE
                }, 2000) // 2 seconds delay
            } else {
                Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
            }
        }

        // Logout Logic
        btnLogout.setOnClickListener {
            profileCard.visibility = View.GONE
            loginForm.visibility = View.VISIBLE
            
            // Clear fields
            etUsername.text.clear()
            etPassword.text.clear()
        }

        // Forgot Password Logic
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Password reset link sent to your email", Toast.LENGTH_LONG).show()
        }
    }
}