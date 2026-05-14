package com.example.universitynewsapp.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.universitynewsapp.R
import kotlinx.coroutines.launch

import com.example.universitynewsapp.repository.PostRepository
import com.example.universitynewsapp.model.Post

class UserProfileActivity : AppCompatActivity() {
    private val repository = PostRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getIntExtra("userId", 1)

        val tvInitials = findViewById<TextView>(R.id.tvProfileInitials)
        val tvName = findViewById<TextView>(R.id.tvProfileName)
        val tvUsername = findViewById<TextView>(R.id.tvProfileUsername)
        val tvEmail = findViewById<TextView>(R.id.tvProfileEmail)
        val tvPhone = findViewById<TextView>(R.id.tvProfilePhone)
        val tvWebsite = findViewById<TextView>(R.id.tvProfileWebsite)
        val tvCompany = findViewById<TextView>(R.id.tvProfileCompany)

        val postsContainer =
            findViewById<LinearLayout>(R.id.postsContainer)

        lifecycleScope.launch {

            val user = repository.getUser(userId)

            val initials = user.name
                .split(" ")
                .map { it.first() }
                .joinToString("")
                .take(2)

            tvInitials.text = initials

            tvInitials.setBackgroundColor(
                Color.rgb(
                    (50..200).random(),
                    (50..200).random(),
                    (50..200).random()
                )
            )

            tvName.text = user.name
            tvUsername.text = "@${user.username}"
            tvEmail.text = user.email
            tvPhone.text = user.phone
            tvWebsite.text = user.website

            tvCompany.text =
                "${user.company.name}\n${user.company.catchPhrase}"

            val posts =
                repository.getPostsByUser(userId)

            posts.forEach { post ->

                val textView =
                    TextView(this@UserProfileActivity)

                textView.text = post.title
                textView.textSize = 18f
                textView.setPadding(20, 20, 20, 20)

                textView.setOnClickListener {

                    val intent =
                        Intent(
                            this@UserProfileActivity,
                            PostDetailActivity::class.java
                        )

                    // Now we use 'post' instead of 'it'
                    intent.putExtra("postId", post.id)
                    intent.putExtra("userId", post.userId)

                    startActivity(intent)
                }

                postsContainer.addView(textView)
            }
        }
    }
}