package com.example.universitynewsapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.universitynewsapp.R
import com.example.universitynewsapp.adapter.PostAdapter
import com.example.universitynewsapp.model.Post
import com.example.universitynewsapp.repository.PostRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private lateinit var retryButton: Button
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var searchView: SearchView

    private lateinit var adapter: PostAdapter

    private val repository = PostRepository()

    private var allPosts = listOf<Post>()
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
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.errorText)
        retryButton = findViewById(R.id.retryButton)
        swipeRefresh = findViewById(R.id.swipeRefresh)
        searchView = findViewById(R.id.searchView)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        adapter = PostAdapter(emptyList(), this)

        recyclerView.adapter = adapter

        loadPosts()

        retryButton.setOnClickListener {
            loadPosts()
        }

        swipeRefresh.setOnRefreshListener {
            loadPosts()
        }

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(
                    newText: String?
                ): Boolean {

                    val filtered =
                        allPosts.filter {
                            it.title.contains(
                                newText ?: "",
                                true
                            )
                        }

                    adapter.updateList(filtered)

                    return true
                }
            }
        )
    }

    private fun loadPosts() {

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorText.visibility = View.GONE
        retryButton.visibility = View.GONE

        lifecycleScope.launch {

            try {

                val posts = repository.getPosts()

                allPosts = posts

                adapter.updateList(posts)

                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE

            } catch (e: HttpException) {

                showError("Server Error ${e.code()}")

            } catch (e: IOException) {

                showError("No Internet Connection")

            } catch (e: Exception) {

                showError(e.message.toString())
            }

            swipeRefresh.isRefreshing = false
        }
    }

    private fun showError(message: String) {

        progressBar.visibility = View.GONE
        errorText.visibility = View.VISIBLE
        retryButton.visibility = View.VISIBLE

        errorText.text = message
    }
}