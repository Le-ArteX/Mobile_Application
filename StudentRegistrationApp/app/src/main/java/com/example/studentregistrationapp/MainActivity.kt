package com.example.studentregistrationapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etStudentId: EditText
    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etAge: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var cbFootball: CheckBox
    private lateinit var cbCricket: CheckBox
    private lateinit var cbBasketball: CheckBox
    private lateinit var cbBadminton: CheckBox
    private lateinit var spCountry: Spinner
    private lateinit var btnDatePicker: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var btnSubmit: Button
    private lateinit var btnReset: Button

    private var selectedDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize view components
        etStudentId = findViewById(R.id.etStudentId)
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        cbFootball = findViewById(R.id.cbFootball)
        cbCricket = findViewById(R.id.cbCricket)
        cbBasketball = findViewById(R.id.cbBasketball)
        cbBadminton = findViewById(R.id.cbBadminton)
        spCountry = findViewById(R.id.spCountry)
        btnDatePicker = findViewById(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        btnSubmit = findViewById(R.id.btnSubmit)
        btnReset = findViewById(R.id.btnReset)

        // Date Picker Implementation
        btnDatePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, yearSelected, monthOfYear, dayOfMonth ->
                selectedDate = "$dayOfMonth/${monthOfYear + 1}/$yearSelected"
                tvSelectedDate.text = "Selected Date: $selectedDate"
            }, year, month, day)
            datePickerDialog.show()
        }

        // Submit Button Implementation
        btnSubmit.setOnClickListener {
            if (validateInputs()) {
                val studentId = etStudentId.text.toString()
                val fullName = etFullName.text.toString()
                val email = etEmail.text.toString()
                val age = etAge.text.toString()
                
                val selectedGenderId = rgGender.checkedRadioButtonId
                val gender = findViewById<RadioButton>(selectedGenderId).text.toString()

                val sports = mutableListOf<String>()
                if (cbFootball.isChecked) sports.add("Football")
                if (cbCricket.isChecked) sports.add("Cricket")
                if (cbBasketball.isChecked) sports.add("Basketball")
                if (cbBadminton.isChecked) sports.add("Badminton")
                val sportsText = if (sports.isEmpty()) "None" else sports.joinToString(", ")

                val country = spCountry.selectedItem.toString()

                val message = """
                    ID: $studentId
                    Name: $fullName
                    Gender: $gender
                    Sports: $sportsText
                    Country: $country
                    DOB: $selectedDate
                """.trimIndent()

                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Reset Button Implementation
        btnReset.setOnClickListener {
            resetFields()
        }
    }

    private fun validateInputs(): Boolean {
        val studentId = etStudentId.text.toString().trim()
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val ageStr = etAge.text.toString().trim()
        val genderSelected = rgGender.checkedRadioButtonId != -1

        if (studentId.isEmpty() || fullName.isEmpty() || email.isEmpty() || 
            password.isEmpty() || ageStr.isEmpty() || !genderSelected || selectedDate.isEmpty()) {
            return false
        }

        if (!email.contains("@")) {
            return false
        }

        val age = ageStr.toIntOrNull()
        if (age == null || age <= 0) {
            return false
        }

        return true
    }

    private fun resetFields() {
        etStudentId.text.clear()
        etFullName.text.clear()
        etEmail.text.clear()
        etPassword.text.clear()
        etAge.text.clear()
        rgGender.clearCheck()
        cbFootball.isChecked = false
        cbCricket.isChecked = false
        cbBasketball.isChecked = false
        cbBadminton.isChecked = false
        spCountry.setSelection(0)
        selectedDate = ""
        tvSelectedDate.text = "No Date Selected"
    }
}