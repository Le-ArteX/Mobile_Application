package com.example.studentcoursemanager

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class AddCourseActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etCode: EditText
    private lateinit var etInstructor: EditText
    private lateinit var etSchedule: EditText
    private lateinit var etRoom: EditText
    private lateinit var spinnerCredits: Spinner
    private lateinit var spinnerSemester: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_course)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = "Add Course"

        etName = findViewById(R.id.etName)
        etCode = findViewById(R.id.etCode)
        etInstructor = findViewById(R.id.etInstructor)
        etSchedule = findViewById(R.id.etSchedule)
        etRoom = findViewById(R.id.etRoom)
        spinnerCredits = findViewById(R.id.spinnerCredits)
        spinnerSemester = findViewById(R.id.spinnerSemester)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        progressBar = findViewById(R.id.progressBar)

        setupSpinners()

        btnSave.setOnClickListener { saveCourse() }
        btnCancel.setOnClickListener { finish() }
    }

    private fun setupSpinners() {
        val credits = arrayOf("1", "2", "3", "4")
        spinnerCredits.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, credits)

        val semesters = arrayOf("Spring 2025", "Summer 2025", "Fall 2025")
        spinnerSemester.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, semesters)
    }

    private fun saveCourse() {
        val name = etName.text.toString().trim()
        val code = etCode.text.toString().trim()
        val instructor = etInstructor.text.toString().trim()
        val schedule = etSchedule.text.toString().trim()
        val room = etRoom.text.toString().trim()
        val credits = spinnerCredits.selectedItem.toString()
        val semester = spinnerSemester.selectedItem.toString()

        if (name.isEmpty() || code.isEmpty() || instructor.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        btnSave.isEnabled = false

        val database = FirebaseDatabase.getInstance().getReference("Courses")
        val courseId = database.push().key ?: return

        val course = Course(courseId, name, code, instructor, credits, schedule, room, semester)

        database.child(courseId).setValue(course)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Course Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                btnSave.isEnabled = true
                Toast.makeText(this, "Error saving course", Toast.LENGTH_SHORT).show()
            }
    }
}