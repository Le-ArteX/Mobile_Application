package com.example.studentauthapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_password)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
           // v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val email = findViewById<EditText>(R.id.etEmail)

        findViewById<Button>(R.id.btnReset).setOnClickListener {

            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email.text.toString())
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        Toast.makeText(
                            this,
                            "Reset email sent",
                            Toast.LENGTH_SHORT
                        ).show()

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