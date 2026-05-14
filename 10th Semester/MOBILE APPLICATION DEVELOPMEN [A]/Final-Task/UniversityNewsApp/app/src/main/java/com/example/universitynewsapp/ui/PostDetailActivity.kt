package com.example.universitynewsapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.universitynewsapp.R
import com.example.universitynewsapp.repository.PostRepository
import kotlinx.coroutines.launch


class PostDetailActivity : AppCompatActivity() {
    private val repository = PostRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_post_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val postId = intent.getIntExtra("postId", 1)
        val userId = intent.getIntExtra("userId", 1)

        val tvTitle = findViewById<TextView>(R.id.tvPostTitle)
        val tvBody = findViewById<TextView>(R.id.tvPostBody)

        val tvAuthor = findViewById<TextView>(R.id.tvAuthor)

        val commentsContainer =
            findViewById<LinearLayout>(R.id.commentsContainer)

        lifecycleScope.launch {

            val post = repository.getPost(postId)

            tvTitle.text = post.title
            tvBody.text = post.body

            val user = repository.getUser(userId)

            tvAuthor.text =
                "${user.name}\n${user.email}\n${user.company.name}"

            tvAuthor.setOnClickListener {

                val intent =
                    Intent(
                        this@PostDetailActivity,
                        UserProfileActivity::class.java
                    )

                intent.putExtra("userId", user.id)

                startActivity(intent)
            }

            val comments =
                repository.getComments(postId)

            comments.forEach {

                val textView = TextView(this@PostDetailActivity)

                textView.text =
                    "${it.name}\n${it.email}\n\n${it.body}"

                textView.textSize = 16f
                textView.setPadding(20,20,20,20)

                commentsContainer.addView(textView)
            }
        }
    }
}