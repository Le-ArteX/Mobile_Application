package com.example.fitnesstrackerapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private var stepsCount = 5000
    private val dailyGoal = 10000

    private lateinit var stepsValue: TextView
    private lateinit var stepsProgressBar: ProgressBar
    private lateinit var goalPercentage: TextView
    private lateinit var dateTextView: TextView
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Views
        stepsValue = findViewById(R.id.stepsValue)
        stepsProgressBar = findViewById(R.id.stepsProgressBar)
        goalPercentage = findViewById(R.id.goalPercentage)
        dateTextView = findViewById(R.id.dateTextView)
        updateButton = findViewById(R.id.updateButton)

        // Set Current Date
        val sdf = SimpleDateFormat("EEEE, MMMM dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        dateTextView.text = currentDate

        // Initial UI Update
        updateUI()

        // Button Click Handler
        updateButton.setOnClickListener {
            showUpdateStepsDialog()
        }
    }

    private fun updateUI() {
        stepsValue.text = String.format("%,d", stepsCount)
        
        val progress = (stepsCount.toFloat() / dailyGoal * 100).toInt()
        stepsProgressBar.progress = if (progress > 100) 100 else progress
        goalPercentage.text = "$progress%"

        if (progress >= 100) {
            Toast.makeText(this, "Congratulations! You've reached your daily goal! 🚀", Toast.LENGTH_LONG).show()
        }
    }

    private fun showUpdateStepsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Daily Steps")
        
        val input = EditText(this)
        input.hint = "Enter new step count"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        builder.setView(input)

        builder.setPositiveButton("Update") { dialog, _ ->
            val enteredValue = input.text.toString()
            if (enteredValue.isNotEmpty()) {
                stepsCount = enteredValue.toInt()
                updateUI()
            }
            dialog.dismiss()
        }
        
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
