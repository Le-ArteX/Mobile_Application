package com.example.universitynewsapp.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.universitynewsapp.R
import com.example.universitynewsapp.model.Post
import com.example.universitynewsapp.ui.PostDetailActivity

class PostAdapter(
    private var postList: List<Post>,
    private val context: Context
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

       val card: CardView = view.findViewById(R.id.postCard)
       val title: TextView = view.findViewById(R.id.tvTitle)
       val body: TextView = view.findViewById(R.id.tvBody)
      val userId: TextView = view.findViewById(R.id.tvUserId)
        val postId: TextView = view.findViewById(R.id.tvPostId)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(
                R.layout.item_post,
                parent,
                false
            )

        return PostViewHolder(view)
    }

    override fun getItemCount() = postList.size

    override fun onBindViewHolder(
        holder: PostViewHolder,
        position: Int
    ) {

        val post = postList[position]

        holder.title.text = post.title
        holder.body.text = post.body
        holder.userId.text = "User ${post.userId}"
        holder.postId.text = "Post #${post.id}"

        holder.card.setOnClickListener {

            val intent =
                Intent(context, PostDetailActivity::class.java)

            intent.putExtra("postId", post.id)
            intent.putExtra("userId", post.userId)

            context.startActivity(intent)
        }
    }

    fun updateList(newList: List<Post>) {
        postList = newList
        notifyDataSetChanged()
    }
}