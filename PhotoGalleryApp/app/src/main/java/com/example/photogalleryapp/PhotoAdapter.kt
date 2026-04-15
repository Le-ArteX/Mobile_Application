package com.example.photogalleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView

class PhotoAdapter(
    private val context: Context,
    private var photos: MutableList<Photo>,
    private var selectionMode: Boolean = false
) : BaseAdapter() {

    private var filteredPhotos: MutableList<Photo> = photos.toMutableList()

    override fun getCount(): Int = filteredPhotos.size

    override fun getItem(position: Int): Photo = filteredPhotos[position]

    override fun getItemId(position: Int): Long = filteredPhotos[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false)
        val photo = getItem(position)

        val ivPhoto = view.findViewById<ImageView>(R.id.ivPhoto)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val cbSelect = view.findViewById<CheckBox>(R.id.cbSelect)

        ivPhoto.setImageResource(photo.resourceId)
        tvTitle.text = photo.title

        cbSelect.visibility = if (selectionMode) View.VISIBLE else View.GONE
        cbSelect.isChecked = photo.isSelected

        cbSelect.setOnClickListener {
            photo.isSelected = cbSelect.isChecked
        }

        return view
    }

    fun setSelectionMode(enabled: Boolean) {
        selectionMode = enabled
        notifyDataSetChanged()
    }

    fun filterByCategory(category: String) {
        filteredPhotos = if (category == "All") {
            photos.toMutableList()
        } else {
            photos.filter { it.category == category }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun removeSelected() {
        val selected = filteredPhotos.filter { it.isSelected }
        photos.removeAll(selected)
        filteredPhotos.removeAll(selected)
        notifyDataSetChanged()
    }

    fun getSelectedCount(): Int = filteredPhotos.count { it.isSelected }

    fun clearSelection() {
        photos.forEach { it.isSelected = false }
        notifyDataSetChanged()
    }

    fun addPhoto(photo: Photo) {
        photos.add(0, photo)
        // Re-apply filter to update filteredPhotos
        // For simplicity, we can just show all or keep current filter logic
        // But here I'll just refresh the view
        filteredPhotos.add(0, photo)
        notifyDataSetChanged()
    }
}
