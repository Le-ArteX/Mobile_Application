package com.example.usersettingsapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileActivity : AppCompatActivity() {
    private lateinit var tvWelcomeBanner: TextView
    private lateinit var etStudentID: EditText
    private lateinit var etFullName: EditText
    private lateinit var spinnerDept: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var etEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = "Profile Setup"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvWelcomeBanner = findViewById(R.id.tvWelcomeBanner)
        etStudentID = findViewById(R.id.etStudentID)
        etFullName = findViewById(R.id.etFullName)
        spinnerDept = findViewById(R.id.spinnerDept)
        spinnerYear = findViewById(R.id.spinnerYear)
        etEmail = findViewById(R.id.etEmail)

        findViewById<Button>(R.id.btnSaveProfile).setOnClickListener {
            saveProfile()
        }
    }

    override fun onResume() {
        super.onResume()
        restoreProfile()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun saveProfile() {
        val prefs = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        prefs.edit().apply {
            putString("KEY_STUDENT_ID", etStudentID.text.toString())
            putString("KEY_STUDENT_NAME", etFullName.text.toString())
            putString("KEY_DEPARTMENT", spinnerDept.selectedItem.toString())
            putString("KEY_YEAR", spinnerYear.selectedItem.toString())
            putString("KEY_EMAIL", etEmail.text.toString())
            apply()
        }
        Toast.makeText(this, "Profile Saved", Toast.LENGTH_SHORT).show()
        updateWelcomeBanner(etFullName.text.toString())
    }

    private fun restoreProfile() {
        val prefs = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        val name = prefs.getString("KEY_STUDENT_NAME", "")
        updateWelcomeBanner(name)

        etStudentID.setText(prefs.getString("KEY_STUDENT_ID", ""))
        etFullName.setText(name)
        etEmail.setText(prefs.getString("KEY_EMAIL", ""))

        val dept = prefs.getString("KEY_DEPARTMENT", "CSE")
        val depts = resources.getStringArray(R.array.department_options)
        spinnerDept.setSelection(depts.indexOf(dept).takeIf { it >= 0 } ?: 0)

        val year = prefs.getString("KEY_YEAR", "1st Year")
        val years = resources.getStringArray(R.array.year_options)
        spinnerYear.setSelection(years.indexOf(year).takeIf { it >= 0 } ?: 0)
    }

    private fun updateWelcomeBanner(name: String?) {
        if (!name.isNullOrBlank()) {
            tvWelcomeBanner.text = "Welcome back, $name!"
        } else {
            tvWelcomeBanner.text = "Welcome!"
        }
    }
}