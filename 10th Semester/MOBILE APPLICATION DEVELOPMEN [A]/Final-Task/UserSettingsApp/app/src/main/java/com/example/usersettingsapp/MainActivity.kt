package com.example.usersettingsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var etStudentName: EditText
    private lateinit var rgTheme: RadioGroup
    private lateinit var rbLight: RadioButton
    private lateinit var rbDark: RadioButton
    private lateinit var rbSystem: RadioButton
    private lateinit var switchNotif: SwitchCompat
    private lateinit var spinnerLang: Spinner
    private lateinit var seekBarFont: SeekBar
    private lateinit var tvFontSizeLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        title = "Dashboard"

        etStudentName = findViewById(R.id.etStudentName)
        rgTheme = findViewById(R.id.rgTheme)
        rbLight = findViewById(R.id.rbLight)
        rbDark = findViewById(R.id.rbDark)
        rbSystem = findViewById(R.id.rbSystem)
        switchNotif = findViewById(R.id.switchNotif)
        spinnerLang = findViewById(R.id.spinnerLang)
        seekBarFont = findViewById(R.id.seekBarFont)
        tvFontSizeLabel = findViewById(R.id.tvFontSizeLabel)


        seekBarFont.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val size = progress + 12
                tvFontSizeLabel.text = "Font Size: ${size}sp"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        findViewById<Button>(R.id.btnSaveSettings).setOnClickListener { saveSettings() }
        findViewById<Button>(R.id.btnReset).setOnClickListener { resetPreferences() }
        findViewById<Button>(R.id.btnViewSettings).setOnClickListener {
            startActivity(Intent(this, SettingsViewerActivity::class.java))
        }
        findViewById<FloatingActionButton>(R.id.fabProfile).setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        restoreUI()
    }

    private fun saveSettings() {

        val appPrefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val selectedTheme = when (rgTheme.checkedRadioButtonId) {
            R.id.rbLight -> "light"
            R.id.rbDark -> "dark"
            else -> "system"
        }

        appPrefs.edit().apply {
            putString("KEY_THEME", selectedTheme)
            putBoolean("KEY_NOTIFICATIONS", switchNotif.isChecked)
            putString("KEY_LANGUAGE", spinnerLang.selectedItem.toString())
            putInt("KEY_FONT_SIZE", seekBarFont.progress + 12)
            putLong("KEY_LAST_SAVED", System.currentTimeMillis())
            apply()
        }


        val profilePrefs = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        profilePrefs.edit().apply {
            putString("KEY_STUDENT_NAME", etStudentName.text.toString())
            apply()
        }

        Toast.makeText(this, "Settings Saved!", Toast.LENGTH_SHORT).show()
    }

    private fun restoreUI() {
        val appPrefs = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val profilePrefs = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)


        etStudentName.setText(profilePrefs.getString("KEY_STUDENT_NAME", ""))

        when (appPrefs.getString("KEY_THEME", "system")) {
            "light" -> rbLight.isChecked = true
            "dark" -> rbDark.isChecked = true
            "system" -> rbSystem.isChecked = true
        }
        switchNotif.isChecked = appPrefs.getBoolean("KEY_NOTIFICATIONS", true)

        val fontSize = appPrefs.getInt("KEY_FONT_SIZE", 16)
        seekBarFont.progress = fontSize - 12
        tvFontSizeLabel.text = "Font Size: ${fontSize}sp"

        val lang = appPrefs.getString("KEY_LANGUAGE", "English")
        val languages = resources.getStringArray(R.array.language_options)
        spinnerLang.setSelection(languages.indexOf(lang).takeIf { it >= 0 } ?: 0)
    }

    private fun resetPreferences() {

        getSharedPreferences("AppSettings", Context.MODE_PRIVATE).edit().clear().apply()

        getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE).edit().remove("KEY_STUDENT_NAME").apply()


        etStudentName.text.clear()
        rbSystem.isChecked = true
        switchNotif.isChecked = true
        seekBarFont.progress = 4
        spinnerLang.setSelection(0)

        Toast.makeText(this, "Settings reset to default", Toast.LENGTH_SHORT).show()
    }
}