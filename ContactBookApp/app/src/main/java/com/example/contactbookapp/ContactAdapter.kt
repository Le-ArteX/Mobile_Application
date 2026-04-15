package com.example.contactbookapp

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class ContactAdapter(
    context: Context,
    private var contacts: MutableList<Contact>
) : ArrayAdapter<Contact>(context, R.layout.item_contact, contacts), android.widget.Filterable {

    private var filteredContacts: MutableList<Contact> = contacts
    private val colors = arrayOf(
        "#EF5350", "#EC407A", "#AB47BC", "#7E57C2", "#5C6BC0",
        "#42A5F5", "#29B6F6", "#26C6DA", "#26A69A", "#66BB6A",
        "#9CCC65", "#D4E157", "#FFEE58", "#FFCA28", "#FFA726",
        "#FF7043", "#8D6E63", "#BDBDBD", "#78909C"
    )

    private class ViewHolder {
        var tvAvatar: TextView? = null
        var tvName: TextView? = null
        var tvPhone: TextView? = null
        var ivCall: ImageView? = null
    }

    override fun getCount(): Int {
        return filteredContacts.size
    }

    override fun getItem(position: Int): Contact? {
        return filteredContacts[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
            holder = ViewHolder()
            holder.tvAvatar = view!!.findViewById(R.id.tvAvatar)
            holder.tvName = view.findViewById(R.id.tvName)
            holder.tvPhone = view.findViewById(R.id.tvPhone)
            holder.ivCall = view.findViewById(R.id.ivCall)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        val contact = getItem(position)
        if (contact != null) {
            holder.tvName?.text = contact.name
            holder.tvPhone?.text = contact.phone
            holder.tvAvatar?.text = contact.initial

            // Dynamic background color based on first letter
            val firstChar = contact.name.lowercase().firstOrNull() ?: 'a'
            val colorIndex = Math.abs((firstChar.code - 'a'.code)) % colors.size
            val colorStr = colors[colorIndex]
            val color = Color.parseColor(colorStr)
            holder.tvAvatar?.background?.setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        return view
    }

    override fun getFilter(): android.widget.Filter {
        return object : android.widget.Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.lowercase()
                val results = FilterResults()
                
                if (queryString.isNullOrEmpty()) {
                    results.values = contacts
                } else {
                    val filtered = contacts.filter { 
                        it.name.lowercase().contains(queryString) || it.phone.contains(queryString)
                    }
                    results.values = filtered
                }
                return results
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredContacts = results?.values as? MutableList<Contact> ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}
