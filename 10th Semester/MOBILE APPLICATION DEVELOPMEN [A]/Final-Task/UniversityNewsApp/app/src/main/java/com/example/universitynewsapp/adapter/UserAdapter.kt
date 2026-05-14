package com.example.universitynewsapp.adapter


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.universitynewsapp.R
import com.example.universitynewsapp.model.User
import com.example.universitynewsapp.ui.UserProfileActivity

class UserAdapter(
    private val users: List<User>,
    private val context: Context
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View)
        : RecyclerView.ViewHolder(view) {

        val card: CardView = view.findViewById(R.id.userCard)
        val initials: TextView = view.findViewById(R.id.tvInitials)
        val name: TextView = view.findViewById(R.id.tvName)
        val username: TextView = view.findViewById(R.id.tvUsername)
        val email: TextView = view.findViewById(R.id.tvEmail)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {

        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_user, parent, false)

        return UserViewHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int
    ) {

        val user = users[position]

        holder.name.text = user.name
        holder.username.text = "@${user.username}"
        holder.email.text = user.email

        val initials = user.name
            .split(" ")
            .map { it.first() }
            .joinToString("")
            .take(2)

        holder.initials.text = initials

        holder.initials.setBackgroundColor(
            Color.rgb(
                (50..200).random(),
                (50..200).random(),
                (50..200).random()
            )
        )

        holder.card.setOnClickListener {

            val intent =
                Intent(context, UserProfileActivity::class.java)

            intent.putExtra("userId", user.id)

            context.startActivity(intent)
        }
    }
}