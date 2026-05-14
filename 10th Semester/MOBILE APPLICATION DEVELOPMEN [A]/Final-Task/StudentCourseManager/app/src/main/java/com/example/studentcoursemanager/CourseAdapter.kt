package com.example.studentcoursemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CourseAdapter(
    private var courseList: List<Course>,
    private val onEditClick: (Course) -> Unit,
    private val onDeleteClick: (Course) -> Unit,
    private val onItemClick: (Course) -> Unit
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCourseName: TextView = itemView.findViewById(R.id.tvCourseName)
        val tvCourseCode: TextView = itemView.findViewById(R.id.tvCourseCode)
        val tvInstructor: TextView = itemView.findViewById(R.id.tvInstructor)
        val tvCredits: TextView = itemView.findViewById(R.id.tvCredits)
        val tvSchedule: TextView = itemView.findViewById(R.id.tvSchedule)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.tvCourseName.text = course.name
        holder.tvCourseCode.text = course.code
        holder.tvInstructor.text = "Instructor: ${course.instructor}"
        holder.tvCredits.text = "Credits: ${course.credits}"
        holder.tvSchedule.text = course.schedule

        holder.btnEdit.setOnClickListener { onEditClick(course) }
        holder.btnDelete.setOnClickListener { onDeleteClick(course) }
        holder.itemView.setOnClickListener { onItemClick(course) }
    }

    override fun getItemCount(): Int = courseList.size

    fun updateList(newList: List<Course>) {
        courseList = newList
        notifyDataSetChanged()
    }
}