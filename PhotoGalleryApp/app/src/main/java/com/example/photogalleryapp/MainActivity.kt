package com.example.photogalleryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var gridView: GridView
    private lateinit var adapter: PhotoAdapter
    private lateinit var selectionToolbar: LinearLayout
    private lateinit var tvSelectionCount: TextView
    private var isSelectionMode = false

    private val photos = mutableListOf(
        Photo(1, R.drawable.ic_nature, "Mountain", "Nature"),
        Photo(2, R.drawable.ic_nature_2, "Forest", "Nature"),
        Photo(3, R.drawable.ic_nature_3, "River", "Nature"),
        Photo(4, R.drawable.ic_nature_4, "Sky", "Nature"),
        Photo(5, R.drawable.ic_city, "New York", "City"),
        Photo(6, R.drawable.ic_city_2, "London", "City"),
        Photo(7, R.drawable.ic_city_3, "Tokyo", "City"),
        Photo(8, R.drawable.ic_animals_1, "Lion", "Animals"),
        Photo(9, R.drawable.ic_animals_2, "Elephant", "Animals"),
        Photo(10, R.drawable.ic_food_1, "Pizza", "Food"),
        Photo(11, R.drawable.ic_food_2, "Burger", "Food"),
        Photo(12, R.drawable.ic_travel_1, "Paris", "Travel"),
        Photo(13, R.drawable.ic_travel_2, "Rome", "Travel")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridView = findViewById(R.id.gridView)
        selectionToolbar = findViewById(R.id.selectionToolbar)
        tvSelectionCount = findViewById(R.id.tvSelectionCount)
        val btnCloseSelection = findViewById<ImageButton>(R.id.btnCloseSelection)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        adapter = PhotoAdapter(this, photos)
        gridView.adapter = adapter

        setupCategoryButtons()

        gridView.setOnItemClickListener { _, _, position, _ ->
            if (isSelectionMode) {
                val photo = adapter.getItem(position)
                photo.isSelected = !photo.isSelected
                adapter.notifyDataSetChanged()
                updateSelectionCount()
            } else {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("resId", adapter.getItem(position).resourceId)
                startActivity(intent)
            }
        }

        gridView.setOnItemLongClickListener { _, _, position, _ ->
            if (!isSelectionMode) {
                enterSelectionMode()
                val photo = adapter.getItem(position)
                photo.isSelected = true
                adapter.notifyDataSetChanged()
                updateSelectionCount()
            }
            true
        }

        btnCloseSelection.setOnClickListener { exitSelectionMode() }

        btnDelete.setOnClickListener {
            val count = adapter.getSelectedCount()
            adapter.removeSelected()
            Toast.makeText(this, getString(R.string.photos_deleted, count), Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }

        btnShare.setOnClickListener {
            val count = adapter.getSelectedCount()
            Toast.makeText(this, "Sharing $count photos", Toast.LENGTH_SHORT).show()
            exitSelectionMode()
        }

        fabAdd.setOnClickListener {
            val newPhoto = Photo(
                (100..999).random(),
                R.drawable.ic_nature_4,
                "New Photo",
                "Nature"
            )
            adapter.addPhoto(newPhoto)
            Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCategoryButtons() {
        findViewById<Button>(R.id.btnAll).setOnClickListener { adapter.filterByCategory("All") }
        findViewById<Button>(R.id.btnNature).setOnClickListener { adapter.filterByCategory("Nature") }
        findViewById<Button>(R.id.btnCity).setOnClickListener { adapter.filterByCategory("City") }
        findViewById<Button>(R.id.btnAnimals).setOnClickListener { adapter.filterByCategory("Animals") }
        findViewById<Button>(R.id.btnFood).setOnClickListener { adapter.filterByCategory("Food") }
        findViewById<Button>(R.id.btnTravel).setOnClickListener { adapter.filterByCategory("Travel") }
    }

    private fun enterSelectionMode() {
        isSelectionMode = true
        selectionToolbar.visibility = View.VISIBLE
        adapter.setSelectionMode(true)
    }

    private fun exitSelectionMode() {
        isSelectionMode = false
        selectionToolbar.visibility = View.GONE
        adapter.clearSelection()
        adapter.setSelectionMode(false)
    }

    private fun updateSelectionCount() {
        val count = adapter.getSelectedCount()
        tvSelectionCount.text = getString(R.string.selection_count, count)
        if (count == 0 && isSelectionMode) exitSelectionMode()
    }
}
