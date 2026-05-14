package com.example.studentcoursemanager

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var fabAddCourse: FloatingActionButton
    private lateinit var searchView: SearchView

    private lateinit var database: DatabaseReference
    private lateinit var adapter: CourseAdapter
    private val courseList = mutableListOf<Course>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        fabAddCourse = findViewById(R.id.fabAddCourse)
        searchView = findViewById(R.id.searchView)

        database = FirebaseDatabase.getInstance().getReference("Courses")

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CourseAdapter(courseList,
            onEditClick = { course ->
                val intent = Intent(this, EditCourseActivity::class.java)
                intent.putExtra("COURSE", course)
                startActivity(intent)
            },
            onDeleteClick = { course -> showDeleteConfirmation(course) },
            onItemClick = { course ->
                val intent = Intent(this, CourseDetailActivity::class.java)
                intent.putExtra("COURSE", course)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        fabAddCourse.setOnClickListener {
            startActivity(Intent(this, AddCourseActivity::class.java))
        }

        fetchCourses()
        setupSearch()
    }

    private fun fetchCourses() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                if (snapshot.exists()) {
                    for (courseSnap in snapshot.children) {
                        val course = courseSnap.getValue(Course::class.java)
                        if (course != null) courseList.add(course)
                    }
                    emptyStateLayout.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                } else {
                    emptyStateLayout.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                adapter.updateList(courseList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSearch() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filteredList = courseList.filter {
                    it.name.contains(newText ?: "", ignoreCase = true) ||
                            it.code.contains(newText ?: "", ignoreCase = true)
                }
                adapter.updateList(filteredList)
                return true
            }
        })
    }

    private fun showDeleteConfirmation(course: Course) {
        AlertDialog.Builder(this)
            .setTitle("Delete Course")
            .setMessage("Are you sure you want to delete ${course.code}?")
            .setPositiveButton("Yes") { _, _ ->
                database.child(course.id).removeValue().addOnSuccessListener {
                    Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("No", null)
            .show()
    }
}