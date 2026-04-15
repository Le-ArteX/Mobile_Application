package com.example.newsreaderapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nestedScrollView = findViewById<NestedScrollView>(R.id.nestedScrollView)
        val btnBookmark = findViewById<ImageButton>(R.id.btnBookmark)
        val btnShare = findViewById<ImageButton>(R.id.btnShare)
        val fabBackToTop = findViewById<FloatingActionButton>(R.id.fabBackToTop)
        val articleTitle = getString(R.string.article_title)

        // Section Views for Navigation
        val sectionIntro = findViewById<TextView>(R.id.sectionIntro)
        val sectionKeyPoints = findViewById<TextView>(R.id.sectionKeyPoints)
        val sectionAnalysis = findViewById<TextView>(R.id.sectionAnalysis)
        val sectionConclusion = findViewById<TextView>(R.id.sectionConclusion)

        // Quick Navigation Button Listeners
        findViewById<Button>(R.id.navIntro).setOnClickListener {
            scrollToView(nestedScrollView, sectionIntro)
        }
        findViewById<Button>(R.id.navKeyPoints).setOnClickListener {
            scrollToView(nestedScrollView, sectionKeyPoints)
        }
        findViewById<Button>(R.id.navAnalysis).setOnClickListener {
            scrollToView(nestedScrollView, sectionAnalysis)
        }
        findViewById<Button>(R.id.navConclusion).setOnClickListener {
            scrollToView(nestedScrollView, sectionConclusion)
        }

        // Bookmark Button Logic
        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            if (isBookmarked) {
                btnBookmark.setImageResource(android.R.drawable.btn_star_big_on)
                Toast.makeText(this, getString(R.string.msg_bookmarked), Toast.LENGTH_SHORT).show()
            } else {
                btnBookmark.setImageResource(android.R.drawable.btn_star_big_off)
                Toast.makeText(this, getString(R.string.msg_bookmark_removed), Toast.LENGTH_SHORT).show()
            }
        }

        // Share Button Logic
        btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.toolbar_title))
                putExtra(Intent.EXTRA_TEXT, articleTitle)
            }
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
        }

        // Back to Top Button Logic
        fabBackToTop.setOnClickListener {
            nestedScrollView.smoothScrollTo(0, 0)
        }
    }

    /**
     * Smoothly scrolls the NestedScrollView to the specified view.
     */
    private fun scrollToView(scrollView: NestedScrollView, view: View) {
        // Since the view is inside a LinearLayout which is the immediate child of NestedScrollView,
        // view.top gives the Y coordinate relative to the LinearLayout top.
        scrollView.smoothScrollTo(0, view.top)
    }
}