package com.example.studentauthapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            //v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        val name = findViewById<EditText>(R.id.etName)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val confirm = findViewById<EditText>(R.id.etConfirmPassword)
        val progress = findViewById<ProgressBar>(R.id.progressBar)

        findViewById<Button>(R.id.btnRegister).setOnClickListener {

            val emailTxt = email.text.toString().trim()
            val passTxt = password.text.toString().trim()
            val confirmTxt = confirm.text.toString().trim()

            when {
                name.text.isEmpty() ||
                        emailTxt.isEmpty() ||
                        passTxt.isEmpty() ||
                        confirmTxt.isEmpty() -> {

                    Snackbar.make(it, "All fields required", Snackbar.LENGTH_SHORT).show()
                }

                !Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches() -> {
                    Snackbar.make(it, "Invalid email", Snackbar.LENGTH_SHORT).show()
                }

                passTxt.length < 8 -> {
                    Snackbar.make(it, "Password must be 8 characters", Snackbar.LENGTH_SHORT).show()
                }

                passTxt != confirmTxt -> {
                    Snackbar.make(it, "Passwords do not match", Snackbar.LENGTH_SHORT).show()
                }

                else -> {

                    progress.visibility = View.VISIBLE

                    auth.createUserWithEmailAndPassword(emailTxt, passTxt)
                        .addOnCompleteListener {

                            progress.visibility = View.GONE

                            if (it.isSuccessful) {

                                startActivity(
                                    Intent(this, MainActivity::class.java)
                                )

                                finish()

                            } else {

                                Snackbar.make(
                                    findViewById(android.R.id.content),
                                    it.exception?.message.toString(),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }

        findViewById<TextView>(R.id.tvLogin).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}