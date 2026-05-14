package com.example.studentcoursemanager

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CourseDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_course_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = "Course Details"

        val course = intent.getSerializableExtra("COURSE") as Course

        findViewById<TextView>(R.id.tvDetailCode).text = course.code
        findViewById<TextView>(R.id.tvDetailName).text = course.name
        findViewById<TextView>(R.id.tvDetailInstructor).text = course.instructor
        findViewById<TextView>(R.id.tvDetailCredits).text = "${course.credits} Credits"
        findViewById<TextView>(R.id.tvDetailSchedule).text = course.schedule
        findViewById<TextView>(R.id.tvDetailRoom).text = course.room
        findViewById<TextView>(R.id.tvDetailSemester).text = course.semester

        findViewById<FloatingActionButton>(R.id.fabEdit).setOnClickListener {
            val intent = Intent(this, EditCourseActivity::class.java)
            intent.putExtra("COURSE", course)
            startActivity(intent)
            finish()
        }
    }
}