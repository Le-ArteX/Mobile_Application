package com.example.gradereportapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import android.widget.TableLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.gradereportapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val subjectList = mutableListOf<Subject>()

    data class Subject(
        val name: String,
        val obtained: Double,
        val total: Double,
        val grade: String,
        val points: Double
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize with default subjects (fulfilling "At least 6 rows" requirement)
        setupDefaultSubjects()

        binding.btnAddSubject.setOnClickListener {
            addSubject()
        }

        binding.btnPrintReport.setOnClickListener {
            Toast.makeText(this, "Generating Report PDF...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupDefaultSubjects() {
        val defaults = listOf(
            Triple("Mathematics", 85.0, 100.0),
            Triple("Physics", 72.0, 100.0),
            Triple("Programming", 92.0, 100.0),
            Triple("Chemistry", 35.0, 100.0),
            Triple("English", 65.0, 100.0),
            Triple("Economics", 55.0, 100.0)
        )

        
        val header = binding.gradeTable.getChildAt(0)
        binding.gradeTable.removeAllViews()
        binding.gradeTable.addView(header)

        defaults.forEach { (name, obtained, total) ->
            val grade = calculateGrade(obtained, total)
            val points = getGPAPoints(grade)
            val subject = Subject(name, obtained, total, grade, points)
            subjectList.add(subject)
            appendRowToTable(subject)
        }
        updateSummary()
    }

    private fun addSubject() {
        val name = binding.etSubjectName.text.toString().trim()
        val obtainedStr = binding.etObtainedMarks.text.toString().trim()
        val totalStr = binding.etTotalMarks.text.toString().trim()

        if (name.isEmpty() || obtainedStr.isEmpty() || totalStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val obtained = obtainedStr.toDouble()
        val total = totalStr.toDouble()

        if (total <= 0) {
            Toast.makeText(this, "Total marks must be > 0", Toast.LENGTH_SHORT).show()
            return
        }

        if (obtained > total) {
            Toast.makeText(this, "Obtained marks cannot exceed total", Toast.LENGTH_SHORT).show()
            return
        }

        val grade = calculateGrade(obtained, total)
        val points = getGPAPoints(grade)
        val subject = Subject(name, obtained, total, grade, points)

        subjectList.add(subject)
        appendRowToTable(subject)
        updateSummary()

        // Clear inputs
        binding.etSubjectName.text?.clear()
        binding.etObtainedMarks.text?.clear()
        binding.etTotalMarks.text?.clear()
        
        Toast.makeText(this, "Subject Added Successfully", Toast.LENGTH_SHORT).show()
    }

    private fun appendRowToTable(subject: Subject) {
        val row = TableRow(this)
        val lp = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)
        row.layoutParams = lp
        row.setPadding(0, 8, 0, 8)

        // Set background based on pass/fail and even/odd
        val isPass = subject.grade != "F"
        val bgColor = if (isPass) Color.parseColor("#E8F5E9") else Color.parseColor("#FFEBEE")
        row.setBackgroundColor(bgColor)

        val nameText = createCell(subject.name)
        val obtainedText = createCell(subject.obtained.toString())
        val totalText = createCell(subject.total.toString())
        val gradeText = createCell(subject.grade, isBold = true)

        if (!isPass) {
            gradeText.setTextColor(Color.parseColor("#B71C1C"))
        } else {
            gradeText.setTextColor(Color.parseColor("#2E7D32"))
        }

        row.addView(nameText)
        row.addView(obtainedText)
        row.addView(totalText)
        row.addView(gradeText)

        binding.gradeTable.addView(row)
    }

    private fun createCell(text: String, isBold: Boolean = false): TextView {
        val tv = TextView(this)
        tv.text = text
        tv.gravity = Gravity.CENTER
        tv.setPadding(8, 16, 8, 16)
        tv.setTextColor(Color.parseColor("#212121"))
        if (isBold) tv.setTypeface(null, Typeface.BOLD)
        
        val params = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
        tv.layoutParams = params
        return tv
    }

    private fun updateSummary() {
        val total = subjectList.size
        val passed = subjectList.count { it.grade != "F" }
        val failed = total - passed
        
        val totalPoints = subjectList.sumOf { it.points }
        val gpa = if (total > 0) totalPoints / total else 0.0

        // Remove existing summary row if any
        val lastView = binding.gradeTable.getChildAt(binding.gradeTable.childCount - 1)
        if (lastView is TableRow && lastView.getChildAt(0) is TextView && (lastView.getChildAt(0) as TextView).id == R.id.summaryText) {
             binding.gradeTable.removeView(lastView)
        }
        
     
        val summaryRow = TableRow(this)
        summaryRow.setBackgroundColor(Color.parseColor("#E1F5FE"))
        summaryRow.setPadding(0, 12, 0, 12)
        
        val summaryTv = TextView(this)
        summaryTv.id = R.id.summaryText
        summaryTv.text = "Total Subjects: $total | Passed: $passed | Failed: $failed"
        summaryTv.gravity = Gravity.CENTER
        summaryTv.setTextColor(Color.parseColor("#1A237E"))
        summaryTv.setTypeface(null, Typeface.BOLD)
        
        val spanParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        spanParams.span = 4
        summaryTv.layoutParams = spanParams
        
        summaryRow.addView(summaryTv)
        binding.gradeTable.addView(summaryRow)

        binding.gpaDisplay.text = String.format("%.2f", gpa)
    }

    private fun calculateGrade(marks: Double, total: Double): String {
        val percentage = (marks / total) * 100
        return when {
            percentage >= 90 -> "A+"
            percentage >= 80 -> "A"
            percentage >= 70 -> "B+"
            percentage >= 60 -> "B"
            percentage >= 50 -> "C"
            percentage >= 40 -> "D"
            else -> "F"
        }
    }

    private fun getGPAPoints(grade: String): Double {
        return when (grade) {
            "A+" -> 4.0
            "A" -> 3.7
            "B+" -> 3.3
            "B" -> 3.0
            "C" -> 2.0
            "D" -> 1.0
            else -> 0.0
        }
    }
}
