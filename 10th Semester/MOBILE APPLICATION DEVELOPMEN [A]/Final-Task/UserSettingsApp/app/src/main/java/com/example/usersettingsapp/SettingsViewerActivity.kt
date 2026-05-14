package com.example.usersettingsapp

import android.content.Context
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings_viewer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = "Saved Settings"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val lastSaved = prefs.getLong("KEY_LAST_SAVED", 0L)

        val cardSettings: CardView = findViewById(R.id.cardSettings)
        val tvEmptyMessage: TextView = findViewById(R.id.tvEmptyMessage)

        if (lastSaved == 0L) {
            cardSettings.visibility = View.GONE
            tvEmptyMessage.visibility = View.VISIBLE
        } else {
            cardSettings.visibility = View.VISIBLE
            tvEmptyMessage.visibility = View.GONE

            findViewById<TextView>(R.id.tvSavedTheme).text = formatText("Theme", prefs.getString("KEY_THEME", "N/A"))
            findViewById<TextView>(R.id.tvSavedNotif).text = formatText("Notifications", prefs.getBoolean("KEY_NOTIFICATIONS", true).toString())
            findViewById<TextView>(R.id.tvSavedLang).text = formatText("Language", prefs.getString("KEY_LANGUAGE", "N/A"))
            findViewById<TextView>(R.id.tvSavedFont).text = formatText("Font Size", "${prefs.getInt("KEY_FONT_SIZE", 0)}sp")

            val formatter = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
            val dateString = formatter.format(Date(lastSaved))
            findViewById<TextView>(R.id.tvLastSaved).text = "Last Saved: $dateString"
        }

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun formatText(label: String, value: String?): CharSequence {
        return Html.fromHtml("<b>$label:</b> $value", Html.FROM_HTML_MODE_LEGACY)
    }
}