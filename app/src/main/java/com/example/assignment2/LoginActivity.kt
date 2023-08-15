package com.example.assignment2

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val newRegisterTextView = findViewById<TextView>(R.id.newRegister)
        val text = "Don't have an account? Register"
        val spannable = SpannableString(text)
        val blueColor = Color.BLUE
        val clickableSpan = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
        spannable.setSpan(ForegroundColorSpan(blueColor), text.indexOf("Register"), text.indexOf("Register") + "Register".length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(clickableSpan, text.indexOf("Register"), text.indexOf("Register") + "Register".length, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)

        newRegisterTextView.text = spannable

        newRegisterTextView.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.loginEmail).text.toString()
            val password = findViewById<EditText>(R.id.loginPassword).text.toString()
            signIn(email, password)
        }
    }

    private fun signIn(email: String, password: String) {
        if (isNotEmpty(email) && isValidEmail(email) && isNotEmpty(password)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // User logged in successfully
                        val user = auth.currentUser
                        // For example, start the main activity
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Optional: Finish the login activity to prevent going back
                    } else {
                        // Handle login failure
                        val errorMessage = task.exception?.message ?: "Login failed"
                        // Display the error message to the user or handle the error
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNotEmpty(value: String): Boolean {
        return value.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }


}