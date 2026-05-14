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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase

class EditCourseActivity : AppCompatActivity() {
    private lateinit var etName: EditText
    private lateinit var etCode: EditText
    private lateinit var etInstructor: EditText
    private lateinit var etSchedule: EditText
    private lateinit var etRoom: EditText
    private lateinit var spinnerCredits: Spinner
    private lateinit var spinnerSemester: Spinner
    private lateinit var btnUpdate: Button
    private lateinit var btnCancel: Button
    private lateinit var btnDelete: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var course: Course
    private val creditsArray = arrayOf("1", "2", "3", "4")
    private val semestersArray = arrayOf("Spring 2025", "Summer 2025", "Fall 2025")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        title = "Edit Course"

        course = intent.getSerializableExtra("COURSE") as Course

        etName = findViewById(R.id.etName)
        etCode = findViewById(R.id.etCode)
        etInstructor = findViewById(R.id.etInstructor)
        etSchedule = findViewById(R.id.etSchedule)
        etRoom = findViewById(R.id.etRoom)
        spinnerCredits = findViewById(R.id.spinnerCredits)
        spinnerSemester = findViewById(R.id.spinnerSemester)
        btnUpdate = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        btnDelete = findViewById(R.id.btnDelete)
        progressBar = findViewById(R.id.progressBar)

        btnUpdate.text = "Update Course"
        btnDelete.visibility = View.VISIBLE

        setupSpinners()
        populateFields()

        btnUpdate.setOnClickListener { updateCourse() }
        btnCancel.setOnClickListener { finish() }
        btnDelete.setOnClickListener { confirmDelete() }
    }

    private fun setupSpinners() {
        spinnerCredits.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, creditsArray)
        spinnerSemester.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, semestersArray)
    }

    private fun populateFields() {
        etName.setText(course.name)
        etCode.setText(course.code)
        etInstructor.setText(course.instructor)
        etSchedule.setText(course.schedule)
        etRoom.setText(course.room)

        val creditIndex = creditsArray.indexOf(course.credits)
        if (creditIndex >= 0) spinnerCredits.setSelection(creditIndex)

        val semesterIndex = semestersArray.indexOf(course.semester)
        if (semesterIndex >= 0) spinnerSemester.setSelection(semesterIndex)
    }

    private fun updateCourse() {
        val name = etName.text.toString().trim()
        val code = etCode.text.toString().trim()
        val instructor = etInstructor.text.toString().trim()

        if (name.isEmpty() || code.isEmpty() || instructor.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        val updatedCourse = Course(
            course.id, name, code, instructor,
            spinnerCredits.selectedItem.toString(),
            etSchedule.text.toString().trim(),
            etRoom.text.toString().trim(),
            spinnerSemester.selectedItem.toString()
        )

        FirebaseDatabase.getInstance().getReference("Courses").child(course.id)
            .setValue(updatedCourse)
            .addOnSuccessListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Course updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun confirmDelete() {
        AlertDialog.Builder(this)
            .setTitle("Delete Course")
            .setMessage("Are you sure you want to delete this course?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseDatabase.getInstance().getReference("Courses").child(course.id)
                    .removeValue().addOnSuccessListener {
                        Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }
}